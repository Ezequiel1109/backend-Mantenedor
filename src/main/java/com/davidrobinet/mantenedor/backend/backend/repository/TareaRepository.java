package com.davidrobinet.mantenedor.backend.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davidrobinet.mantenedor.backend.backend.entities.Tarea;


public interface TareaRepository extends JpaRepository<Tarea, Long> {

}
