package com.zytoune.dailygame.controller.advice;

import com.zytoune.dailygame.exception.NoGameFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.ProblemDetail;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;



class ApplicationControllerAdviceTest {

    @Mock
    private Exception exception;

    @InjectMocks
    private ApplicationControllerAdvice applicationControllerAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shouldHandleGeneralException() {
        when(exception.getMessage()).thenReturn("General exception");

        ProblemDetail responseEntity =  applicationControllerAdvice.exceptionsHandler(exception);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseEntity.getStatus());
        assertEquals("General exception", responseEntity.getProperties().get("erreur"));
    }

    @Test
    void shouldHandleAccessDeniedException() {
        when(exception.getMessage()).thenReturn("Access denied");

        ProblemDetail responseEntity = applicationControllerAdvice.accessDeniedException(new AccessDeniedException("Access denied"));

        assertEquals(HttpStatus.FORBIDDEN.value(), responseEntity.getStatus());
        assertEquals("Access denied", responseEntity.getProperties().get("erreur"));
    }

    @Test
    void shouldHandleRuntimeException() {
        when(exception.getMessage()).thenReturn("Runtime exception");

        ProblemDetail responseEntity = applicationControllerAdvice.runtimeException(new RuntimeException("Runtime exception"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatus());
        assertEquals("Runtime exception", responseEntity.getProperties().get("erreur"));
    }

    @Test
    void shouldHandleNoGameFoundException() {
        when(exception.getMessage()).thenReturn("No game found");

        ProblemDetail responseEntity = applicationControllerAdvice.noGameFoundException(new NoGameFoundException("No game found"));

        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatus());
        assertEquals("No game found", responseEntity.getProperties().get("erreur"));
    }
}