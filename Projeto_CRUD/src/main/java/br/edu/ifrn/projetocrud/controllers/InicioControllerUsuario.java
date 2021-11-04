package br.edu.ifrn.projetocrud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioControllerUsuario {
	
	@GetMapping("/")
	public String inicio() {
		return "inicio";
	}

}
