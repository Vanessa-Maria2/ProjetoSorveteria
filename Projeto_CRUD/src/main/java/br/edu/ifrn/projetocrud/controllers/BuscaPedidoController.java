package br.edu.ifrn.projetocrud.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.projetocrud.dominio.Pedido;
import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.repository.PedidoRepository;
import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

@Controller
@RequestMapping("/pedidos")
public class BuscaPedidoController {
	
	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/busca")
	public String entrarBusca() {
		return "busca";
	}


	@GetMapping("/buscaPedidos")
	public String buscaPedidos() {
		return "buscaPedidos";
	}

	//busca os pedidos do usuário logado, ou seja, um usuário específico
	@GetMapping("/buscar")
	public String buscar(
			@RequestParam(name="id", required = false) Long id,
			HttpSession sessao,
			ModelMap model
			) {
	
		/*if(id == null) {
			model.addAttribute("msgErro", "Digite um número inteiro");
			return "busca";
		}*/
		
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
		
		//busca de pedidos no banco de dados pelos pedidos do usuário logado
		List<Pedido> pedidosEncontrados = pedidoRepository.findByPedidos(email);

		if(pedidosEncontrados != null && !pedidosEncontrados.isEmpty()) {
			model.addAttribute("pedidosEncontrados", pedidosEncontrados);
			model.addAttribute("msgSucesso", "Pedidos buscados com sucesso!");
		} else {
			model.addAttribute("msgErro", "Você não possui pedidos cadastrados!");
		}
		return "busca";
	}
	
	//método responsável por buscar todos os pedidos, apenas o ADMIN tem acesso
	@GetMapping("/buscarPedidos")
	public String buscarTodosPedidos(
			@RequestParam(name="id", required = false) Long id,
			HttpSession sessao,
			ModelMap model
			) {
	
		/*if(id == null) {
			model.addAttribute("msgErro", "Digite um número inteiro");
			return "busca";
		}*/
		
		
		List<Pedido> pedidosEncontrados = pedidoRepository.findAll();

		if(pedidosEncontrados != null && !pedidosEncontrados.isEmpty()) {
			model.addAttribute("pedidosEncontrados", pedidosEncontrados);
			model.addAttribute("msgSucesso", "Pedidos buscados com sucesso!");
		} else {
			model.addAttribute("msgErro", "Não existem pedidos cadastrados!");
		}
		return "buscaPedidos";
	}
	

	@GetMapping("/remove")
	public String entrarRemover(ModelMap model) {
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
				
		//busca dos pedidos do usuário logado para mandar para a página Remover
		List<Pedido> pedidosEncontrados = pedidoRepository.findByPedidos(email);

		if(pedidosEncontrados != null && !pedidosEncontrados.isEmpty()) {
		model.addAttribute("pedidosEncontrados", pedidosEncontrados);
		} else {
			model.addAttribute("msgErro", "Você não possui pedidos cadastrados!");
		}
		return "remover";
	}
	
	@Transactional
	@GetMapping("/remover/{id}")
	public String remover(
			@PathVariable(name="id", required = false) Long idP,
			HttpSession sessao,
			RedirectAttributes attr
			) {
		
		//remove o pedido de id passado na url específicado ao clicar no link remover da página Remover
		pedidoRepository.deleteById(idP);
		
		attr.addFlashAttribute("msgSucesso", "Pedido removido com sucesso!");
	
		return "redirect:/pedidos/remove";
	}
	
}
