package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.LoginDTO;
import br.com.fiap.techchallenge.dto.PasswordDTO;
import br.com.fiap.techchallenge.dto.UsuarioDTO;
import br.com.fiap.techchallenge.dto.UsuarioResponseDTO;
import br.com.fiap.techchallenge.model.Usuario;
import br.com.fiap.techchallenge.service.UsuarioService;
import br.com.fiap.techchallenge.util.PasswordHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( "/usuarios")
public class UsuarioController {

    private final UsuarioService svc;

    public UsuarioController(UsuarioService usuarioService) {
        this.svc = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listByNome(
            @RequestParam(name = "q", required = false, defaultValue = "")  String nome) {
        if (nome == null || nome.isEmpty()) {
            nome = "";
        }
        // Convert List<Usuario> to List<UsuarioResponseDTO>
        List<UsuarioResponseDTO> lista = svc.listarUsuarioPorNome(nome).stream()
                .map(UsuarioResponseDTO::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> get(@PathVariable("id") String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Usuario> usuario = svc.buscarUsuarioPorId(Long.parseLong(id));
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponseDTO(usuario.get()));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioDTO dto) {
        // Username obrigatorio
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Password obrigatorio
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Verifica se já existe um usuário com o mesmo username
        Optional<Usuario> existente = svc.buscarUsuarioPorUserame(dto.getUsername());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String novaSenha = PasswordHelper.cypher(dto.getPassword());
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(novaSenha);
        usuario.setEndereco(dto.getEndereco());
        Usuario novo = svc.criarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(novo));
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> update(@RequestBody UsuarioDTO dto) {
        if (dto.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Usuario> usuarioExistente = svc.buscarUsuarioPorId(dto.getId());
        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Usuario usuario = usuarioExistente.get();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setEndereco(dto.getEndereco());
        Usuario salvo = svc.atualizarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponseDTO(salvo));
    }

    @DeleteMapping
    public ResponseEntity<UsuarioResponseDTO> delete(@RequestBody UsuarioDTO dto) {
        if (dto.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Usuario> usuarioExistente = svc.buscarUsuarioPorId(dto.getId());
        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        svc.deletarUsuario(usuarioExistente.get().getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/auth")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody LoginDTO dto) {
        Optional<Usuario> usuario = svc.buscarUsuarioPorUserame(dto.getUsername());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Usuario existente = usuario.get();
        if (!PasswordHelper.check(dto.getPassword(), existente.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<UsuarioResponseDTO> changePassword(@RequestBody PasswordDTO dto) {
        Optional<Usuario> usuario = svc.buscarUsuarioPorUserame(dto.getUsername());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Usuario existente = usuario.get();
        if (!PasswordHelper.check(dto.getPassword(), existente.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String novaSenha = PasswordHelper.cypher(dto.getNewPassword());
        existente.setPassword(novaSenha);
        existente = svc.atualizarUsuario(existente);
        return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponseDTO(existente));
    }
}
