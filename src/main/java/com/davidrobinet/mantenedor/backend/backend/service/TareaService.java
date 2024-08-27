package com.davidrobinet.mantenedor.backend.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidrobinet.mantenedor.backend.backend.entities.Tarea;
import com.davidrobinet.mantenedor.backend.backend.repository.TareaRepository;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tarRepo;

    public List<Tarea> listarTareas() {
        return tarRepo.findAll();
    }

    public Tarea agregarTarea(Tarea tarea) {
        return tarRepo.save(tarea);
    }

    public Tarea editarTarea(Long id, Tarea tareaUp) {
        Tarea tarea = tarRepo.findById(id).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        tarea.setDescripcion(tareaUp.getDescripcion());
        return tarRepo.save(tarea);
    }

    public void eliminarTarea(Long id) {
        tarRepo.deleteById(id);
    }

}
