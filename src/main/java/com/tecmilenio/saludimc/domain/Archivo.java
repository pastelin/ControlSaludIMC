package com.tecmilenio.saludimc.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="files")
public class Archivo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_usuario",nullable=false) public Usuario usuario;
    @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="id_directorio",nullable=false) public Directorio directorio;
    @Column(nullable=false,length=100) public String nombre;
    @Lob @Column(nullable=false,columnDefinition="LONGTEXT") public String contenido;
    @Column(nullable=false) public LocalDateTime fecha=LocalDateTime.now();
}
