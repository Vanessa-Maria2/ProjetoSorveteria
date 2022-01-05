package br.edu.ifrn.projetocrud.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

@Controller
public class InicioControllerUsuario {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		model.addAttribute("msgErro", "Login ou senha incorretos. Tente novamente.");
		return "login";
	}
	
	
}
