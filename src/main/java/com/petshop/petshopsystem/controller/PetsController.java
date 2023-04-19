package com.petshop.petshopsystem.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.petshop.petshopsystem.entity.Cliente;
import com.petshop.petshopsystem.entity.Pet;
import com.petshop.petshopsystem.entity.Usuario;
import com.petshop.petshopsystem.repository.ClientesRepository;
import com.petshop.petshopsystem.repository.PetRepository;
import com.petshop.petshopsystem.repository.RacaRepository;

import jakarta.transaction.Transactional;


@RestController
public class PetsController {
    
    private final PetRepository petRepository;
    private final ClientesRepository clientesRepository;
    private final RacaRepository racaRepository;

    @Autowired
    public PetsController(PetRepository petRepository, ClientesRepository clientesRepository, RacaRepository racaRepository) {
        this.petRepository = petRepository;
        this.clientesRepository = clientesRepository;
        this.racaRepository = racaRepository;
    }


    @GetMapping("/pets")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Pet> getPets() {
        List<Pet> enderecos = petRepository.findAll();
        return enderecos;
    }

    @GetMapping("/pets-usuario")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<List<Pet>> getPetsUsuario(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        String cpf = usuario.getCpf();
        Cliente cliente = clientesRepository.findByCpf(cpf);
        String clienteId = cliente.getId();
        List<Pet> pets = petRepository.findAllByCliente(clienteId);
        if(pets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/pets/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Pet getPetById(@PathVariable String id) {
        if(petRepository.existsById(id)) {
            return petRepository.findById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/pets")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Pet> registrarPet(@RequestBody Pet pet) {
        String clienteId = pet.getCliente();
        String racaId = pet.getRaca();
        //Verifica se o cliente e a raça do pet existem
        if (clientesRepository.existsById(clienteId) && racaRepository.existsById(racaId)) {
            petRepository.save(pet);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/pets/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Pet updatePet(@PathVariable String id, @RequestBody Pet pet) {
        //Verifica se o pet já existe no banco de dados
        if (!petRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // Define id e clienteId, não sendo necessário informá-los no body da request
        // Raça ainda deve ser informada, visto que pode necessitar alguma alteração
        pet.setId(id);
        Pet petAntigo = petRepository.findById(id);
        String clienteId = petAntigo.getCliente();
        pet.setCliente(clienteId);
        Pet petAtualizado = (Pet) petRepository.save(pet);
        return petAtualizado;
    }

    @PutMapping("/pets-usuario/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public Pet updatePetUsuario(@PathVariable String id, @RequestBody Pet pet, Authentication authentication) {
        //Verifica se o pet já existe no banco de dados
        if (!petRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // Define id e clienteId, não sendo necessário informá-los no body da request
        // Raça ainda deve ser informada, visto que pode necessitar alguma alteração
        Pet petAntigo = petRepository.findById(id);
        String clienteId = petAntigo.getCliente();

        authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        String cpf = usuario.getCpf();
        Cliente clienteAuth = clientesRepository.findByCpf(cpf);
        String clienteAuthId = clienteAuth.getId();

        // Verificação se o pet pertence ao usuário
        if(!clienteAuthId.equalsIgnoreCase(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        pet.setId(id);
        pet.setCliente(clienteId);
        Pet petAtualizado = (Pet) petRepository.save(pet);
        return petAtualizado;
    }

    @Transactional
    @DeleteMapping("/pets/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    //Verifica se o pet já existe no banco de dados
    public ResponseEntity<Void> deletarPet(@PathVariable String id) {
        if(petRepository.existsById(id)) {
            petRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
