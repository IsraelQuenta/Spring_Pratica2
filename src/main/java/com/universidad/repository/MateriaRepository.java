package com.universidad.repository;

import com.universidad.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Materia findByCodigoUnico(String codigoUnico);

    // Para verificar si existe un código único (creación)
    boolean existsByCodigoUnico(String codigoUnico);
    
    // Para verificar si existe otro código único al actualizar (excluyendo el ID actual)
    boolean existsByCodigoUnicoAndIdNot(String codigoUnico, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // Bloqueo pesimista para evitar condiciones de carrera
    Optional<Materia> findById(Long id);
}
