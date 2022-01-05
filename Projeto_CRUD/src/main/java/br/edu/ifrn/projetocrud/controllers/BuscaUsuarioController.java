package br.edu.ifrn.projetocrud.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class BuscaUsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/busca")
	public String entrarBusca() {
		return "usuario/busca";
	}
	
	@GetMapping("/buscaUsuarios")
	public String buscaUsuarios() {
		return "usuario/buscaUsuarios";
	}
	
	//busca os dados do usuário logado
	@GetMapping("/buscar")
	public String buscar(@RequestParam (name="nome", required=false) String nome,
			@RequestParam (name="mostrarTodosDados", required=false)
	Boolean mostrarTodosDados, HttpSession sessao , ModelMap model )  {
		
		//List<Usuario> usuariosEncontrados = usuarioRepository.findByNome(nome);
		
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
		
		Usuario u = usuarioRepository.findByEmail(email).get();
		
		if(u != null) {
			model.addAttribute("usuariosEncontrados", u);
		}
		
		if(mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}
		
		return "usuario/busca";
		
	}
	
	//busca todos usuários, apenas o ADMIN tem acesso
	@GetMapping("/buscarUsuarios")
	public String buscarTodosUsuarios(@RequestParam (name="nome", required=false) String nome,
			@RequestParam (name="mostrarTodosDados", required=false)
	Boolean mostrarTodosDados, HttpSession sessao , ModelMap model )  {
		
		//List<Usuario> usuariosEncontrados = usuarioRepository.findByNome(nome);
		
		List<Usuario> usuariosEncontrados = usuarioRepository.findAll();
		
		if(usuariosEncontrados != null) {
			model.addAttribute("usuariosEncontrados", usuariosEncontrados);
		}
		
		if(mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}
		
		return "usuario/buscaUsuarios";
		
	}
	
	@GetMapping("/remove")
	public String entrarRemover() {
		return "/usuario/removerUsuario";
	}
	
	@Transactional
	@GetMapping("/remover")
	public String remover(
			//@RequestParam(name="id", required = false) Long idUsuario,
			HttpSession sessao,
			RedirectAttributes attr
			) {
	
		/*if(idUsuario == null) {
			attr.addFlashAttribute("msgErro", "Digite um número inteiro");
			return "redirect:/usuarios/remove";
		}
		
		boolean existe = usuarioRepository.existsById(idUsuario);
		*/
		
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
				
		//usuarioRepository.deleteById(idUsuario);
		
		Optional<Usuario> u = usuarioRepository.findByEmail(email);
		
		Long idUsuario = u.get().getId();
		
		
		if(idUsuario != null) {
			usuarioRepository.deleteById(idUsuario);
			attr.addFlashAttribute("msgSucesso", "Usuário removido com sucesso!");
		} else {
			attr.addFlashAttribute("msgErro", "Não foi possível remover esse usuário!");
		}

		//nome da url de alguma página que será chamada
		return "redirect:/login";
	}
}
