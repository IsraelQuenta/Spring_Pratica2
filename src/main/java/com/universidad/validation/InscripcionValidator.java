package com.universidad.validation;

import org.springframework.stereotype.Component;
import com.universidad.dto.InscripcionDTO;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.MateriaRepository;
import java.time.LocalDate;

@Component
public class InscripcionValidator {

    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;

    public InscripcionValidator(EstudianteRepository estudianteRepository, 
                              MateriaRepository materiaRepository) {
        this.estudianteRepository = estudianteRepository;
        this.materiaRepository = materiaRepository;
    }

    public void validaEstudianteExistente(Long estudianteId) {
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new IllegalArgumentException("No existe un estudiante con el ID proporcionado");
        }
    }

    public void validaMateriaExistente(Long materiaId) {
        if (!materiaRepository.existsById(materiaId)) {
            throw new IllegalArgumentException("No existe una materia con el ID proporcionado");
        }
    }

    public void validaFechaInscripcion(LocalDate fechaInscripcion) {
        if (fechaInscripcion == null) {
            throw new IllegalArgumentException("La fecha de inscripción no puede ser nula");
        }
        
        if (fechaInscripcion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inscripción no puede ser futura");
        }
    }

    public void validacionCompletaInscripcion(InscripcionDTO inscripcion) {
        validaEstudianteExistente(inscripcion.getEstudianteId());
        validaMateriaExistente(inscripcion.getMateriaId());
        validaFechaInscripcion(inscripcion.getFechaInscripcion());
    }

    public class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}