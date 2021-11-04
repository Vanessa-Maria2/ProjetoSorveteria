package br.edu.ifrn.projetocrud.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.projetocrud.dominio.Pedido;
import br.edu.ifrn.projetocrud.repository.PedidoRepository;

@Controller
@RequestMapping("/pedidos")
public class BuscaPedidoController {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@GetMapping("/busca")
	public String entrarBusca() {
		return "busca";
	}

	@GetMapping("/buscar")
	public String buscar(
			@RequestParam(name="id", required = false) Long id,
			HttpSession sessao,
			ModelMap model
			) {
	
		if(id == null) {
			model.addAttribute("msgErro", "Digite um número inteiro");
			return "busca";
		}
		
		boolean existe;
		
		if(id == 0) {
			existe = pedidoRepository.findAll().isEmpty();
			List<Pedido> pedidosEncontrados = pedidoRepository.findAll();
			
			if(!existe) {
				model.addAttribute("pedidosEncontrados", pedidosEncontrados);
				model.addAttribute("msgSucesso", "Pedidos buscados com sucesso!");
			}
			
		} else if(id != 0) {
		
		existe = pedidoRepository.existsById(id);
		if(existe) {
			Optional<Pedido> pedidosEncontrados = pedidoRepository.findById(id);
			model.addAttribute("pedidosEncontrados", pedidosEncontrados.get());
			model.addAttribute("msgSucesso", "Pedido buscado com sucesso!");
		
		}else {
			model.addAttribute("msgErro", "O pedido buscado não existe!");
			}
		}

		return "busca";
	}
	

	@GetMapping("/remove")
	public String entrarRemover() {
		return "remover";
	}
	
	@Transactional
	@GetMapping("/remover")
	public String remover(
			@RequestParam(name="id", required = false) Long idPedido,
			HttpSession sessao,
			RedirectAttributes attr
			) {

		if(idPedido == null) {
			attr.addFlashAttribute("msgErro", "Digite um número inteiro");
			return "redirect:/pedidos/remove";
		}
		boolean existe = pedidoRepository.existsById(idPedido);
		
		if(existe) {
			pedidoRepository.deleteById(idPedido);
			attr.addFlashAttribute("msgSucesso", "Pedido removido com sucesso!");
		} else {
			attr.addFlashAttribute("msgErro", "Não foi possível remover esse pedido!");
		}
		
		//nome da url de alguma página que será chamada
		return "redirect:/pedidos/remove";
	}
}
