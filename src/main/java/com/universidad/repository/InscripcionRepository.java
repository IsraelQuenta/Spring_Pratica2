package com.universidad.repository;

import com.universidad.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByEstudianteId(Long estudianteId);
    List<Inscripcion> findByMateriaId(Long materiaId);

    boolean existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
}
