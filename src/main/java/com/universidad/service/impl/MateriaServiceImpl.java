package com.universidad.service.impl;

import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IMateriaService;
import com.universidad.validation.MateriaValidator;
import com.universidad.dto.MateriaDTO;
import com.universidad.exception.RecursoNoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaServiceImpl implements IMateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private MateriaValidator materiaValidator;

    @Override
    @Cacheable(value = "materias")
    public List<MateriaDTO> obtenerTodasLasMaterias() {
        return materiaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "materia", key = "#id")
    public MateriaDTO obtenerMateriaPorId(Long id) {
        return materiaRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "materia", key = "#codigoUnico")
    public MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico) {
        Materia materia = materiaRepository.findByCodigoUnico(codigoUnico);
        return convertirADTO(materia);
    }

    @Override
    @CachePut(value = "materia", key = "#result.id")
    @CacheEvict(value = "materias", allEntries = true)
    public MateriaDTO crearMateria(MateriaDTO dto) {

        materiaValidator.validacionCompletaMateria(dto);
        Materia materia = convertirAEntidad(dto);
        Materia guardada = materiaRepository.save(materia);
        return convertirADTO(guardada);
    }

    @Override
    @CachePut(value = "materia", key = "#id")
    @CacheEvict(value = "materias", allEntries = true)
    public MateriaDTO actualizarMateria(Long id, MateriaDTO dto) {
        Materia materiaExistente = materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Materia no encontrada"));

        materiaExistente.setNombreMateria(dto.getNombreMateria());
        materiaExistente.setCodigoUnico(dto.getCodigoUnico());
        materiaExistente.setCreditos(dto.getCreditos());

        if (dto.getDocenteId() != null) {
            Docente docente = docenteRepository.findById(dto.getDocenteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Docente no encontrado"));
            materiaExistente.setDocente(docente);
        } else {
            materiaExistente.setDocente(null);
        }

        Materia actualizada = materiaRepository.save(materiaExistente);
        return convertirADTO(actualizada);
    }

    @Override
    @CacheEvict(value = {"materia", "materias"}, allEntries = true)
    public void eliminarMateria(Long id) {
        materiaRepository.deleteById(id);
    }

    // ======================
    // Métodos de conversión
    // ======================

    private MateriaDTO convertirADTO(Materia materia) {
        if (materia == null) return null;

        return MateriaDTO.builder()
                .id(materia.getId())
                .nombreMateria(materia.getNombreMateria())
                .codigoUnico(materia.getCodigoUnico())
                .creditos(materia.getCreditos())
                .docenteId(materia.getDocente() != null ? materia.getDocente().getId() : null)
                .prerequisitos(materia.getPrerequisitos() != null ?
                        materia.getPrerequisitos().stream().map(Materia::getId).collect(Collectors.toList()) : null)
                .esPrerequisitoDe(materia.getEsPrerequisitoDe() != null ?
                        materia.getEsPrerequisitoDe().stream().map(Materia::getId).collect(Collectors.toList()) : null)
                .build();
    }

    private Materia convertirAEntidad(MateriaDTO dto) {
        Materia materia = new Materia();
        materia.setId(dto.getId());
        materia.setNombreMateria(dto.getNombreMateria());
        materia.setCodigoUnico(dto.getCodigoUnico());
        materia.setCreditos(dto.getCreditos());

        if (dto.getDocenteId() != null) {
            Docente docente = docenteRepository.findById(dto.getDocenteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Docente no encontrado"));
            materia.setDocente(docente);
        }

        // Opcional: manejo de prerequisitos si fuera necesario.

        return materia;
    }
}
