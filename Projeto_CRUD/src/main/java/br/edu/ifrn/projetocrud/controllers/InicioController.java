package br.edu.ifrn.projetocrud.controllers;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.projetocrud.dominio.Pedido;
import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.repository.PedidoRepository;
import br.edu.ifrn.projetocrud.repository.UsuarioRepository;

/* Objetivos: Esta classe tem objetivo de salvar os dados de pedido cadastrado ou após a realização de edição de pedidos de um
 * usuário logado, além de busca os dados do usuário após realizar login
 * 
 * Autor: Mírian Andryellen (mirianvital21@gmail.com) e Vanessa Maria (vanessa.silva5205@gmail.com)
 * 
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
public class InicioController {
	
	//objeto do tipo PedidoRepository que tem o objetivo de consultar informações no banco de dados da tabela Pedido.
	@Autowired
	PedidoRepository pedidoRepository;
	
	//objeto do tipo UsuarioRepository que tem o objetivo de consultar informações no banco de dados da tabela Usuario.
	@Autowired
	UsuarioRepository usuarioRepository;
	
	/*dá acesso a página de inicio que corresponde a página de cadastro de pedidos, ao consultar a requisição será retornado a página Html 
	 * inicio enviando um objeto pedido através do model map*/
	@GetMapping("/")
	public String inicio(ModelMap model) {
		//envia uma variavel chamada pedido que correponde a um novo Pedido
		model.addAttribute("pedido", new Pedido());
		return "inicio";
	}
	
	//dá acesso a página quemSomos, ao consultar a requisição será retornado a página Html quemSomos
	@GetMapping("/quemSomos")
	public String entrarquemSomos() {
		return "quemSomos";
	}
	
	//método responsável por fazer a validação dos dados do pedido passsado como parâmetro
	private List<String> validarDados(Pedido pedido){
		
		//lista para armazenar os possíveis erros
		List<String> msgs = new ArrayList<>();
		
		//verifica os dados do objeto passado, se a condição for verdadeira uma mensagem vai ser salva na lista msgs
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

		//retorna a lista msgs
		return msgs;
	}
	
	//responsável por salvar os dados do pedido após preenchidos ou após a realização de edição dos dados
	@Transactional
	@PostMapping("/salvar")
	public String salvar(Pedido pedido, ModelMap model,
			RedirectAttributes attr, HttpSession sessao) {
		
		//o método validarDados é chamado passando o objeto que foi recebido na requisição e armazenado os erros na lista msgValidacao
		List<String> msgValidacao = validarDados(pedido);
		
		//Pegando o email do usuário logado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); 
		
		//Usa o usuaroRepository para buscar os dados do usuário logado pelo email e armazena essa informação no objeto u
		Usuario u = usuarioRepository.findByEmail(email).get();
		
		//relaciona o pedido cadastrado ao usuário que está logado
		pedido.setUsuario(u);
		
		//se a lista não for vazia, isso significa que existem erros nos campos preenchidos do objeto pedido
		if(!msgValidacao.isEmpty()) {
			//retorna para a página inicio com a lista de erros
			model.addAttribute("msgsErro", msgValidacao);
			return "inicio";
		}
		
		//verifica se o id do pedido recebido na requisição é nulo, se isso for verdade significa que um novo pedido vai ser salvo
		if(pedido.getId() == null) {
			//novo pedido
			//salva o pedido no banco de dados
			pedidoRepository.save(pedido);
			attr.addFlashAttribute("msgSucesso", "Pedido realizado com sucesso! O id (identificador) do seu pedido é "+pedido.getId());
			
			//redireciona para a url /pedidos/ que dá acesso a página de inicio (cadastro de pedidos)
			return "redirect:/pedidos/";
		} else {
			//se o id não é igual a nulo isso significa que esse id já existe e se trata de uma edição
			
			//salva o pedido no banco de dados
			pedidoRepository.save(pedido);
			
			//mensagem de feedback para mostrar ao usuário que o pedido foi editado com sucesso
			attr.addFlashAttribute("msgSucesso", "Pedido editado com sucesso!");
			
			//redireciona para a url /pedidos/edicao que dá acesso a página de editar pedido 
			return "redirect:/pedidos/edicao";
		}
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
