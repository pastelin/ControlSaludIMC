package com.tecmilenio.saludimc.dto;

import jakarta.validation.constraints.*;

public final class Inputs {
    private Inputs(){}
    public record MedicionInput(@NotNull @DecimalMin(value="0.0",inclusive=false) Double peso,
                                 @NotNull @DecimalMin("1.0") @DecimalMax("2.5") Double estatura){}
    public record NombreInput(@NotBlank @Size(max=80) String nombre){}
    public record ArchivoInput(@NotBlank @Size(max=100) String nombre, @NotNull Long directorioId){}
    public record PerfilInput(@NotBlank @Size(max=120) String nombreCompleto,
                              @NotNull @Min(15) @Max(120) Integer edad,
                              @NotBlank String sexo,
                              @NotNull @DecimalMin("1.0") @DecimalMax("2.5") Double estatura){}
}
