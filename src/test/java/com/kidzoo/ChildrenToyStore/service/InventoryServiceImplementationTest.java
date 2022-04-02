package com.kidzoo.ChildrenToyStore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidzoo.ChildrenToyStore.dao.ToysRepository;
import com.kidzoo.ChildrenToyStore.entity.Toys;
import com.kidzoo.ChildrenToyStore.exception.IncorrectToyStatusException;
import com.kidzoo.ChildrenToyStore.exception.ToyNotFoundException;
import com.kidzoo.ChildrenToyStore.exception.ToysLimitException;
import com.kidzoo.ChildrenToyStore.model.ToyDetailsByStatus;
import com.kidzoo.ChildrenToyStore.model.ToyStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceImplementationTest {

    @InjectMocks
    InventoryServiceImplementation inventoryServiceImplementation;

    @Value("${inventory.service.base-url}")
    private String baseUrl;

    @Value("${inventory.service.findByStatus}")
    private String findByStatus;

    @Value("${inventory.service.findStatusByToyId}")
    private String findStatusByToyId;

    @Value("${inventory.service.getStatus}")
    private String getStatus;


    private static List<ToyStatus> listOfToys = Arrays.asList(new ToyStatus(1L,"available"),new ToyStatus(2L,"available"),new ToyStatus(3L,"backorder"),new ToyStatus(4L,"backorder"),new ToyStatus(5L,"outofstock"));


    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetToyStatusById()
    {
        Long toyId = 1L;
        String mockUrl = "http://inventory.kidzoo.com/v2/inventory/1";
        ToyStatus toyStatusResponse;
        RestTemplate restTemplate = new RestTemplate();

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
            toyStatusResponse = restTemplate.getForObject(mockUrl, ToyStatus.class);

        }
        catch (Exception exception) {
            throw new ToyNotFoundException("404", "Toy not found");
        }

        assertThat(toyStatusResponse).isEqualTo(new ToyStatus(1L,"available"));
        assertThrows(ToyNotFoundException.class,() -> inventoryServiceImplementation.getToyStatusById(9L));
        assertThat(toyStatusResponse).isNotEqualTo(new ToyStatus(4L,"available"));
        assertThat(toyStatusResponse.getStatus()).isNotEqualTo(HttpStatus.OK);


    }

    @Test
    public void testGetAllToysByStockStatus() {
        String status = "available";
        String mockUrl = "http://inventory.kidzoo.com/v2/inventory/findByStatus";
        RestTemplate restTemplate = new RestTemplate();
        ToyDetailsByStatus listOfToyStatusMock = new ToyDetailsByStatus();
        ToyDetailsByStatus listOfToyStatus = new ToyDetailsByStatus();
        List<ToyStatus> availableStock = Arrays.asList(new ToyStatus(1L,"available"),new ToyStatus(2L,"available"));
        listOfToyStatusMock.setListOfToyStatus(availableStock);
        try {
            MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();

            List<ToyStatus> filteredStatus =listOfToys.stream().filter(toy -> toy.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());

            if(filteredStatus.isEmpty()) {
                throw new IncorrectToyStatusException();
            }
            String responseStatus = objectMapper.writeValueAsString(filteredStatus);
            server.expect(manyTimes(), requestTo(mockUrl)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(responseStatus, MediaType.APPLICATION_JSON));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");

            Map<String, String> params = new HashMap<String, String>();
            params.put("status", status);

            HttpEntity entity = new HttpEntity(headers);

            HttpEntity<ToyStatus[]> responseFromInventory = restTemplate.exchange(mockUrl, HttpMethod.GET, entity, ToyStatus[].class, params);

            listOfToyStatus.setListOfToyStatus(Arrays.asList(responseFromInventory.getBody()));
        } catch (Exception exception) {
            throw new IncorrectToyStatusException("400", "Invalid status value");
        }

        assertThat(listOfToyStatus).isEqualTo(listOfToyStatusMock);
        assertThrows(IncorrectToyStatusException.class,() -> inventoryServiceImplementation.getAllToysByStockStatus("Test"));
        assertThat(listOfToyStatus).isNotEqualTo(null);

    }
}
