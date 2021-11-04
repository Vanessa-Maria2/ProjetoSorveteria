package br.edu.ifrn.projetocrud.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public String inicioEdicao() {
		return "editarPedido";
	}
	
	@Transactional
	@GetMapping("/editar")
	public String iniciarEdicao(
			@RequestParam(name="id", required = false) Long idPedido,
			ModelMap model,
			HttpSession sessao
			) {

		if(idPedido == null) {
			model.addAttribute("msgErro", "Digite um número inteiro");
			return "editarPedido";
		}
		
		boolean existe = pedidoRepository.existsById(idPedido);
		
		//Se o pedido existir
		if(existe) {	
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
