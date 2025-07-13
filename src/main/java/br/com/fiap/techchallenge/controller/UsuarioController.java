package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.CreateUsuarioRequest;
import br.com.fiap.techchallenge.dto.PasswordRequest;
import br.com.fiap.techchallenge.dto.UpdateUsuarioRequest;
import br.com.fiap.techchallenge.dto.UsuarioResponse;
import br.com.fiap.techchallenge.model.Usuario;
import br.com.fiap.techchallenge.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    private final UsuarioService svc;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.svc = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listByNome(
            @RequestParam(name = "q", required = false, defaultValue = "")  String nome) {
        if (nome == null || nome.isEmpty()) {
            nome = "";
        }
        // Convert List<Usuario> to List<UsuarioResponse>
        List<UsuarioResponse> lista = svc.listarUsuarioPorNome(nome).stream()
                .map(usuario -> new UsuarioResponse(usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getUsername(),
                        usuario.getEndereco()))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> get(@PathVariable("id") String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var usuario = svc.buscarUsuarioPorId(Long.parseLong(id));
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var response = new UsuarioResponse(usuario.get().getId(),
                usuario.get().getNome(),
                usuario.get().getEmail(),
                usuario.get().getUsername(),
                usuario.get().getEndereco());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> create(@RequestBody CreateUsuarioRequest dto) {
        // Username obrigatorio
        if (dto.username() == null || dto.username().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Password obrigatorio
        if (dto.password() == null || dto.password().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Verifica se já existe um usuário com o mesmo username
        var existente = svc.buscarUsuarioPorUserame(dto.username());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String novaSenha = passwordEncoder.encode(dto.password());
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setUsername(dto.username());
        usuario.setPassword(novaSenha);
        usuario.setEndereco(dto.endereco());
        Usuario novo = svc.criarUsuario(usuario);
        var response = new UsuarioResponse(novo.getId(),
                novo.getNome(),
                novo.getEmail(),
                novo.getUsername(),
                novo.getEndereco());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/usuarios")
    public ResponseEntity<Void> update(@RequestBody UpdateUsuarioRequest dto) {
        if (dto.id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var existente = svc.buscarUsuarioPorId(dto.id());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Usuario usuario = existente.get();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setEndereco(dto.endereco());
        svc.atualizarUsuario(usuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var existente = svc.buscarUsuarioPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        svc.deletarUsuario(existente.get().getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordRequest dto) {
        var usuario = svc.buscarUsuarioPorUserame(dto.username());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var existente = usuario.get();
        if (!passwordEncoder.matches(dto.password(), existente.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String novaSenha = passwordEncoder.encode(dto.newPassword());
        existente.setPassword(novaSenha);
        svc.atualizarUsuario(existente);
        return ResponseEntity.ok().build();
    }
}
