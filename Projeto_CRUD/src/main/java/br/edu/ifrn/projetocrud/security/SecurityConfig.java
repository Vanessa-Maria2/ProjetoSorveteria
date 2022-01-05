package br.edu.ifrn.projetocrud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.edu.ifrn.projetocrud.dominio.Usuario;
import br.edu.ifrn.projetocrud.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UsuarioService service;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.authorizeRequests().anyRequest().permitAll();
		http.authorizeRequests()
			.antMatchers("/css/**", "/imagens/**", "/js/**").permitAll()
			.antMatchers("/publico/**").permitAll()
			
			.antMatchers("/cadastro",
			"/usuarios/cadastro", "/paginaUsuario", "/usuarios/salvar").permitAll()
	
			.antMatchers("/usuarios/buscaUsuarios", "/usuarios/buscarUsuarios", "/pedidos/buscaPedidos",
					"/pedidos/buscarPedidos").hasAuthority(Usuario.ADMIN)
			
			.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/usuarios/paginaUsuario", true)
				.failureUrl("/login-error")
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.rememberMe();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
}
