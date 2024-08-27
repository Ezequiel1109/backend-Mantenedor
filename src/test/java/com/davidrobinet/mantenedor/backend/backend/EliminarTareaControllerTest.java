package com.davidrobinet.mantenedor.backend.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;

import com.davidrobinet.mantenedor.backend.backend.controller.TareaController;
import com.davidrobinet.mantenedor.backend.backend.service.TareaService;

import jakarta.persistence.EntityNotFoundException;

public class EliminarTareaControllerTest {

    @Mock
    private TareaService tareaService;

    @InjectMocks
    private TareaController tareaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para verificar que el metodo DELETETAREA funcione de manera correcta
    // cuando la tarea existe y se elimina.
    @Test
    void testEliminarLaTarea() {
        // Arrange
        Long tareaId = 1L;
        ResponseEntity<Void> response = tareaController.deleteTarea(tareaId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tareaService, times(1)).eliminarTarea(tareaId);
    }

    // Prueba que simula si una tarea no existe y verifica que se responda con un
    // 404 NOT FOUND
    @Test
    void testEliminarLaTareaNoExiste_EntityNotFoundException() {
        // Arrange
        Long tareaId = 1L;
        doThrow(new EntityNotFoundException()).when(tareaService).eliminarTarea(tareaId);

        ResponseEntity<Void> response = tareaController.deleteTarea(tareaId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tareaService, times(1)).eliminarTarea(tareaId);
    }
}
