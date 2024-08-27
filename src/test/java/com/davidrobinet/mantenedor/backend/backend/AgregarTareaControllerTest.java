package com.davidrobinet.mantenedor.backend.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.validation.*;

import com.davidrobinet.mantenedor.backend.backend.controller.TareaController;
import com.davidrobinet.mantenedor.backend.backend.entities.Tarea;
import com.davidrobinet.mantenedor.backend.backend.service.TareaService;

public class AgregarTareaControllerTest {

    @InjectMocks
    private TareaController tareaController;

    @Mock
    private TareaService tareaService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba que verifica cuando no hay errores y el servicio guarda la tarea
    // correctamente, devolviendo un status 201 CREATED y un mensaje de éxito al
    // guardar la tarea.
    @Test
    void testAgregarTarea() {
        // Arrange
        Tarea tarea = new Tarea();
        tarea.setDescripcion("Descripción válida con más de 20 caracteres.");

        Tarea tareaGuardada = new Tarea();
        tareaGuardada.setId(1L);
        tareaGuardada.setDescripcion("Descripción válida con más de 20 caracteres.");
        tareaGuardada.setFechaCreacion(LocalDateTime.now());
        tareaGuardada.setVigente(true);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(tareaService.agregarTarea(any(Tarea.class))).thenReturn(tareaGuardada);

        ResponseEntity<?> response = tareaController.addTareas(tarea, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verificación de nulos
        Object body = response.getBody();
        assertTrue(body instanceof Map<?, ?>); // Verifica que el cuerpo es un mapa

        @SuppressWarnings("unchecked")
        Map<String, Object> cuerpo = (Map<String, Object>) body; // Cast seguro

        assertEquals("Tarea creada con éxito", cuerpo.get("mensaje"));
        assertEquals(tareaGuardada, cuerpo.get("registro"));

    }

    // Prueba que verifica cuando hay errores de validación, lanzando un respuesta
    // con status 400 BAD REQUEST con los mensajes de error de validación.
    @Test
    void testAgregarTareasValidandoConErrores() {
        // Arrange
        Tarea tarea = new Tarea();
        List<FieldError> errores = new ArrayList<>();
        errores.add(new FieldError("tarea", "descripcion", "La descripción no puede estar vacía"));

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(errores);

        ResponseEntity<?> response = tareaController.addTareas(tarea, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Object body = response.getBody();
        assertTrue(body instanceof Map<?, ?>);

        @SuppressWarnings("unchecked")
        Map<String, Object> cuerpo = (Map<String, Object>) body;

        Object cuerpoMsj = cuerpo.get("mensaje");
        assertTrue(cuerpoMsj instanceof List<?>);

        @SuppressWarnings("unchecked")
        List<String> error = (List<String>) cuerpoMsj;

        assertEquals(1, error.size());
        assertEquals("El campo 'descripcion' La descripción no puede estar vacía", error.get(0));

    }

    // Prueba que al ocurrir una excepción al intentar guardar la tarea, el
    // controlador devolvera un status 400 BAD REQUEST,
    // con el mensaje de error de la excepción.
    @Test
    void testAgregarTareaException() {
        // Arrange
        Tarea tarea = new Tarea();
        tarea.setDescripcion("tarea con excepción");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(tareaService.agregarTarea(any(Tarea.class))).thenThrow(new RuntimeException("Error al agregar tarea"));

        ResponseEntity<?> response = tareaController.addTareas(tarea, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Object body = response.getBody();
        assertTrue(body instanceof Map<?, ?>);

        @SuppressWarnings("unchecked")
        Map<String, Object> cuerpo = (Map<String, Object>) body;

        assertEquals("Error al agregar tarea", cuerpo.get("mensaje"));
    }

    // Prueba que simula una solicitud con múltiples errores de validación y
    // verifica que el controlador los maneje de manera correcta.
    @Test
    void testAgregarTareasDeValidacionConMultiplesErrores() {
        // Arrange
        Tarea tarea = new Tarea();
        List<FieldError> errores = new ArrayList<>();
        errores.add(new FieldError("tarea", "descripcion", "La descripción no puede estar vacía"));
        errores.add(new FieldError("tarea", "fechaCreacion", "La fecha de creación es obligatoria"));

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(errores);

        // Act
        ResponseEntity<?> response = tareaController.addTareas(tarea, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Object body = response.getBody();
        assertTrue(body instanceof Map<?, ?>);

        @SuppressWarnings("unchecked")
        Map<String, Object> cuerpo = (Map<String, Object>) body;

        Object cuerpoMsj = cuerpo.get("mensaje");
        assertTrue(cuerpoMsj instanceof List<?>);

        @SuppressWarnings("unchecked")
        List<String> error = (List<String>) cuerpoMsj;

        assertEquals(2, error.size());
        assertEquals("El campo 'descripcion' La descripción no puede estar vacía", error.get(0));
        assertEquals("El campo 'fechaCreacion' La fecha de creación es obligatoria", error.get(1));
    }

    // Prueba para verificar que el controlador maneje correctamente una solicitud,
    // donde algunos campos no están completos o son nulos,
    // como en este caso donde VIGENTE es NULL.
    @Test
    void testAgregarTareaConDatosIncompletos() {
        // Arrange
        Tarea tarea = new Tarea();
        tarea.setDescripcion("Descripción válida");

        Tarea tareaGuardada = new Tarea();
        tareaGuardada.setId(1L);
        tareaGuardada.setDescripcion("Descripción válida");
        tareaGuardada.setFechaCreacion(LocalDateTime.now());
        tareaGuardada.setVigente(null); // Vigente es null

        when(bindingResult.hasErrors()).thenReturn(false);
        when(tareaService.agregarTarea(any(Tarea.class))).thenReturn(tareaGuardada);

        // Act
        ResponseEntity<?> response = tareaController.addTareas(tarea, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Object body = response.getBody();
        assertTrue(body instanceof Map<?, ?>);

        @SuppressWarnings("unchecked")
        Map<String, Object> cuerpo = (Map<String, Object>) body;

        assertEquals("Tarea creada con éxito", cuerpo.get("mensaje"));
        assertEquals(tareaGuardada, cuerpo.get("registro"));

        Tarea registro = (Tarea) cuerpo.get("registro");
        assertNotNull(registro.getId());
        assertNull(registro.getVigente()); // Verificar que vigente es null
    }

}
