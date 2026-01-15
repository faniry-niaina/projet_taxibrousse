package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lib", nullable = false)
    private Integer lib;

    @OneToMany(mappedBy = "status")
    private List<Reservation> reservations;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getLib() { return lib; }
    public void setLib(Integer lib) { this.lib = lib; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}