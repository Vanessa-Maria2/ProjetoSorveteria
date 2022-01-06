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

/** Objetivos: Esta classe tem objetivo de editar os pedidos do usuário logado
 * 
 * @author Mírian Andryellen (mirianvital21@gmail.com) e Vanessa Maria (vanessa.silva5205@gmail.com)
 * @version 3ª versão do projeto
 * Data de criação: 16/09/2021
 * ##################################
 * Última alteração: 
 * 
 * Analista: Vanessa
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
//essa classe responde pela url "/pedidos"
@RequestMapping("/pedidos")
public class EditarPedidoController {
	
	//a variável x do tipo String possui o valor de a
	String x = "a";
	
	//objeto do tipo PedidoRepository que tem o objetivo de consultar informações no banco de dados da tabela Pedido.
	@Autowired
	PedidoRepository pedidoRepository;
	
	/**
	 * Esse método da acesso a página de editar pedidos
	 * 
	 * @param model Responsável por enviar todos os pedidos encontrados do usuário logado para a página de editarPedido
	 * @return Retorna a página de Editar Pedido
	 */
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
				//mensagem de feedback para informar ao usuário caso ele não tenha nenhum pedido cadastrado no banco de dados
				model.addAttribute("msgErro", "Você não possui pedidos cadastrados!");
			}
		return "editarPedido";
	}
	
	/**
	 * Responsável por editar o pedido de id passado pela url
	 * 
	 * @param idPedido Armazena o id passado pela url
	 * @param model Retorna o objeto do tipo pedido de id passado pela url para a página editarPedido
	 * @return Retorna para a página de editarPedido
	 */
	@Transactional
	@GetMapping("/editar/{id}")
	public String iniciarEdicao(
			@PathVariable(name="id", required = false) Long idPedido,
			ModelMap model
			) {

		//verifica se o id passado na url existe no banco de dados
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

	//método responsável por atribuir ao atributo pedidos uma lista com os tipos de pedidos
	@ModelAttribute("pedidos")
	public List<String> getTiposPedidos(){
		return Arrays.asList("Sorvete de Chocolate", "Sorvete de Morango", "Açaí", 
				"Milk Shake", "Sorvete Napolitano", "Sorvete Baunilha", "Sorvete Acerola");
	}
	
	//método responsável por atribuir ao atributo tiposDePagamento uma lista com os tipos de pagamentos
	@ModelAttribute("tiposDePagamento")
	public List<String> getTiposDePagamento(){
		return Arrays.asList("Crédito", "Débito", "Pix", 
				"Espécie");
	}
	
}
