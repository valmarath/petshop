package com.petshop.petshopsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshop.petshopsystem.entity.Atendimento;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {
    boolean existsById(String id);
    void deleteById(String id);
    Atendimento findById(String id);
    List<Atendimento> findAllByPetIn(List<String> petsIdList);
}
