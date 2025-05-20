package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.InscripcionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;
    private static final Logger logger = LoggerFactory.getLogger(InscripcionController.class);

    @GetMapping("/estudiante/{estudianteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudiante(@PathVariable Long estudianteId) {
        long inicio = System.currentTimeMillis();
        logger.info("[INSCRIPCION] Inicio obtenerInscripcionesPorEstudiante: {}", inicio);
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorEstudiante(estudianteId);
        logger.info("[INSCRIPCION] Fin obtenerInscripcionesPorEstudiante: {} (Duracion: {} ms)", System.currentTimeMillis(), (System.currentTimeMillis() - inicio));
        return ResponseEntity.ok(inscripciones);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<InscripcionDTO> crearInscripcion(@Valid @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO nueva = inscripcionService.crearInscripcion(inscripcionDTO);
        return ResponseEntity.status(201).body(nueva);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}
