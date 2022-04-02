package com.kidzoo.ChildrenToyStore.model;

import com.kidzoo.ChildrenToyStore.entity.Toys;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ToysList {
    private List<Toys> toysList;

    public ToysList() {
        toysList = new ArrayList<>();
    }
}
