package com.petshop.petshopsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.petshopsystem.entity.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Integer> {
    boolean existsById(String id);
    void deleteById(String id);
    Contato findById(String id);
    boolean existsByCliente(String Cliente);
    List<Contato> findAllByCliente(String clienteId);
}
