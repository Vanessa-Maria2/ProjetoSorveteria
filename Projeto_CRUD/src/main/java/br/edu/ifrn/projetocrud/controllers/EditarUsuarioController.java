package br.edu.ifrn.projetocrud.controllers;

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

import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

/**
 * Objetivos: Esta classe tem objetivo de editar os dados do usuário logado
 * 
 * @author  Mírian Andryellen (mirianvital21@gmail.com) e Vanessa Maria (vanessa.silva5205@gmail.com)
 * @version 3ª versão do projeto
 * 
 * Data de criação: 16/09/2021
 * ##################################
 * Última alteração: 
 * 
 * Analista: Mírian
 * Data: 05/01/2022
 * Alteração: Documentação de código
 * 
 * #####################################
 * 
 * 
 * 
Essa é um classe controladora cuja função é controlar a exibição e execução das urls que foram requisitidas, dando assim uma
resposta para para quem a requisitou, de modo que envie a requisição correta.
 */
@Controller
//essa classe responde pela url "/usuarios"
@RequestMapping("/usuarios")
public class EditarUsuarioController {
	
	//a variável x do tipo String possui o valor de a
	String x = "a";
	
	//objeto do tipo UsuarioRepository que tem o objetivo de consultar informações no banco de dados da tabela Usuario.
	@Autowired
	UsuarioRepository usuarioRepository;
	
	//dá acesso a página de editar usuário que está logado, ao consultar a requisição será retornado a página Html de editar usuário
	@GetMapping("/edicao")
	public String inicioEdicao() {
		return "/usuario/editarUsuario";
	}
	
	/**
	 * Método responsável pela edição do usuário logado
	 * 
	 * @param model Responsável por enviar o objeto usuário logado para a página de editarPedido
	 * @return Retorna para a página de editarUsuario
	 */
	@Transactional
	@GetMapping("/editar")
	public String iniciarEdicao(
			ModelMap model) {
	
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
			
	
			//realiza uma busca no repositório de usuário buscando pelo email logado e armazena essa busca no objeto u
			Optional<Usuario> u = usuarioRepository.findByEmail(email);
			
			//pega as informações do objeto u através do método get e armazena no objeto usuario
			Usuario usuario = u.get();
			//Enviar os dados encontrados para a página de edição usando o modelMap
			model.addAttribute("usuario", u);
			/*o x serve para informar a página de editar usuário que mostrarTodosDados não é null, cuja finalidade é permitir que as informações 
			 * do usuário logado sejam exibidas*/
			model.addAttribute("mostrarTodosDados", x); 
		
		//retorna a página de editarUsuario que está dentro da pasta usuario
		return "/usuario/editarUsuario";
	}	
}
