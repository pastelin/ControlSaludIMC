package com.tecmilenio.saludimc.domain;

import jakarta.persistence.*;

@Entity @Table(name="directories")
public class Directorio {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_usuario",nullable=false) public Usuario usuario;
    @Column(nullable=false,length=80) public String nombre;
}
