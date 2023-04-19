package com.petshop.petshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.petshopsystem.entity.Raca;

@Repository
public interface RacaRepository extends JpaRepository<Raca, Integer> {
    boolean existsById(String id);
    void deleteById(String id);
    Raca findById(String id);
    boolean existsByDescricao(String descricao);
}
