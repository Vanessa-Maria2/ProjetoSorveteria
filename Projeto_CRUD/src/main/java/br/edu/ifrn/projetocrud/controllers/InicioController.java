package br.edu.ifrn.projetocrud.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.projetocrud.dominio.Pedido;
import br.edu.ifrn.projetocrud.repository.PedidoRepository;

@Controller
@RequestMapping("/pedidos")
public class InicioController {
	
	int idRecebido = 0;
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@GetMapping("/")
	public String inicio(ModelMap model) {
		//envia uma variavel chamada pedido que correponde a um novo Pedido
		model.addAttribute("pedido", new Pedido());
		return "inicio";
	}
	

	@GetMapping("/quemSomos")
	public String entrarquemSomos() {
		return "quemSomos";
	}
	private List<String> validarDados(Pedido pedido){
		
		List<String> msgs = new ArrayList<>();
		
		if(pedido.getTipoPedido() == null || pedido.getTipoPedido().isEmpty()) {
			msgs.add("O campo pedido é obrigatório.");
		} 
		if(pedido.getTipoDePagamento() == null || pedido.getTipoDePagamento().isEmpty()) {
			msgs.add("O campo tipo de pagamento é obrigatório.");
		} 
		if(pedido.getQuantidade() <= 0) {
			msgs.add("O valor de quantidade deve ser maior que 0.");
		} 
		if(pedido.getEndereco() == null || pedido.getEndereco().isEmpty()) {
			msgs.add("O campo endereço é obrigatório.");
		} 

		return msgs;
	}
	
	@Transactional
	@PostMapping("/salvar")
	public String salvar(Pedido pedido, ModelMap model,
			RedirectAttributes attr, HttpSession sessao) {
		
		List<String> msgValidacao = validarDados(pedido);
		
		//se a lista não for vazia
		if(!msgValidacao.isEmpty()) {
			model.addAttribute("msgsErro", msgValidacao);
			return "inicio";
		}
		
		if(pedido.getId() == null) {
			//novo pedido
			//Já serve para cadastro e edição
			pedidoRepository.save(pedido);
			attr.addFlashAttribute("msgSucesso", "Pedido realizado com sucesso! O id (identificador) do seu pedido é "+pedido.getId());
			return "redirect:/pedidos/";
		} else {
			//Edição
			//Já serve para cadastro e edição
			pedidoRepository.save(pedido);
			attr.addFlashAttribute("msgSucesso", "Pedido editado com sucesso!");
			return "redirect:/pedidos/edicao";
		}
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
