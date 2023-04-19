package com.petshop.petshopsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.petshopsystem.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    boolean existsById(String id);
    void deleteById(String id);
    Pet findById(String id);
    boolean existsByCliente(String Cliente);
    String getClienteById(String id);
    List<Pet> findAllByCliente(String clienteId);
}
