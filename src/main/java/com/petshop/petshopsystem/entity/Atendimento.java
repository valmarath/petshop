package com.petshop.petshopsystem.entity;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36) NOT NULL")    
    private String id;

    private String pet;
    private String descricao;
    private Float valor; 
    private LocalDate data_atendimento;

    public Atendimento() {

    }

    public Atendimento(String pet, String descricao, Float valor, LocalDate data_atendimento) {
        this.pet = pet;
        this.descricao = descricao;
        this.valor = valor;
        this.data_atendimento = data_atendimento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public LocalDate getData_atendimento() {
        return data_atendimento;
    }

    public void setData_atendimento(LocalDate data_atendimento) {
        this.data_atendimento = data_atendimento;
    }


    
}
