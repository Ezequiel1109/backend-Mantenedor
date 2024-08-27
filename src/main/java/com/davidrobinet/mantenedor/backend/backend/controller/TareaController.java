package com.davidrobinet.mantenedor.backend.backend.controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;

import com.davidrobinet.mantenedor.backend.backend.entities.Tarea;
import com.davidrobinet.mantenedor.backend.backend.service.TareaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tareas")
@Tag(name = "Tareas", description = "API para la gestión de tareas")
public class TareaController {

    @Autowired
    private TareaService tarSer;

    @Operation(summary = "Listar todas las tareas", description = "Obtiene una lista de todas las tareas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tarea.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })

    @GetMapping
    public ResponseEntity<List<Tarea>> listTareas() {
        List<Tarea> tareas = tarSer.listarTareas();
        return ResponseEntity.ok(tareas);
    }

    @Operation(summary = "Agregar una nueva tarea", description = "Crea una nueva tarea y la guarda en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea creada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tarea.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud o validación fallida", content = @Content)
    })

    @PostMapping("/agregar")
    public ResponseEntity<?> addTareas(@Valid @RequestBody Tarea tarea, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        // errores de validacion
        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("mensaje", "Error en la validación de los campos");
            response.put("mensaje", errores);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Guardamos la tarea
        try {
            tarea.setFechaCreacion(LocalDateTime.now());
            tarea.setVigente(true);
            Tarea guardarTarea = tarSer.agregarTarea(tarea);
            response.put("mensaje", "Tarea creada con éxito");
            response.put("registro", guardarTarea);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al crear la tarea");
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @Operation(summary = "Editar una tarea existente", description = "Actualiza los detalles de una tarea existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tarea.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud o validación fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content)
    })

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> updateTarea(@PathVariable("id") Long id, @Valid @RequestBody Tarea tareaUp,
            BindingResult result) {
        if (result.hasErrors()) {
            // Captura y devuelve los errores de validación
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            Tarea tarea = tarSer.editarTarea(id, tareaUp);

            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            // Manejo de la excepción si la tarea no es encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una tarea", description = "Elimina una tarea existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarea eliminada con éxito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> deleteTarea(@PathVariable("id") Long id) {
        try {
            tarSer.eliminarTarea(id);
            return ResponseEntity.noContent().build(); // Responde con 204 No Content si la eliminación fue exitosa
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found si la tarea no existe
        }
    }

}
