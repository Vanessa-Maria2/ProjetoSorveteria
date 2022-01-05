package br.edu.ifrn.projetocrud.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifrn.projetocrud.dominio.Pedido;
import br.edu.ifrn.projetocrud.repository.PedidoRepository;

@Controller
@RequestMapping("/pedidos")
public class EditarPedidoController {
	
	String x = "a";
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@GetMapping("/edicao")
	public String inicioEdicao(ModelMap model) {
			//Pegando o email do usuário logado
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName(); 
			
			//busca dos pedidos do usuário logado e coloca em uma lista de pedidos
			List<Pedido> pedidosEncontrados = pedidoRepository.findByPedidos(email);

			//manda a lista para página de editarPedido, ou seja, carrega todos os pedidos do usuário logado
			if(pedidosEncontrados != null && !pedidosEncontrados.isEmpty()) {
			model.addAttribute("pedidosEncontrados", pedidosEncontrados);
			} else {
				model.addAttribute("msgErro", "Você não possui pedidos cadastrados!");
			}
		return "editarPedido";
	}
	
	//responsável por editar o pedido de id passado pela url
	@Transactional
	@GetMapping("/editar/{id}")
	public String iniciarEdicao(
			@PathVariable(name="id", required = false) Long idPedido,
			ModelMap model,
			HttpSession sessao
			) {

		/*if(idPedido == null) {
			model.addAttribute("msgErro", "Digite um número inteiro");
			return "editarPedido";
		}*/
		
		//verifica se o id passado existe no banco de dados
		boolean existe = pedidoRepository.existsById(idPedido);
		
		//Se o pedido existir
		if(existe) {	
			/*as informações do id de pedido passado na url é mandado para a página de editarPedido com os dados no campo 
			do formulário para o usuário realizar a edição*/
			Pedido p = pedidoRepository.findById(idPedido).get();
			model.addAttribute("pedido", p);
			model.addAttribute("mostrarTodosDados", x); 
		} else {
			model.addAttribute("msgErro", "Esse pedido não existe!");
		}
		return "editarPedido";
	}

	
	@ModelAttribute("pedidos")
	public List<String> getTiposPedidos(){
		return Arrays.asList("Sorvete de Chocolate", "Sorvete de Morango", "Açaí", 
				"Milk Shake", "Sorvete Napolitano", "Sorvete Baunilha", "Sorvete Acerola");
	}
	
	@ModelAttribute("tiposDePagamento")
	public List<String> getTiposDePagamento(){
		return Arrays.asList("Crédito", "Débito", "Pix", 
				"Espécie");
	}
	
}
