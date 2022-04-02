package com.kidzoo.ChildrenToyStore.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoToyFoundInPriceRangeException extends RuntimeException{
    private String statusCode;
    private String message;
}
