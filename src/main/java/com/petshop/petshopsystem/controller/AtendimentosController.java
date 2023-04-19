package com.petshop.petshopsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.petshop.petshopsystem.entity.Atendimento;
import com.petshop.petshopsystem.entity.Cliente;
import com.petshop.petshopsystem.entity.Pet;
import com.petshop.petshopsystem.entity.Usuario;
import com.petshop.petshopsystem.repository.AtendimentoRepository;
import com.petshop.petshopsystem.repository.ClientesRepository;
import com.petshop.petshopsystem.repository.PetRepository;

import jakarta.transaction.Transactional;


@RestController
public class AtendimentosController {
    
    private final AtendimentoRepository atendimentoRepository;
    private final ClientesRepository clientesRepository;
    private final PetRepository petRepository;

    @Autowired
    public AtendimentosController(AtendimentoRepository atendimentoRepository, ClientesRepository clientesRepository, PetRepository petRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.clientesRepository = clientesRepository;
        this.petRepository = petRepository;
    }


    @GetMapping("/atendimentos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Atendimento> getAtendimentos() {
        List<Atendimento> enderecos = atendimentoRepository.findAll();
        return enderecos;
    }

    @GetMapping("/atendimentos-usuario")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<List<Atendimento>> getAtendimentosUsuario(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        String cpf = usuario.getCpf();
        Cliente cliente = clientesRepository.findByCpf(cpf);
        String clienteId = cliente.getId();
        List<Pet> pets = petRepository.findAllByCliente(clienteId);
        List<String> petsIdList = new ArrayList<>();
        for (Pet pet : pets) {
            String petId = pet.getId();
            petsIdList.add(petId);
        }
        List<Atendimento> atendimentos = atendimentoRepository.findAllByPetIn(petsIdList);
        if(atendimentos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atendimentos);
    }

    @GetMapping("/atendimentos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Atendimento getAtendimentoById(@PathVariable String id) {
        if(atendimentoRepository.existsById(id)) {
            return atendimentoRepository.findById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/atendimentos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Atendimento> registrarAtendimento(@RequestBody Atendimento atendimento) {
        String petId = atendimento.getPet();
        //Verifica se o pet existe
        if (petRepository.existsById(petId)) {
            atendimentoRepository.save(atendimento);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atendimentos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Atendimento updateAtendimento(@PathVariable String id, @RequestBody Atendimento atendimento) {
        //Verifica se o atendimento já existe no banco de dados
        if (!atendimentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // Define id e petId, não sendo necessário informá-los no body da request
        atendimento.setId(id);
        Atendimento atendimentoAntigo = atendimentoRepository.findById(id);
        String petId = atendimentoAntigo.getPet();
        atendimento.setPet(petId);
        Atendimento atendimentoAtualizado = (Atendimento) atendimentoRepository.save(atendimento);
        return atendimentoAtualizado;
    }

    @Transactional
    @DeleteMapping("/atendimentos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    //Verifica se o pet já existe no banco de dados
    public ResponseEntity<Void> deletarAtendimento(@PathVariable String id) {
        if(atendimentoRepository.existsById(id)) {
            atendimentoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
