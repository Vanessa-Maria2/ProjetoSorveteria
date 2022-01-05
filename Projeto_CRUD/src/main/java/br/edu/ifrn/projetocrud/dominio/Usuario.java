package br.edu.ifrn.projetocrud.dominio;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Usuario {
	
	public static final String ADMIN = "ADMIN";
	public static final String USUARIO_COMUM = "COMUM";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message= "o campo nome é obrigatório.")
	@Size(min=2, message= "Um nome deve ter pelo menos dois caracteres.")
	private String nome;
	
	@NotBlank(message= "o campo email é obrigatório.")
	private String email;
	
	@NotBlank(message= "o campo senha é obrigatório.")
	private String senha;

	@NotBlank(message= "o campo telefone é obrigatório.")
	private String tel;

	@Column(nullable = false)
	private String perfil = USUARIO_COMUM;
	
	@OneToMany(mappedBy = "usuario")
	private List<Pedido> pedidos;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Arquivo foto;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public List<Pedido> getPedidos() {
		return pedidos;
	}
	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	public Arquivo getFoto() {
		return foto;
	}
	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}
	
}
