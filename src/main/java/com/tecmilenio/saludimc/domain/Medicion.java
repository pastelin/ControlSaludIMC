package com.tecmilenio.saludimc.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="imc")
public class Medicion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_usuario",nullable=false) public Usuario usuario;
    @Column(nullable=false) public Double peso;
    @Column(nullable=false) public Double estatura;
    @Column(nullable=false) public Double valorImc;
    @Column(nullable=false,length=30) public String estado;
    @Column(nullable=false) public LocalDateTime fecha=LocalDateTime.now();
}
