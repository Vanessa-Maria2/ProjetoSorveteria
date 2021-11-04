package br.edu.ifrn.projetocrud.controllers;


import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class CadastroUsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/cadastro")
	public String entrarCadastro(ModelMap model) {
		model.addAttribute("usuario", new Usuario());
		return "usuario/cadastro";
	}

	@GetMapping("/cadastro/quemSomos")
	public String entrarquemSomos() {
		return "usuario/quemSomos";
	}

	@Transactional
	@PostMapping("/salvar")
	public String salvar( @Valid Usuario usuario, BindingResult result,ModelMap model,RedirectAttributes attr,  HttpSession sessao) {
		if(result.hasErrors()) {
			return "/usuario/cadastro";
		}
		List<String> msgValidacao = validarDados(usuario);
		if(!msgValidacao.isEmpty()) {
			model.addAttribute("msgsErro", msgValidacao);
			return "/usuario/cadastro";
		}
				
		if(usuario.getId() == null) {
			//novo usuário
			//Já serve para cadastro e edição
			usuarioRepository.save(usuario);
			attr.addFlashAttribute("msgSucesso", "Cadastro realizado com sucesso! O seu id (identificador) é "+usuario.getId());
			return "redirect:/usuarios/cadastro";
		} else {
			//Edição
			//Já serve para cadastro e edição
			usuarioRepository.save(usuario);
			attr.addFlashAttribute("msgSucesso", "Usuário editado com sucesso!");
			return "redirect:/usuarios/edicao";
		}
	}
	
	private List<String> validarDados(Usuario usuario){
		List<String> msgs= new ArrayList<>();
		if(usuario.getNome()== null ||usuario.getNome().isEmpty()) {
			msgs.add("O campo nome é obrigatório.");
		}
		if(usuario.getEmail()== null ||usuario.getEmail().isEmpty()) {
			msgs.add("O campo email é obrigatório.");
		}
		if(usuario.getTel()== null ||usuario.getTel().isEmpty()) {
			msgs.add("O campo telefone é obrigatório.");
		}
		if(usuario.getSenha()== null ||usuario.getSenha().isEmpty()) {
			msgs.add("O campo senha é obrigatório.");
		}
		
		 return msgs;
	}
	
}

