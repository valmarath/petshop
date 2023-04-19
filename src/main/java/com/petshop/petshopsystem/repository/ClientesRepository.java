package com.petshop.petshopsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.petshopsystem.entity.Cliente;

@Repository
public interface ClientesRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByCpf(String cpf);
    boolean existsById(String id);
    void deleteById(String id);
    Optional<Cliente> findById(String id);
    Cliente findByCpf(String cpf);
}
