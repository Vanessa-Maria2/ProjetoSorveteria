package br.edu.ifrn.projetocrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.projetocrud.dominio.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long>{

}
