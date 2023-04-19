
package com.petshop.petshopsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.petshop.petshopsystem.ValidaCPF;
import com.petshop.petshopsystem.dto.Cpf;
import com.petshop.petshopsystem.entity.Usuario;
import com.petshop.petshopsystem.service.TokenService;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public String login(@RequestBody Cpf cpf) {
        // Verifica se o CPF informado está em formato válido
        if (ValidaCPF.isCPF(cpf.cpf()) == true) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(cpf.cpf(),
                                                    cpf.password());
            Authentication authenticate = this.authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);

            var usuario = (Usuario) authenticate.getPrincipal();

            return tokenService.gerarToken(usuario);
        } else if (ValidaCPF.isCPF(cpf.cpf()) == false) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CPF informado não é válido!");
        }
        return null;

    }
}
