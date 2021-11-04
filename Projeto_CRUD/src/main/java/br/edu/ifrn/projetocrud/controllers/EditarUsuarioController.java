package br.edu.ifrn.projetocrud.controllers;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class EditarUsuarioController {
	
	String x = "a";
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/edicao")
	public String inicioEdicao() {
		return "/usuario/editarUsuario";
	}
	
	@Transactional
	@GetMapping("/editar")
	public String iniciarEdicao(
			@RequestParam(name="id", required = false) Long idUsuario,
			ModelMap model,
			HttpSession sessao
			) {
		
		if(idUsuario == null) {
			model.addAttribute("msgErro", "Digite um número inteiro");
			return "/usuario/editarUsuario";
		}
		
		boolean existe = usuarioRepository.existsById(idUsuario);
		
		//Se o usuário existe
		if(existe) {	
			Usuario u = usuarioRepository.findById(idUsuario).get();
			model.addAttribute("usuario", u);
			model.addAttribute("mostrarTodosDados", x); 
		} else {
			model.addAttribute("msgErro", "Esse usuário não existe!");
		}
		return "/usuario/editarUsuario";
	}	
}
