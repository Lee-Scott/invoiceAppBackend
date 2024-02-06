package com.familyfirstsoftware.invoiceApplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {
    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason; // if error we send reason
    protected String message; // if no error, we send a message
    protected String developerMessage;
    protected Map<?, ?> data; // any type data
}
