package com.kidzoo.ChildrenToyStore.controller;

import com.kidzoo.ChildrenToyStore.exception.NoToyFoundInPriceRangeException;
import com.kidzoo.ChildrenToyStore.exception.ToysLimitException;
import com.kidzoo.ChildrenToyStore.entity.Toys;
import com.kidzoo.ChildrenToyStore.model.ToyDetailsByStatus;
import com.kidzoo.ChildrenToyStore.model.ToyStatus;
import com.kidzoo.ChildrenToyStore.service.InventoryService;
import com.kidzoo.ChildrenToyStore.service.ToysService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToysControllerTest {
    @InjectMocks
    ToysController toysController;

    @Mock
    ToysService toysService;

    @Mock
    InventoryService inventoryService;


    static List<Toys> emptyToyList = new ArrayList<>();
    static List<Toys>  toysList = Arrays.asList(new Toys(1L, 1000L, 4L,"Ben10","https://images.app.goo.gl/evJHCxUNhWs1EwsD7"),new Toys(2L, 1800L, 7L,"BarbieDoll","https://images.app.goo.gl/BPQJKzSvameJinjm8"),new Toys(3L, 900L, 8L,"minnions","https://images.app.goo.gl/1pNnymKb4AkuMgC17"),new Toys(4L, 200L, 4L,"Transformers","https://images.app.goo.gl/DUn7XQjuQjH4e4HP6"));
    static Long minPriceLimit;
    static Long maxPriceLimit;

    @Test
    public void testGetAllToys()
    {
        when(toysService.getAllToys()).thenReturn(toysList);

        assertThat(toysController.getAllToys().getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(toysController.getAllToys().getBody()).isEqualTo(toysList);
        assertThat(toysController.getAllToys().getBody()).isNotEqualTo(emptyToyList);
    }

    @Test
    public void testGetAllToysByPriceRange()
    {
        minPriceLimit = 100L;
        maxPriceLimit = 900L;
        List<Toys> toysPriceSorted = Arrays.asList(new Toys(4L, 200L, 4L,"Transformers","https://images.app.goo.gl/DUn7XQjuQjH4e4HP6"),new Toys(3L, 900L, 8L,"minnions","https://images.app.goo.gl/1pNnymKb4AkuMgC17"));

        when(toysService.getAllToysByPriceRange(minPriceLimit,maxPriceLimit)).thenReturn(toysPriceSorted);
        when(toysService.getAllToysByPriceRange(null,maxPriceLimit)).thenThrow(new ToysLimitException("400","Please enter the price limits correctly"));
        when(toysService.getAllToysByPriceRange(minPriceLimit,null)).thenThrow(new ToysLimitException("400","Please enter the price limits correctly"));
        when(toysService.getAllToysByPriceRange(null,null)).thenThrow(new ToysLimitException("400","Please enter the price limits correctly"));
        when(toysService.getAllToysByPriceRange(maxPriceLimit,minPriceLimit)).thenThrow(new ToysLimitException("400","Please enter the price limits correctly"));
        when(toysService.getAllToysByPriceRange(3000L,4000L)).thenThrow(new NoToyFoundInPriceRangeException("404","No Toy Found in the Price Range"));
        // then
        assertThat(toysController.getAllToysByPriceRange(minPriceLimit,maxPriceLimit).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(toysController.getAllToysByPriceRange(minPriceLimit,maxPriceLimit).getBody()).isEqualTo(toysPriceSorted);
        assertThat(toysController.getAllToysByPriceRange(minPriceLimit,maxPriceLimit).getBody()).isNotEqualTo(toysList);
        assertThat(toysController.getAllToysByPriceRange(minPriceLimit,maxPriceLimit).getBody()).isNotEqualTo(emptyToyList);

    }

    @Test
    public void testGetToyStatusById() {

        Long id = 1L;
        ToyStatus toy = new ToyStatus(1L,"available");


        when(inventoryService.getToyStatusById(id)).thenReturn(toy);

        assertThat(toysController.getToyStatusById(id).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(toysController.getToyStatusById(id).getStatusCode()).isNotEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(toysController.getToyStatusById(id).getBody()).isEqualTo(toy);

    }


    @Test
    public void testGetAllToysByStockStatus() {

        String status = "available";
        List<ToyStatus> listOfToys = Arrays.asList(new ToyStatus(1L,"available"),new ToyStatus(2L,"available"));

        ToyDetailsByStatus toyDetails = new ToyDetailsByStatus(listOfToys);

        when(inventoryService.getAllToysByStockStatus(status)).thenReturn(toyDetails);

        assertThat(toysController.getAllToysByStockStatus(status).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(toysController.getAllToysByStockStatus(status).getStatusCode()).isNotEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(toysController.getAllToysByStockStatus(status).getBody()).isEqualTo(toyDetails);

    }


}
