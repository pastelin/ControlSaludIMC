package com.tecmilenio.saludimc.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="notifications")
public class Notificacion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_usuario",nullable=false) public Usuario usuario;
    @Column(nullable=false,length=250) public String mensaje;
    @Column(nullable=false) public boolean leida=false;
    @Column(nullable=false) public LocalDateTime fecha=LocalDateTime.now();
}
