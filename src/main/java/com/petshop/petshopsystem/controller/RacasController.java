package com.petshop.petshopsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.petshop.petshopsystem.entity.Raca;
import com.petshop.petshopsystem.repository.RacaRepository;

import jakarta.transaction.Transactional;


@RestController
public class RacasController {
    
    private final RacaRepository racaRepository;

    @Autowired
    public RacasController(RacaRepository racaRepository) {
        this.racaRepository = racaRepository;
    }


    @GetMapping("/racas")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public List<Raca> getRacas() {
        List<Raca> racas = racaRepository.findAll();
        return racas;
    }

    @GetMapping("/racas/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public Raca getRacaById(@PathVariable String id) {
        if(racaRepository.existsById(id)) {
            return racaRepository.findById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/racas")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Raca> registrarRaca(@RequestBody Raca raca) {
        String descricao = raca.getDescricao();
        //Verifica se o id do cliente já existe na tabela de raças, para não duplicar
        if(racaRepository.existsByDescricao(descricao)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        //Verifica se o cliente existe na tabela de clientes
        } else {
            racaRepository.save(raca);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PutMapping("/racas/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Raca updateRaca(@PathVariable String id, @RequestBody Raca raca) {
        //Verifica se a raça já existe no banco de dados
        if (!racaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // Define id da raça
        raca.setId(id);
        Raca racaAtualizada = (Raca) racaRepository.save(raca);
        return racaAtualizada;
    }

    @Transactional
    @DeleteMapping("/racas/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    //Verifica se a raça já existe no banco de dados
    public ResponseEntity<Void> deletarRaca(@PathVariable String id) {
        if(racaRepository.existsById(id)) {
            racaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
