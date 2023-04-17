package br.com.alura.app.api.usuario.controller;

import br.com.alura.app.api.usuario.model.Perfil;
import br.com.alura.app.api.usuario.model.Usuario;
import br.com.alura.app.api.usuario.service.UsuarioService;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class UsuarioForm {
	
	@NotEmpty
	private String login;
	
	@NotEmpty
	private String senha;
	
	private List<PerfilForm> perfis;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<PerfilForm> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<PerfilForm> perfis) {
		this.perfis = perfis;
	}

	public Usuario toEntity(UsuarioService usuarioService) {
		
		List<Perfil> perfis = new ArrayList<>();
		this.perfis.forEach(t -> perfis.add(t.toEntity(usuarioService)));
		
		Usuario usuario = new Usuario(this.login, this.senha, perfis);

		return usuario;
	}

	@Override
	public String toString() {
		return "UsuarioForm [login=" + login + ", senha=" + senha + ", perfis=" + perfis + "]";
	}

}
