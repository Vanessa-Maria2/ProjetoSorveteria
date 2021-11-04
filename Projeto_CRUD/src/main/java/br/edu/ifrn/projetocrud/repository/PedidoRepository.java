package br.edu.ifrn.projetocrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.projetocrud.dominio.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}
