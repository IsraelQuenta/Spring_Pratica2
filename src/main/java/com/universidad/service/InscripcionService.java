package com.universidad.service;

import com.universidad.dto.InscripcionDTO;
import java.util.List;

public interface InscripcionService {
    List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId);
    InscripcionDTO crearInscripcion(InscripcionDTO inscripcion);
    void eliminarInscripcion(Long id);
    InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcion);
}
