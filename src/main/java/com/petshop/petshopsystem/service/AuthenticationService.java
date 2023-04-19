package com.petshop.petshopsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.petshop.petshopsystem.repository.UsuarioRepository;

@Service
public class AuthenticationService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) 
        throws UsernameNotFoundException {
            return usuarioRepository.findByCpf(username);
        }
    
}
