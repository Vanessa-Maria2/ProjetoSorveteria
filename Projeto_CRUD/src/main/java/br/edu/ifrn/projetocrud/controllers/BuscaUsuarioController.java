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


/**
 * Objetivos: Esta classe tem objetivo de buscar os dados do usuário, seja especifíco ou geral, e também
 * remover o usuário do sistema
 * 
 * 
 * @author Mírian Andryellen (mirianvital21@gmail.com) e Vanessa Maria (vanessa.silva5205@gmail.com)
 * @version 3ª versão do projeto
 *
 * Data de criação: 16/09/2021
 * ##################################
 * Última alteração: 
 * 
 * Analista: Vanessa
 * Data: 05/01/2022
 * Alteração: Documentação de código
 * 
 * #####################################
 * 
 * 
Essa é um classe controladora cuja função é controlar a exibição e execução das urls que foram requisitidas, dando assim uma
resposta para para quem a requisitou, de modo que envie a requisição correta.
 */
@Controller
//essa classe responde pela url "/usuarios"
@RequestMapping("/usuarios")
public class BuscaUsuarioController {
	
	//objeto do tipo UsuarioRepository que tem o objetivo de consultar informações no banco de dados da tabela Usuario.
	@Autowired
	UsuarioRepository usuarioRepository;
	
	/*dá acesso a página de busca de todos usuário cadastrados no sistema, ao consultar a requisição será retornado a página Html 
	de busca de usuários*/
	@GetMapping("/buscaUsuarios")
	public String buscaUsuarios() {
		return "usuario/buscaUsuarios";
	}
	
	/**
	 * Busca todos usuários cadastrados no sistema, apenas o ADMINISTRADOR tem acesso a essa funcionalidade
	 *  
	 * @param model Envia todos os usuários encontrados para a página buscaUsuario
	 * @return Retorna para a página de buscaUsuarios
	 */
	@GetMapping("/buscarUsuarios")
	public String buscarTodosUsuarios(ModelMap model )  {
		
		//realiza uma busca no repositório de usuário, consultando todos os usuários encontrados e armazenando na lista usuariosEncontrados
		List<Usuario> usuariosEncontrados = usuarioRepository.findAll();
		
		//verificando se a lsita de usuariosEncontrados é diferente de nulo
		if(usuariosEncontrados != null) {
			//envia a lista de usuariosEncontrados para a página no qual vai ser apresentado através do atributo usuariosEncontrados
			model.addAttribute("usuariosEncontrados", usuariosEncontrados);
		}
		
		//responsável por retornar a página buscaUsuarios
		return "usuario/buscaUsuarios";
		
	}
	
	//dá acesso a página remover, ao consultar a requisição será retornado a página Html de remover usuário
	@GetMapping("/remove")
	public String entrarRemover() {
		return "/usuario/removerUsuario";
	}
	
	//responsável pela remoção de usuário do sistema
	@Transactional
	@GetMapping("/remover")
	public String remover(HttpSession sessao,
			RedirectAttributes attr
			) {
		
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
		
		//realiza uma busca no repositório de usuário buscando pelo email logado e armazena essa busca no objeto u
		Optional<Usuario> u = usuarioRepository.findByEmail(email);
		
		//por meio do get pega as informações armazenadas no objeto u e através dele também consulta o id desse usuário
		Long idUsuario = u.get().getId();
		
		//verifica se o id do usuário é diferente de null
		if(idUsuario != null) {
			//se a verificação for correta, ele deleta o usuário do banco de dados pelo id
			usuarioRepository.deleteById(idUsuario);
			//mensagem de feedback para mostrar ao usuário que a remoção deu certo
			attr.addFlashAttribute("msgSucesso", "Usuário removido com sucesso!");
		} else {
			//mensagem de feedback para mostrar ao usuário quea operação não deu certo
			attr.addFlashAttribute("msgErro", "Não foi possível remover esse usuário!");
		}

		//redireciona para a url de /login que tem a função de retornar a página de login
		return "redirect:/login";
	}
}
