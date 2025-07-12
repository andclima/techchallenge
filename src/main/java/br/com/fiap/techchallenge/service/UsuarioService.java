package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.model.Usuario;
import br.com.fiap.techchallenge.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
     private final UsuarioRepository usuarioRepository;

     public UsuarioService(UsuarioRepository usuarioRepository) {
         this.usuarioRepository = usuarioRepository;
     }

     public Usuario criarUsuario(Usuario usuario) {
         return usuarioRepository.save(usuario);
     }

     public Usuario atualizarUsuario(Usuario usuario) {
         return usuarioRepository.save(usuario);
     }

     public void deletarUsuario(Long id) {
         usuarioRepository.deleteById(id);
     }

     public Optional<Usuario> buscarUsuarioPorId(Long id) {
         return usuarioRepository.findById(id);
     }

     public Optional<Usuario> buscarUsuarioPorEmail(String email) {
         return usuarioRepository.findByEmail(email);
     }

     public Optional<Usuario> buscarUsuarioPorUserame(String username) {
         return usuarioRepository.findByUsername(username);
     }

     public List<Usuario> listarUsuarioPorNome(String nome) {
         return usuarioRepository.findAllByNomeContainingIgnoreCase(nome);
     }
}
