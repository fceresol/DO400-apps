package com.redhat.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.redhat.training.service.SolverService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MultiplierResourceTest {

    SolverService solverService;
    MultiplierResource multiplierResource;

    @BeforeEach
    public void setup() {
        solverService = mock(SolverService.class);
        multiplierResource = new MultiplierResource(solverService);
    }
    // Create one unit test for a simple multiplication of two positive values,
    // another one for one positive and one negative value,
    // and one more for the case of an invalid value.

    @Test
    public void simpleTwoPositivesMultiplicationTest() {
        Mockito.when(solverService.solve("2")).thenReturn(Float.valueOf("2"));
        Mockito.when(solverService.solve("4")).thenReturn(Float.valueOf("4"));
        Float actual = multiplierResource.multiply("4", "2");
        assertEquals(8f, actual);
    }

    @Test
    public void oneNegativeAndOnePositiveMultiplicationTest() {
        Mockito.when(solverService.solve("-2")).thenReturn(Float.valueOf("-2"));
        Mockito.when(solverService.solve("4")).thenReturn(Float.valueOf("4"));
        Float actual = multiplierResource.multiply("4", "-2");
        assertEquals(-8f, actual);
    }

    @Test
    public void invalidValueMultiplicationTest() {
        WebApplicationException cause = new WebApplicationException("Unknown error", Response.Status.BAD_REQUEST);
        Mockito.when(solverService.solve("invalidValue")).thenThrow(new ResteasyWebApplicationException(cause));
        Mockito.when(solverService.solve("2")).thenReturn(Float.valueOf("2"));

        Executable multiplication = () -> multiplierResource.multiply("invalidValue", "2");
        assertThrows(ResteasyWebApplicationException.class, multiplication);
    }
}
