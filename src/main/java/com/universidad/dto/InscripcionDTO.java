package com.universidad.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDTO {
    private Long id;

    @NotBlank(message = "El ID estudiante es obligatorio")
    private Long estudianteId;

    @NotBlank(message = "EL ID materia es obligatorio")
    private Long materiaId;

    @PastOrPresent(message = "La fecha de alta debe ser anterior o igual a la fecha actual")
    private LocalDate fechaInscripcion;

    // Getters y setters o Builder
}
