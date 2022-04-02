package com.kidzoo.ChildrenToyStore.service;

import com.kidzoo.ChildrenToyStore.entity.Toys;
import com.kidzoo.ChildrenToyStore.model.ToyDetailsByStatus;
import com.kidzoo.ChildrenToyStore.model.ToyStatus;

import java.util.List;

public interface InventoryService {
  ToyStatus getToyStatusById(Long toyId);
  ToyDetailsByStatus getAllToysByStockStatus(String status);
}
