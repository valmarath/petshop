package com.petshop.petshopsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.petshop.petshopsystem.ValidaCPF;
import com.petshop.petshopsystem.entity.Usuario;
import com.petshop.petshopsystem.repository.UsuarioRepository;

import jakarta.transaction.Transactional;


@RestController
public class UsuariosController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Usuario> getUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/usuarios/{id}")
    public Usuario getUsuariosById(@PathVariable String id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        // Verifica se o CPF informado está em formato válido
        if (ValidaCPF.isCPF(usuario.getCpf()) == false) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        //Verifica se o CPF já não está na tabela de usuarios
        if(usuarioRepository.existsByCpf(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //Criptografia da senha antes de salvar no banco de dados
        String passwordCriptografada = new BCryptPasswordEncoder().encode(usuario.getPassword());
        usuario.setPassword(passwordCriptografada);
        usuarioRepository.save(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Object> updateUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        // Verifica se o CPF informado está em formato válido
        if (ValidaCPF.isCPF(usuario.getCpf()) == false) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        //Verifica se o usuario já existe no banco de dados
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        usuario.setId(id);

        //Criptografia da senha antes de salvar no banco de dados
        String passwordCriptografada = new BCryptPasswordEncoder().encode(usuario.getPassword());
        usuario.setPassword(passwordCriptografada);
        Usuario UsuarioAtualizado = (Usuario) usuarioRepository.save(usuario);
        return ResponseEntity.ok(UsuarioAtualizado);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String id) {
        //Verifica se o usuario já existe no banco de dados
        if(usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
