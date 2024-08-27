package com.davidrobinet.mantenedor.backend.backend;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.davidrobinet.mantenedor.backend.backend.entities.Tarea;
import com.davidrobinet.mantenedor.backend.backend.repository.TareaRepository;
import com.davidrobinet.mantenedor.backend.backend.service.TareaService;

public class EditarTareaControllerTest {

    @InjectMocks
    private TareaService tareaService;

    @Mock
    private TareaRepository tareaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Pruebas unitarias para el metodo editarTarea
    // Prueba para editar de manera correcta la tarea.
    @Test
    void testEditarTarea() {
        // Arrange
        Long tareaId = 1L;
        Tarea tareaExistente = new Tarea();
        tareaExistente.setId(tareaId);
        tareaExistente.setDescripcion("Descripción original");

        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setDescripcion("Descripción actualizada");

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tareaExistente));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaActualizada);

        // Act
        Tarea resultado = tareaService.editarTarea(tareaId, tareaActualizada);

        // Assert
        assertEquals("Descripción actualizada", resultado.getDescripcion());
    }

    // Prueba para editar la Tarea y no es encontrada, por la cual lanza la
    // excepcion correspondiente.
    @Test
    void testEditarTareaSinLaTareaExistente() {
        // Arrange
        Long tareaId = 2L;
        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setDescripcion("Descripción actualizada");

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tareaService.editarTarea(tareaId, tareaActualizada);
        });

        // Verifica el mensaje exacto
        String expectedMessage = "Tarea no encontrada";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // Prueba para asegurar que el repo SAVE se llame correctamente.
    @Test
    void testEditarTareaYVerificaLaLlamadaSave() {
        // Arrange
        Long tareaId = 1L;
        Tarea tareaExistente = new Tarea();
        tareaExistente.setId(tareaId);
        tareaExistente.setDescripcion("Descripción original");

        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setDescripcion("Descripción actualizada");

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tareaExistente));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaActualizada);

        // Act
        tareaService.editarTarea(tareaId, tareaActualizada);

        // Assert
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    // Prueba apra verificar la actualizacion de la descripcion.
    @Test
    void testEditarTareaVerificarLaActualizacionDeLaDescripcion() {
        // Arrange
        Long tareaId = 1L;
        Tarea tareaExistente = new Tarea();
        tareaExistente.setId(tareaId);
        tareaExistente.setDescripcion("Descripción original");

        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setDescripcion("Descripción actualizada");

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tareaExistente));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(invocation -> {
            Tarea tarea = invocation.getArgument(0);
            tarea.setDescripcion("Descripción actualizada");
            return tarea;
        });

        // Act
        Tarea resultado = tareaService.editarTarea(tareaId, tareaActualizada);

        // Assert
        assertEquals("Descripción actualizada", resultado.getDescripcion());
    }

    // Prueba de validacion para que no se llame al metodo SAVE si la tarea no
    // existe.
    @Test
    void testEditarLaTareaSinLlamadaSaveCuandoNoExisteLaTarea() {
        // Arrange
        Long tareaId = 2L;
        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setDescripcion("Descripción actualizada");

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            tareaService.editarTarea(tareaId, tareaActualizada);
        });

        // Verifica que save no fue llamado
        verify(tareaRepository, never()).save(any(Tarea.class));
    }

    // Prueba para manejar las excepciones en el repositorio
    @Test
    void testeEditarLaTareaManejandoExcepcionesSave() {
        // Arrange
        Long tareaId = 1L;
        Tarea tareaExistente = new Tarea();
        tareaExistente.setId(tareaId);
        tareaExistente.setDescripcion("Descripción original");

        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setDescripcion("Descripción actualizada");

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tareaExistente));
        when(tareaRepository.save(any(Tarea.class))).thenThrow(new RuntimeException("Error en el repositorio"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tareaService.editarTarea(tareaId, tareaActualizada);
        });

        assertEquals("Error en el repositorio", exception.getMessage());

    }

    // Prueba para verificar que TAREA.SETDESCRIPCION es invocada de manera
    // correcta.
    @Test
    void testEditarTareaVerificaSetDescripcion() {
        // Arrange
        Long tareaId = 1L;
        Tarea tareaExistente = new Tarea(); // Usa una instancia real aquí
        tareaExistente.setId(tareaId);
        tareaExistente.setDescripcion("Descripción original");

        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setDescripcion("Descripción actualizada");

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tareaExistente));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(invocation -> {
            Tarea tarea = invocation.getArgument(0);
            return tarea; // Devuelve la misma tarea con la descripción actualizada
        });

        // Act
        tareaService.editarTarea(tareaId, tareaActualizada);

        // Assert
        assertEquals("Descripción actualizada", tareaExistente.getDescripcion(),
                "La descripción de la tarea no fue actualizada correctamente.");
    }

}