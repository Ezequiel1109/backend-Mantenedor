package com.davidrobinet.mantenedor.backend.backend.entities;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Schema(description = "Entidad que representa una tarea en el sistema.")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la tarea.", example = "1")
    private Long id;

    @NotBlank(message = "no puede estar vacio.")
    @Size(min = 20, max = 255, message = "debe tener como minimo 20 caracteres y no puede tener mas de 255 caracteres.")
    @Schema(description = "Descripción detallada de la tarea.", example = "Completar la documentación del proyecto.", required = true)
    @Column(nullable = false)
    private String descripcion;

    @Schema(description = "Fecha y hora en que la tarea fue creada.", example = "2024-08-27T12:00:00", required = true)
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Indica si la tarea está vigente o no.", example = "true", required = true)
    @Column(nullable = false)
    private Boolean vigente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

}
