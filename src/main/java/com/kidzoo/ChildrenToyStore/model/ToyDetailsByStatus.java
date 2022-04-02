package com.kidzoo.ChildrenToyStore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToyDetailsByStatus {
    private List<ToyStatus> listOfToyStatus;
}
