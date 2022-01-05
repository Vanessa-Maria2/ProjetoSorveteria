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

@Controller
@RequestMapping("/usuarios")
public class CadastroUsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ArquivoRepository arquivoRepository;
	
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
				//List<Usuario> usuariosEncontrados = usuarioRepository.findByNome(nome);
				
			//Pegando o email do usuário logado
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName(); 
			
			Optional<Usuario> u = usuarioRepository.findByEmail(email);
			
			Usuario usuario = u.get();
		
			
			if(u != null) {
				model.addAttribute("usuariosEncontrados", usuario);
			}
			
			if(mostrarTodosDados != null) {
				model.addAttribute("mostrarTodosDados", true);
			}
			
		return "paginaUsuario";
	}
	
	@GetMapping("/cadastro/quemSomos")
	public String entrarquemSomos() {
		return "usuario/quemSomos";
	}

	@PostMapping("/salvar")
	@Transactional(readOnly = false)
	public String salvar( @Valid Usuario usuario, BindingResult result, ModelMap model,
			@RequestParam("file") MultipartFile arquivo,
			RedirectAttributes attr,HttpSession sessao) {
		
		/*if(result.hasErrors()) {
			return "/usuario/cadastro";
		}*/
	
		List<String> msgValidacao = validarDados(usuario);
		if(!msgValidacao.isEmpty()) {
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
				
				usuario.setFoto(arquivoBD);
				
		} else{
			usuario.setFoto(null);
		}
			
			
			if(usuario.getId() == null) {
				
				//Criptografando a senha
				String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
				
				//seta a senha criptografada em usuario
				usuario.setSenha(senhaCriptografada);
				
				//novo usuário
				usuarioRepository.save(usuario);
				attr.addFlashAttribute("msgSucesso", "Cadastro realizado com sucesso! O seu id (identificador) é "+usuario.getId());
				
				return "redirect:/login";
			} else {
				//Edição
				
				//Criptografando a senha
				String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
				
				usuario.setSenha(senhaCriptografada);
				usuarioRepository.save(usuario);
				attr.addFlashAttribute("msgSucesso", "Usuário editado com sucesso!");
				return "redirect:/usuarios/edicao";
			}
		
		
	} catch(IOException e) {
		e.printStackTrace();
		
	}
		return null;
		
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

