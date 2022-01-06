package br.edu.ifrn.projetocrud.controllers;


import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.projetocrud.dominio.Arquivo;
import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.repository.ArquivoRepository;
import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

/* Objetivos: Esta classe tem objetivo de salvar os dados do usuário cadastrado ou após a realização de edição de um perfil logado, 
 * além de busca os dados do usuário após realizar login
 * 
 * Autor: Mírian Andryellen (mirianvital21@gmail.com) e Vanessa Maria (vanessa.silva5205@gmail.com)
 * 
 * Data de criação: 16/09/2021
 * ##################################
 * Última alteração: 
 * 
 * Analista: Mírian
 * Data: 06/01/2022
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
public class CadastroUsuarioController {
	
	//objeto do tipo UsuarioRepository que tem o objetivo de consultar informações no banco de dados da tabela Usuario.
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//objeto do tipo ArquivoRepository que tem o objetivo de consultar informações no banco de dados da tabela Arquivo.
	@Autowired
	private ArquivoRepository arquivoRepository;
	
	/*dá acesso a página de cadastro de usuário, ao consultar a requisição será retornado a página Html de cadastro enviando um 
	 * objeto usuário através do model map*/
	@GetMapping("/cadastro")
	public String entrarCadastro(ModelMap model) {
		//envia uma variavel chamada pedido que correponde a um novo Pedido
		model.addAttribute("usuario", new Usuario());
		return "usuario/cadastro";
	}

	//busca as informações do usuário, assim que ele realizar o login ou cadastro os dados vão aparecer na página de Usuário
	@GetMapping("/paginaUsuario")
	public String paginaUsuario(@RequestParam (name="nome", required=false) String nome,
						@RequestParam (name="mostrarTodosDados", required=false)
						Boolean mostrarTodosDados, HttpSession sessao , ModelMap model ) {
		
			//Pegando o email do usuário logado
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName(); 
			
			//realiza uma busca no repositório de usuário buscando pelo email logado e armazena essa busca no objeto u
			Optional<Usuario> u = usuarioRepository.findByEmail(email);
			
			//pega as informações do objeto u através do método get e armazena no objeto usuario
			Usuario usuario = u.get();
		
			//verifica se objeto que recebeu as informações da busca realizada no repositório é diferente de nulo
			if(u != null) {
				//se sim, o objeto usuario é enviado para a página de usuário através do model map
				model.addAttribute("usuariosEncontrados", usuario);
			}
			
			//verificando se a requisição mostrar todos dados é diferente de nulo
			if(mostrarTodosDados != null) {
				model.addAttribute("mostrarTodosDados", true);
			}
			
			//retorna para a página de usuário
		return "paginaUsuario";
	}
	
	//dá acesso a página quemSomos, ao consultar a requisição será retornado a página Html quemSomos
	@GetMapping("/cadastro/quemSomos")
	public String entrarquemSomos() {
		return "usuario/quemSomos";
	}

	//responsável por salvar os dados do usuário após preenchidos ou após a realização de edição dos dados
	@PostMapping("/salvar")
	@Transactional(readOnly = false)
	public String salvar( @Valid Usuario usuario, BindingResult result, ModelMap model,
			@RequestParam("file") MultipartFile arquivo,
			RedirectAttributes attr,HttpSession sessao) {
		
		/*if(result.hasErrors()) {
			return "/usuario/cadastro";
		}*/
	
		//o método validarDados é chamado passando o objeto que foi recebido na requisição e armazenado os erros na lista msgValidacao
		List<String> msgValidacao = validarDados(usuario);
		
		//verifica se a lista de msgValidacao é diferente de nulo
		if(!msgValidacao.isEmpty()) {
			/*se isso for verdade significa que existem erros nos campos preenchidos de cadastro, logo os erros vão ser retornados
			para a página para o usuário ver e poder ajeitar conforme os critérios estabelecidos*/
			model.addAttribute("msgsErro", msgValidacao);
			return "/usuario/cadastro";
		}
		

		try {
			if(arquivo != null && !arquivo.isEmpty()) {
				//ajustes no arquivo foto, normalizando nome do arquivo
				String nomeArquivo =
						StringUtils.cleanPath(arquivo.getOriginalFilename());
				
				Arquivo arquivoBD =
						new Arquivo(null, nomeArquivo, 
								arquivo.getContentType(), arquivo.getBytes());
				
				//salvando a foto no banco de dados
				arquivoRepository.save(arquivoBD);
				
				if(usuario.getFoto() != null && usuario.getFoto().getId() != null
						&& usuario.getFoto().getId() > 0) {
					arquivoRepository.delete(usuario.getFoto());
				}
				
				//seta a foto no usuário recebido na requisição
				usuario.setFoto(arquivoBD);
				
		} else{
			//foto do usuário nula, ou seja, ele não informou foto
			usuario.setFoto(null);
		}
			
			//verifica se o id do usuário recebido na requisição é nulo, se isso for verdade significa que um novo usuário vai ser salvo
			if(usuario.getId() == null) {
				
				//Criptografando a senha
				String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
				
				//seta a senha criptografada em usuario
				usuario.setSenha(senhaCriptografada);
				
				//novo usuário
				usuarioRepository.save(usuario);
				//mensagem de feedback para mostrar ao usuário que o cadastro foi realizado com sucesso
				attr.addFlashAttribute("msgSucesso", "Cadastro realizado com sucesso! O seu id (identificador) é "+usuario.getId());
				
				//redireciona para a url /login que dá acesso a página de login
				return "redirect:/login";
			} else {
				//se o id não é igual a nulo isso significa que esse id já existe e se trata de uma edição
				
				//Criptografando a senha
				String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
				
				//seta a nova senha criptografada no usuario logado
				usuario.setSenha(senhaCriptografada);
				//salva o usuário 
				usuarioRepository.save(usuario);
				
				//mensagem de feedback para mostrar ao usuário que a edição foi realizada com sucesso
				attr.addFlashAttribute("msgSucesso", "Usuário editado com sucesso!");
				
				//redireciona para a url /usuarios/edicao que dá acesso a página de editar usuário
				return "redirect:/usuarios/edicao";
			}
		
		
	} catch(IOException e) {
		e.printStackTrace();
		
	}
		return null;
		
	}
	
	//método responsável por fazer a validação dos dados do usuário passsado como parâmetro
	private List<String> validarDados(Usuario usuario){
		//lista para armazenar os possíveis erros
		List<String> msgs= new ArrayList<>();
		
		//verifica os dados do objeto passado, se a condição for verdadeira uma mensagem vai ser salva na lista msgs
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
		
		//retorna a lista msgs
		 return msgs;
	}
	
}

