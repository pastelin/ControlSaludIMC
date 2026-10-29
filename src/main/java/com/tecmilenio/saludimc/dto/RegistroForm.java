package com.tecmilenio.saludimc.dto;

import jakarta.validation.constraints.*;

public class RegistroForm {
    @NotBlank @Size(max=120) public String nombreCompleto;
    @NotBlank @Size(min=3,max=50) @Pattern(regexp="[a-zA-Z0-9._-]+",message="Usa letras, números, punto, guion o guion bajo") public String usuario;
    @NotBlank @Size(min=8,max=72) public String contrasena;
    @NotNull @Min(15) @Max(120) public Integer edad;
    @NotBlank public String sexo;
    @NotNull @DecimalMin("1.0") @DecimalMax("2.5") public Double estatura;
    @DecimalMin(value="0.0",inclusive=false) public Double pesoInicial;
    public String getNombreCompleto(){return nombreCompleto;}
    public void setNombreCompleto(String x){nombreCompleto=x;}
    public String getUsuario(){return usuario;}
    public void setUsuario(String x){usuario=x;}
    public String getContrasena(){return contrasena;}
    public void setContrasena(String x){contrasena=x;}
    public Integer getEdad(){return edad;}
    public void setEdad(Integer x){edad=x;}
    public String getSexo(){return sexo;}
    public void setSexo(String x){sexo=x;}
    public Double getEstatura(){return estatura;}
    public void setEstatura(Double x){estatura=x;}
    public Double getPesoInicial(){return pesoInicial;}
    public void setPesoInicial(Double x){pesoInicial=x;}
}
