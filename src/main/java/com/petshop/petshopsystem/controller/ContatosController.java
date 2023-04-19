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
import com.petshop.petshopsystem.entity.Contato;
import com.petshop.petshopsystem.entity.Usuario;
import com.petshop.petshopsystem.repository.ClientesRepository;
import com.petshop.petshopsystem.repository.ContatoRepository;

import jakarta.transaction.Transactional;


@RestController
public class ContatosController {
    
    private final ContatoRepository contatoRepository;
    private final ClientesRepository clientesRepository;

    @Autowired
    public ContatosController(ContatoRepository contatoRepository, ClientesRepository clientesRepository) {
        this.contatoRepository = contatoRepository;
        this.clientesRepository = clientesRepository;
    }


    @GetMapping("/contatos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Contato> getContatos() {
        List<Contato> contatos = contatoRepository.findAll();
        return contatos;
    }

    @GetMapping("/contatos-usuario")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<List<Contato>> getContatosUsuario(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        String cpf = usuario.getCpf();
        Cliente cliente = clientesRepository.findByCpf(cpf);
        String clienteId = cliente.getId();
        List<Contato> contatos = contatoRepository.findAllByCliente(clienteId);
        if(contatos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/contatos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Contato getContatoById(@PathVariable String id) {
        if(contatoRepository.existsById(id)) {
            return contatoRepository.findById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/contatos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Contato> registrarContato(@RequestBody Contato contato) {
        String clienteId = contato.getCliente();
        //Verifica se o id do cliente já existe na tabela de contatos, para não duplicar
        if(contatoRepository.existsByCliente(clienteId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        //Verifica se o cliente existe na tabela de clientes
        } else if (clientesRepository.existsById(clienteId)) {
            contatoRepository.save(contato);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/contatos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Contato updateContato(@PathVariable String id, @RequestBody Contato contato) {
        //Verifica se o contato já existe no banco de dados
        if (!contatoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // Define id e clienteId, não sendo necessário informá-los no body da request
        contato.setId(id);
        Contato contatoAntigo = contatoRepository.findById(id);
        String clienteId = contatoAntigo.getCliente();
        contato.setCliente(clienteId);
        Contato ContatoAtualizado = (Contato) contatoRepository.save(contato);
        return ContatoAtualizado;
    }

    @PutMapping("/contatos-usuario/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public Contato updatePetUsuario(@PathVariable String id, @RequestBody Contato contato, Authentication authentication) {
        //Verifica se o contato já existe no banco de dados
        if (!contatoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // Define id e clienteId, não sendo necessário informá-los no body da request
        Contato contatoAntigo = contatoRepository.findById(id);
        String clienteId = contatoAntigo.getCliente();

        authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        String cpf = usuario.getCpf();
        Cliente clienteAuth = clientesRepository.findByCpf(cpf);
        String clienteAuthId = clienteAuth.getId();

        // Verificação se o contato pertence ao usuário
        if(!clienteAuthId.equalsIgnoreCase(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        contato.setId(id);
        contato.setCliente(clienteId);
        Contato contatoAtualizado = (Contato) contatoRepository.save(contato);
        return contatoAtualizado;
    }

    @Transactional
    @DeleteMapping("/contatos/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    //Verifica se o contato já existe no banco de dados
    public ResponseEntity<Void> deletarContato(@PathVariable String id) {
        if(contatoRepository.existsById(id)) {
            contatoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
