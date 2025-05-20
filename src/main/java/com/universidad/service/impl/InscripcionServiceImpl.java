package com.universidad.service.impl;

import com.universidad.dto.InscripcionDTO;
import com.universidad.exception.RecursoNoEncontradoException;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.InscripcionService;
import com.universidad.validation.InscripcionValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private InscripcionValidator inscripcionValidator;


    @Override
    @Cacheable(value = "inscripciones")
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "inscripcion", key = "#result.id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO crearInscripcion(InscripcionDTO dto) {

        inscripcionValidator.validacionCompletaInscripcion(dto);

        Estudiante estudiante = estudianteRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));
        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Materia no encontrada"));

        // Validación: no permitir inscribir dos veces la misma materia
        boolean yaInscripto = inscripcionRepository.existsByEstudianteIdAndMateriaId(estudiante.getId(), materia.getId());
        if (yaInscripto) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esta materia");
        }

        // Validación: si quieres, verifica prerequisitos aquí...

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setFechaInscripcion(LocalDate.now());

        Inscripcion guardada = inscripcionRepository.save(inscripcion);
        return convertirADTO(guardada);
    }

    @Override
    @CacheEvict(value = "inscripciones", allEntries = true)
    public void eliminarInscripcion(Long id) {
        inscripcionRepository.deleteById(id);
    }

    @Override
    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO dto) {
        Inscripcion inscripcionExistente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripción no encontrada"));

        // Validación: no permitir cambiar a una materia en la que ya está inscrito
        if (!inscripcionExistente.getMateria().getId().equals(dto.getMateriaId())) {
            boolean yaInscripto = inscripcionRepository.existsByEstudianteIdAndMateriaId(inscripcionExistente.getEstudiante().getId(), dto.getMateriaId());
            if (yaInscripto) {
                throw new IllegalArgumentException("El estudiante ya está inscrito en esta materia");
            }
        }

        // Actualizar los campos de la inscripción
        inscripcionExistente.setMateria(materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Materia no encontrada")));
        inscripcionExistente.setFechaInscripcion(LocalDate.now());

        Inscripcion guardada = inscripcionRepository.save(inscripcionExistente);
        return convertirADTO(guardada);
    }

    private InscripcionDTO convertirADTO(Inscripcion inscripcion) {
        InscripcionDTO dto = new InscripcionDTO();
        dto.setId(inscripcion.getId());
        dto.setEstudianteId(inscripcion.getEstudiante().getId());
        dto.setMateriaId(inscripcion.getMateria().getId());
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        return dto;
    }
}
