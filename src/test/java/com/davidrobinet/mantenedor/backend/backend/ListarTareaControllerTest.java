package com.davidrobinet.mantenedor.backend.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;

import com.davidrobinet.mantenedor.backend.backend.controller.TareaController;
import com.davidrobinet.mantenedor.backend.backend.entities.Tarea;
import com.davidrobinet.mantenedor.backend.backend.service.TareaService;

public class ListarTareaControllerTest {

    @Mock
    private TareaService tareaService;

    @InjectMocks
    private TareaController tareaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    void testListarTareas() {
        // Arrange
        Tarea tarea1 = new Tarea();
        tarea1.setId(1L);
        tarea1.setDescripcion("Descripción válida con más de 20 caracteres 1.");
        tarea1.setFechaCreacion(LocalDateTime.now());
        tarea1.setVigente(true);

        Tarea tarea2 = new Tarea();
        tarea2.setId(1L);
        tarea2.setDescripcion("Descripción válida con más de 20 caracteres 2.");
        tarea2.setFechaCreacion(LocalDateTime.now());
        tarea2.setVigente(true);

        List<Tarea> tareas = Arrays.asList(tarea1, tarea2);
        when(tareaService.listarTareas()).thenReturn(tareas);

        ResponseEntity<List<Tarea>> response = tareaController.listTareas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(tarea1, response.getBody().get(0));
        assertEquals(tarea2, response.getBody().get(1));
    }
}
