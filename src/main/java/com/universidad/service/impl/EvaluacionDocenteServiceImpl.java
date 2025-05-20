package com.universidad.service.impl;

import com.universidad.model.EvaluacionDocente;
import com.universidad.model.Docente;
import com.universidad.repository.EvaluacionDocenteRepository;
import com.universidad.repository.DocenteRepository;
import com.universidad.service.IEvaluacionDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluacionDocenteServiceImpl implements IEvaluacionDocenteService {
    @Autowired
    private EvaluacionDocenteRepository evaluacionDocenteRepository;
    @Autowired
    private DocenteRepository docenteRepository;

    @Override
    @CachePut(value = "evaluacionDocente", key = "#result.id")
    @CacheEvict(value = "evaluacionesDocente", allEntries = true)
    public EvaluacionDocente crearEvaluacion(EvaluacionDocente evaluacion) {
        return evaluacionDocenteRepository.save(evaluacion);
    }

    @Override
    @Cacheable(value = "evaluacionesDocente", key = "#docenteId")
    public List<EvaluacionDocente> obtenerEvaluacionesPorDocente(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId).orElse(null);
        if (docente == null) return java.util.Collections.emptyList();
        return evaluacionDocenteRepository.findByDocente(docente);
    }

    @Override
    @Cacheable(value = "evaluacionDocente", key = "#id")
    public EvaluacionDocente obtenerEvaluacionPorId(Long id) {
        return evaluacionDocenteRepository.findById(id).orElse(null);
    }

    @Override
    @CacheEvict(value = "evaluacionDocente", key = "#id")
    public void eliminarEvaluacion(Long id) {
        evaluacionDocenteRepository.deleteById(id);
    }
}
