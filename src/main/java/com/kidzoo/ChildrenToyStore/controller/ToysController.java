package com.kidzoo.ChildrenToyStore.controller;

import com.kidzoo.ChildrenToyStore.exception.ToysLimitException;
import com.kidzoo.ChildrenToyStore.service.InventoryService;
import com.kidzoo.ChildrenToyStore.service.ToysService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ToysController {

    @Autowired
    private ToysService toyService;

    @Autowired
    private InventoryService inventoryService;

    @ApiOperation(value = "Get the list of toys")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(value = "/getToysList")
    public ResponseEntity<Object> getAllToys() {
        return new ResponseEntity<Object>(toyService.getAllToys(),HttpStatus.OK);
    }

    @ApiOperation(value = "Get the list of toys by price range")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping(value = "/getToysListByPriceRange")
    public ResponseEntity<Object> getAllToysByPriceRange(
            @ApiParam(value = "Minimum price limit for sorting") @RequestParam(value = "MinimumPriceLimit") Long minPriceLimit,@ApiParam(value = "Maximum price limit for sorting") @RequestParam(value = "MaximumPriceLimit") Long maxPriceLimit) {
        return new ResponseEntity<Object>(toyService.getAllToysByPriceRange(Long.valueOf(minPriceLimit), Long.valueOf(maxPriceLimit)),HttpStatus.OK);
    }

    @ApiOperation(value = "Get the list of toys by stock status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping(value = "/getToysListByStock")
    public ResponseEntity<Object> getAllToysByStockStatus(
            @ApiParam(value = "Stock status") @RequestParam(value = "status") String status) {
        return new ResponseEntity<Object>(inventoryService.getAllToysByStockStatus(status),HttpStatus.OK);
    }

    @ApiOperation(value = "Get the toy status by toy id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping(value = "/getToyStatus/{id}")
    public ResponseEntity<Object> getToyStatusById(
            @ApiParam(value = "Stock id") @PathVariable(value = "id") Long id) {
        return new ResponseEntity<Object>(inventoryService.getToyStatusById(id),HttpStatus.OK);
    }




}