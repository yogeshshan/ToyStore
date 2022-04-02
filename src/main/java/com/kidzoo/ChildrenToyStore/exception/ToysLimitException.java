package com.kidzoo.ChildrenToyStore.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ToysLimitException extends RuntimeException {
    private String statusCode;
    private String message;
}
