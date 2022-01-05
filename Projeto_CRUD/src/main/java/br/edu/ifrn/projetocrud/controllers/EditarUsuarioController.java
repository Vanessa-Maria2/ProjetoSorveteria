package br.edu.ifrn.projetocrud.controllers;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
			ModelMap model, HttpSession sessao
			) {
		
		/*if(idUsuario == null) {
			model.addAttribute("msgErro", "Digite um número inteiro");
			return "/usuario/editarUsuario";
		}*/
		
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
			
		//Usar usuaroRepository para buscar os dados do usuário logado pelo email
		//Enviar os dados encontrados para a página de edição usando o modelMap
	
			Usuario u = usuarioRepository.findByEmail(email).get();
			model.addAttribute("usuario", u);
			model.addAttribute("mostrarTodosDados", x); 
		
		
		return "/usuario/editarUsuario";
	}	
}
