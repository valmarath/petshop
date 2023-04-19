package com.petshop.petshopsystem.controller;

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

import com.petshop.petshopsystem.ValidaCPF;
import com.petshop.petshopsystem.entity.Cliente;
import com.petshop.petshopsystem.entity.Usuario;
import com.petshop.petshopsystem.repository.ClientesRepository;

import jakarta.transaction.Transactional;


@RestController
public class ClientesController {
    
    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping("/clientes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> getClientes() {
        List<Cliente> clientes = clientesRepository.findAll();
        return clientes;
    }

    @GetMapping("/clientes-usuario")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public Cliente getClientesUsuario(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String cpf = usuario.getCpf();
        if(!clientesRepository.existsByCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return clientesRepository.findByCpf(cpf);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clientes/{id}")
    public Cliente getClienteById(@PathVariable String id) {
        return clientesRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody Cliente cliente) {
        // Verifica se o CPF informado está em formato válido
        if (ValidaCPF.isCPF(cliente.getCpf()) == false) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        //Verifica se o CPF já não está na tabela de clientes
        if(clientesRepository.existsByCpf(cliente.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        clientesRepository.save(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable String id, @RequestBody Cliente cliente) {
        // Verifica se o CPF informado está em formato válido
        if (ValidaCPF.isCPF(cliente.getCpf()) == false) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        //Verifica se o cliente já existe no banco de dados
        if (!clientesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        cliente.setId(id);
        Cliente ClienteAtualizado = (Cliente) clientesRepository.save(cliente);
        return ResponseEntity.ok(ClienteAtualizado);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String id) {
        //Verifica se o cliente já existe no banco de dados
        if(clientesRepository.existsById(id)) {
            clientesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
