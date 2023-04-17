package br.com.alura.app.config.security;

import java.io.IOException;
import java.util.Optional;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.app.api.auth.TokenService;
import br.com.alura.app.api.usuario.model.Usuario;
import br.com.alura.app.api.usuario.repository.UsuarioRepository;

public class AuthenticationTokenFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	
	private UsuarioRepository usuarioRepository;
	
	public AuthenticationTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}


	private void autenticarUsuario(String token) {
		Long idUsuario = tokenService.getIdUsuario(token);
		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario.get(), null, usuario.get().getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String getToken(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String token = getToken(request);

		Boolean tokenValido = tokenService.isTokenValido(token);

		if (tokenValido) {
			autenticarUsuario(token);
		}

		filterChain.doFilter(request, response);

	}
}
