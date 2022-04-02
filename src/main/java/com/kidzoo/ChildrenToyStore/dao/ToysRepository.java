package com.kidzoo.ChildrenToyStore.dao;

import com.kidzoo.ChildrenToyStore.entity.Toys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToysRepository  extends JpaRepository<Toys, Integer> {
    List<Toys> findAll();
    List<Toys> findByPriceBetween(Long minPriceLimit,Long maxPriceLimit);
}
