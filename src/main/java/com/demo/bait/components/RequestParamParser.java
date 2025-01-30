package com.demo.bait.components;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RequestParamParser {

    public Integer parseId(String id, String paramName) {
        if (id != null && !"null".equalsIgnoreCase(id)) {
            try {
                return Integer.parseInt(id);
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + paramName);
            }
        }
        return null;
    }
}
