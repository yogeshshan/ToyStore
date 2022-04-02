package com.kidzoo.ChildrenToyStore.service;

import com.kidzoo.ChildrenToyStore.dao.ToysRepository;
import com.kidzoo.ChildrenToyStore.entity.Toys;
import com.kidzoo.ChildrenToyStore.exception.NoToyFoundInPriceRangeException;
import com.kidzoo.ChildrenToyStore.exception.ToysLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToysServiceImplementation implements ToysService {

    @Autowired
    ToysRepository toysRepository;

    @Override
    public List<Toys> getAllToys() {
        return toysRepository.findAll();
    }

    @Override
    public List<Toys> getAllToysByPriceRange(Long minPriceLimit, Long maxPriceLimit) throws RuntimeException{
        if(minPriceLimit == null || maxPriceLimit == null) { throw new ToysLimitException("400","Price limit should not be null"); }
        else if(maxPriceLimit < minPriceLimit) { throw new ToysLimitException("400","Minimum price limit should not be greater than Maximum price limit"); }
        List<Toys> listOfToys = toysRepository.findByPriceBetween(minPriceLimit, maxPriceLimit);
        if(listOfToys.isEmpty()) {
             throw new NoToyFoundInPriceRangeException("400","No Toy Found in the Price Range");
        }
        return toysRepository.findByPriceBetween(minPriceLimit, maxPriceLimit);
    }

}
