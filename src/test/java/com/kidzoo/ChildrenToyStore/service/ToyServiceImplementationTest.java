package com.kidzoo.ChildrenToyStore.service;

import com.kidzoo.ChildrenToyStore.exception.NoToyFoundInPriceRangeException;
import com.kidzoo.ChildrenToyStore.exception.ToysLimitException;
import com.kidzoo.ChildrenToyStore.dao.ToysRepository;
import com.kidzoo.ChildrenToyStore.entity.Toys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToyServiceImplementationTest {
    @InjectMocks
    ToysServiceImplementation toysServiceImplementation;

    @Mock
    ToysRepository toysRepository;

    static List<Toys> toys = Arrays.asList(new Toys(1L, 1000L, 4L,"Ben10","https://images.app.goo.gl/evJHCxUNhWs1EwsD7"),new Toys(2L, 1800L, 7L,"BarbieDoll","https://images.app.goo.gl/BPQJKzSvameJinjm8"),new Toys(3L, 900L, 8L,"minnions","https://images.app.goo.gl/1pNnymKb4AkuMgC17"),new Toys(4L, 200L, 4L,"Transformers","https://images.app.goo.gl/DUn7XQjuQjH4e4HP6"));
    static List<Toys> emptyToysList = new ArrayList<>();
    @Test
    public void testGetAllToysByPriceRange()
    {
        Long min = 100L;
        Long max = 900L;

        List<Toys> toysPriceSorted = Arrays.asList(new Toys(3L, 900L, 8L,"minnions","https://images.app.goo.gl/1pNnymKb4AkuMgC17"),new Toys(4L, 200L, 4L,"Transformers","https://images.app.goo.gl/DUn7XQjuQjH4e4HP6"));
        when(toysRepository.findByPriceBetween(min,max)).thenReturn(toysPriceSorted);


        assertThat(toysServiceImplementation.getAllToysByPriceRange(min,max)).isEqualTo(toysPriceSorted);
        assertThat(toysServiceImplementation.getAllToysByPriceRange(min,max)).isNotEqualTo(emptyToysList);
        assertThrows(ToysLimitException.class,() -> toysServiceImplementation.getAllToysByPriceRange(null,max));
        assertThrows(ToysLimitException.class,() -> toysServiceImplementation.getAllToysByPriceRange(null,null));
        assertThrows(ToysLimitException.class,() -> toysServiceImplementation.getAllToysByPriceRange(max,min));
        assertThrows(NoToyFoundInPriceRangeException.class,() -> toysServiceImplementation.getAllToysByPriceRange(3000L,5000L));
    }

    @Test
    public void testGetAllToys()
    {
        when(toysRepository.findAll()).thenReturn(toys);

        assertThat(toysServiceImplementation.getAllToys()).isEqualTo(toys);
        assertThat(toysServiceImplementation.getAllToys()).isNotEqualTo(emptyToysList);

    }




}
