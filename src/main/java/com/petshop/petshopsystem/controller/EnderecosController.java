package com.petshop.petshopsystem.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

import com.petshop.petshopsystem.entity.Cliente;
import com.petshop.petshopsystem.entity.Endereco;
import com.petshop.petshopsystem.entity.Usuario;
import com.petshop.petshopsystem.repository.ClientesRepository;
import com.petshop.petshopsystem.repository.EnderecoRepository;

import jakarta.transaction.Transactional;


@RestController
public class EnderecosController {
    
    private final EnderecoRepository enderecoRepository;
    private final ClientesRepository clientesRepository;

    @Autowired
    public EnderecosController(EnderecoRepository enderecoRepository, ClientesRepository clientesRepository) {
        this.enderecoRepository = enderecoRepository;
        this.clientesRepository = clientesRepository;
    }


    @GetMapping("/enderecos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Endereco> getEnderecos() {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos;
    }

    @GetMapping("/enderecos-usuario")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<List<Endereco>> getEnderecosUsuario(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        String cpf = usuario.getCpf();
        Cliente cliente = clientesRepository.findByCpf(cpf);
        String clienteId = cliente.getId();
        List<Endereco> enderecos = enderecoRepository.findAllByCliente(clienteId);
        if(enderecos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/enderecos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Endereco getEnderecoById(@PathVariable String id) {
        if(enderecoRepository.existsById(id)) {
            return enderecoRepository.findById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/enderecos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Endereco> registrarEndereco(@RequestBody Endereco endereco) {
        String clienteId = endereco.getCliente();
        //Verifica se o id do cliente já existe na tabela de endereços, para não duplicar
        if(enderecoRepository.existsByCliente(clienteId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        //Verifica se o cliente existe na tabela de clientes
        } else if (clientesRepository.existsById(clienteId)) {
            enderecoRepository.save(endereco);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/enderecos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Endereco updateEndereco(@PathVariable String id, @RequestBody Endereco endereco) {
        //Verifica se o endereço já existe no banco de dados
        if (!enderecoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // Define id e clienteId, não sendo necessário informá-los no body da request
        endereco.setId(id);
        Endereco enderecoAntigo = enderecoRepository.findById(id);
        String clienteId = enderecoAntigo.getCliente();
        endereco.setCliente(clienteId);
        Endereco EnderecoAtualizado = (Endereco) enderecoRepository.save(endereco);
        return EnderecoAtualizado;
    }

    @PutMapping("/enderecos-usuario/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public Endereco updatePetUsuario(@PathVariable String id, @RequestBody Endereco endereco, Authentication authentication) {
        //Verifica se o pet já existe no banco de dados
        if (!enderecoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // Define id e clienteId, não sendo necessário informá-los na request
        Endereco contatoAntigo = enderecoRepository.findById(id);
        String clienteId = contatoAntigo.getCliente();

        authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        String cpf = usuario.getCpf();
        Cliente clienteAuth = clientesRepository.findByCpf(cpf);
        String clienteAuthId = clienteAuth.getId();

        // Verificação se o endereço pertence ao usuário
        if(!clienteAuthId.equalsIgnoreCase(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        endereco.setId(id);
        endereco.setCliente(clienteId);
        Endereco contatoAtualizado = (Endereco) enderecoRepository.save(endereco);
        return contatoAtualizado;
    }

    @Transactional
    @DeleteMapping("/enderecos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    //Verifica se o endereço já existe no banco de dados
    public ResponseEntity<Void> deletarEndereco(@PathVariable String id) {
        if(enderecoRepository.existsById(id)) {
            enderecoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
