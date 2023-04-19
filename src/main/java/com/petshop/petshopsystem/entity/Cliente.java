package com.petshop.petshopsystem.entity;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36) NOT NULL")    
    private String id;
    
    private String nome;
    private String cpf;
    private LocalDate data_cadastro;

    public Cliente() {

    }

    public Cliente(String nome, String cpf, LocalDate data_cadastro) {
        this.nome = nome;
        this.cpf = cpf;        
        this.data_cadastro = data_cadastro;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public LocalDate getData_cadastro() {
        return data_cadastro;
    }
    public void setData_cadastro(LocalDate data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    
}
