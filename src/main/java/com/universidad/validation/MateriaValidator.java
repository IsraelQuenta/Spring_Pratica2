package com.universidad.validation;

import org.springframework.stereotype.Component;
import com.universidad.dto.MateriaDTO;
import com.universidad.repository.MateriaRepository;
import com.universidad.repository.DocenteRepository;
import java.util.List;

@Component
public class MateriaValidator {

    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;

    public MateriaValidator(MateriaRepository materiaRepository, 
                          DocenteRepository docenteRepository) {
        this.materiaRepository = materiaRepository;
        this.docenteRepository = docenteRepository;
    }

    public void validaCodigoUnico(String codigoUnico, Long idExcluir) {
        if (idExcluir == null) {
            if (materiaRepository.existsByCodigoUnico(codigoUnico)) {
                throw new IllegalArgumentException("Ya existe una materia con este código único");
            }
        } else {
            if (materiaRepository.existsByCodigoUnicoAndIdNot(codigoUnico, idExcluir)) {
                throw new IllegalArgumentException("Ya existe otra materia con este código único");
            }
        }
    }

    public void validaDocenteExistente(Long docenteId) {
        if (docenteId != null && !docenteRepository.existsById(docenteId)) {
            throw new IllegalArgumentException("No existe un docente con el ID proporcionado");
        }
    }

    public void validaPrerequisitos(List<Long> prerequisitos) {
        if (prerequisitos != null) {
            for (Long materiaId : prerequisitos) {
                if (!materiaRepository.existsById(materiaId)) {
                    throw new IllegalArgumentException("No existe una materia con ID " + materiaId + " en los prerequisitos");
                }
            }
        }
    }

    public void validaEsPrerequisitoDe(List<Long> esPrerequisitoDe) {
        if (esPrerequisitoDe != null) {
            for (Long materiaId : esPrerequisitoDe) {
                if (!materiaRepository.existsById(materiaId)) {
                    throw new IllegalArgumentException("No existe una materia con ID " + materiaId + " en 'es prerequisito de'");
                }
            }
        }
    }

    public void validaNoAutoPrerequisito(Long materiaId, List<Long> prerequisitos) {
        if (materiaId != null && prerequisitos != null && prerequisitos.contains(materiaId)) {
            throw new IllegalArgumentException("Una materia no puede ser prerequisito de sí misma");
        }
    }

    public void validacionCompletaMateria(MateriaDTO materia) {
        validacionCompletaMateria(materia, null);
    }

    public void validacionCompletaMateria(MateriaDTO materia, Long idExcluir) {
        validaCodigoUnico(materia.getCodigoUnico(), idExcluir);
        validaDocenteExistente(materia.getDocenteId());
        validaPrerequisitos(materia.getPrerequisitos());
        validaEsPrerequisitoDe(materia.getEsPrerequisitoDe());
        validaNoAutoPrerequisito(idExcluir != null ? idExcluir : materia.getId(), materia.getPrerequisitos());
    }

    public class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}