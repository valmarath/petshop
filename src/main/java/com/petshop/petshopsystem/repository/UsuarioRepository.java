package com.petshop.petshopsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.petshop.petshopsystem.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    UserDetails findByCpf(String usuario);

    Optional<Usuario> findById(String id);

    boolean existsByCpf(String cpf);

    boolean existsById(String id);

    void deleteById(String id);

}
