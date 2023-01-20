package com.rnd.v2.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.mutiny.CompositeException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.util.Date;

@Provider
public class CustomErrorException implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        ObjectMapper objectMapper = new ObjectMapper();
        Throwable throwable = exception;
        int code = 500;
        if (throwable instanceof WebApplicationException){
            code = ((WebApplicationException) exception)
                    .getResponse()
                    .getStatus();
        }

        if (throwable instanceof CompositeException){
            throwable = throwable.getCause();
        }

        ObjectNode exceptionJson = objectMapper.createObjectNode();
        exceptionJson.put("timestamp", String.valueOf(new Date()));
        exceptionJson.put("exceptionType", throwable.getClass().getName());
        exceptionJson.put("code", code);

        if (exception.getMessage() != null){
            exceptionJson.put("error", throwable.getMessage());
        }

        return Response.status(code)
                .entity(exceptionJson.toPrettyString())
                .build();
    }
}
