package com.tecmilenio.saludimc.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="usuarios", uniqueConstraints=@UniqueConstraint(name="uk_usuario_nombre",columnNames="usuario"))
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @Column(name="nombre_completo",nullable=false,length=120) public String nombreCompleto;
    @Column(name="usuario",nullable=false,length=50) public String usuario;
    @Column(name="contrasena_hash",nullable=false,length=100) public String contrasenaHash;
    @Column(nullable=false) public Integer edad;
    @Column(nullable=false,length=20) public String sexo;
    @Column(nullable=false) public Double estatura;
    @Column(name="peso_inicial") public Double pesoInicial;
    @Column(name="fecha_registro",nullable=false) public LocalDateTime fechaRegistro=LocalDateTime.now();
}
