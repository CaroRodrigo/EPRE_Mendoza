/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Epre.RRHH.servicios;

import com.Epre.RRHH.entidades.Estado;
import com.Epre.RRHH.entidades.Persona;
import com.Epre.RRHH.entidades.Usuario;
import com.Epre.RRHH.enums.Role;
import com.Epre.RRHH.excepciones.WebException;
import com.Epre.RRHH.repositorios.PersonaRepositorio;
import com.Epre.RRHH.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Rodrigo Caro
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
	private PersonaRepositorio personaRepositorio;
    

    @Transactional
    public Usuario save(Usuario usuario) throws WebException, IOException {

        String[] symbols = {"+", "=", "-", "*", "'"};

        if (usuario.getUsername().isEmpty() || usuario.getUsername() == null) {

            throw new WebException("El legajo no puede estar vacio");
        }

        Usuario user = findByUserName(usuario.getUsername());
        if (user != null) {
            throw new WebException("El numero de legado ya está registrado");
        }

        if (usuario.getPassword().isEmpty() || usuario.getPassword() == null) {

            throw new WebException("El password no puede estar vacio");
        }
        if (usuario.getPassword().length() < 6) {
            throw new WebException("La contraseña debe tener al menos 6 caracteres");
        }

        for (int i = 0; i < symbols.length; i++) {
            if (usuario.getPassword().contains(symbols[i])) {
                throw new WebException("La contraseña no debe contener símbolos");
            }
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setId(usuario.getId());
        usuario.setUsername(usuario.getUsername());
        usuario.setPassword(encoder.encode(usuario.getPassword()));

        usuario.setRol(Role.USER);


        return usuarioRepositorio.save(usuario);
    }

    public List<Usuario> listAll() {
        return usuarioRepositorio.findAll();
    }

    public List<Usuario> listAllByQ(String q) {
        // return usuarioRepositorio.findAll("%" + q + "%");
        return usuarioRepositorio.findAll();
    }

    public Usuario findByUserName(String username) {
        return usuarioRepositorio.findByUserName(username);
    }

    public Optional<Usuario> findById(String id) {
        return usuarioRepositorio.findById(id);
    }

    @Transactional
	public Persona cambioAPersona(Usuario usuario,String legajo,String apellido
                ,String nombre,Date fechaDeIngreso,Integer anios,Integer dias,Integer diasDisponibles, String detalles,Estado estado) throws IOException, WebException {

		Persona persona = new Persona();
		persona.setId(usuario.getId());
                persona.setUsername(usuario.getUsername());
                persona.setPassword(usuario.getPassword());
                persona.setLegajo(legajo);
                persona.setApellido(apellido);
		persona.setNombre(nombre);
		persona.setFechaDeIngreso(fechaDeIngreso);
		persona.setAnios(anios);
		persona.setDias(dias);
		persona.setDiasDisponibles(diasDisponibles);
                persona.setDetalles(detalles);
                persona.setEstado(estado);
		persona.setRol(Role.PERSONAL);
		deleteById(usuario.getId());
		return personaRepositorio.save(persona);

	}
    
    
    @Transactional
	public void deleteById(String id) throws WebException {
		Optional<Usuario> optional = usuarioRepositorio.findById(id);
		if (optional.isPresent()) {
			usuarioRepositorio.delete(optional.get());

		} else {
			throw new WebException("No se encontra la usuario seleccionada");
		}

	}
        public Usuario encontrarPorId(String id) {
		return usuarioRepositorio.encontrarPorId(id);
	}
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Usuario usuario = usuarioRepositorio.findByUserName(username);
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));
			if (usuario.getRol().equals(Role.ADMIN)) {
				authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				authorities.add(new SimpleGrantedAuthority("ROLE_PERSONAL"));
			}
			if (usuario.getRol().equals(Role.PERSONAL)) {
				authorities.add(new SimpleGrantedAuthority("ROLE_PERSONAL"));
			}
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuariosession", usuario);
			return new User(username, usuario.getPassword(), authorities);
		} catch (Exception e) {
			throw new UsernameNotFoundException("El usuario no existe");
		}
	}

}


