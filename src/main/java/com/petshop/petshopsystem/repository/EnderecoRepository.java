package com.petshop.petshopsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.petshopsystem.entity.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    boolean existsById(String id);
    void deleteById(String id);
    Endereco findById(String id);
    boolean existsByCliente(String Cliente);
    String getClienteById(String id);
    List<Endereco> findAllByCliente(String clienteId);
}
