package com.kidzoo.ChildrenToyStore.service;

import com.kidzoo.ChildrenToyStore.entity.Toys;

import java.util.List;

public interface ToysService {
    List<Toys> getAllToys();
    List<Toys> getAllToysByPriceRange(Long range1, Long range2);
}
