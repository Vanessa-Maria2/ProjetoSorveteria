package br.edu.ifrn.projetocrud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.projetocrud.dominio.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	@Query("select u from Usuario u where u.nome like %:nome% ")
	List<Usuario> findByNome(@Param("nome") String nome);
}
