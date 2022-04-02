package com.kidzoo.ChildrenToyStore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidzoo.ChildrenToyStore.exception.IncorrectToyStatusException;
import com.kidzoo.ChildrenToyStore.exception.ToyNotFoundException;
import com.kidzoo.ChildrenToyStore.model.ToyDetailsByStatus;
import com.kidzoo.ChildrenToyStore.model.ToyStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@Service
public class InventoryServiceImplementation implements InventoryService{

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${inventory.service.base-url}")
    private String baseUrl;

    @Value("${inventory.service.findByStatus}")
    private String findByStatus;

    @Value("${inventory.service.findStatusByToyId}")
    private String findStatusByToyId;

    @Value("${inventory.service.getStatus}")
    private String getStatus;


    private List<ToyStatus> listOfToys = Arrays.asList(new ToyStatus(1L,"available"),new ToyStatus(2L,"available"),new ToyStatus(3L,"backorder"),new ToyStatus(4L,"backorder"),new ToyStatus(5L,"outofstock"));


    ObjectMapper objectMapper = new ObjectMapper();


    public ToyStatus getToyStatusById(Long toyId) throws ToyNotFoundException  {
        final String mockUrl = baseUrl+toyId;
        final String getURLForToyId = baseUrl+findStatusByToyId;
        ToyStatus toyStatusResponse = new ToyStatus();

        try {
            Optional<ToyStatus> toyStatusFiltered = listOfToys.stream().filter(toy -> toy.getId() == toyId).findFirst();
            if (toyStatusFiltered.isEmpty()) {
                throw new ToyNotFoundException();
            }
            MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
            String response = null;
            response = objectMapper.writeValueAsString(toyStatusFiltered.get());
            server.expect(manyTimes(), requestTo(mockUrl)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
            toyStatusResponse = restTemplate.getForObject(getURLForToyId, ToyStatus.class, toyId);
        }
        catch (Exception exception) {
            throw new ToyNotFoundException("404", "Toy not found");
        }

        return toyStatusResponse;
    }

    public ToyDetailsByStatus getAllToysByStockStatus(String status) throws IncorrectToyStatusException{
        final String url = baseUrl+findByStatus+getStatus+status;
        ToyDetailsByStatus listOfToyStatus = new ToyDetailsByStatus();
        try {
            MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
            List<ToyStatus> listOfToyStatus1 = Arrays.asList(new ToyStatus(1L,"available"),new ToyStatus(2L,"available"),new ToyStatus(3L,"backorder"),new ToyStatus(4L,"backorder"),new ToyStatus(5L,"outofstock"));

            List<ToyStatus> filteredStatus =listOfToyStatus1.stream().filter(toy -> toy.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());

            if(filteredStatus.isEmpty()) {
             throw new IncorrectToyStatusException();
            }
            String responseStatus = objectMapper.writeValueAsString(filteredStatus);
            server.expect(manyTimes(), requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(responseStatus, MediaType.APPLICATION_JSON));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");

            Map<String, String> params = new HashMap<String, String>();
            params.put("status", status);

            HttpEntity entity = new HttpEntity(headers);

            HttpEntity<ToyStatus[]> responseFromInventory = restTemplate.exchange(url, HttpMethod.GET, entity, ToyStatus[].class, params);

            listOfToyStatus.setListOfToyStatus(Arrays.asList(responseFromInventory.getBody()));
            return listOfToyStatus;
        } catch (Exception exception) {
            throw new IncorrectToyStatusException("400", "Invalid status value");
        }

    }

}
