package br.edu.ifrn.projetocrud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.projetocrud.dominio.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

	@Query("select p from Pedido p where p.usuario.email = :email")
	List<Pedido> findByPedidos(@Param("email") String email);
}
