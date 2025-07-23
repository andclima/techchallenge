package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.CreateUsuarioRequest;
import br.com.fiap.techchallenge.dto.PasswordRequest;
import br.com.fiap.techchallenge.dto.UpdateUsuarioRequest;
import br.com.fiap.techchallenge.dto.UsuarioResponse;
import br.com.fiap.techchallenge.model.Cliente;
import br.com.fiap.techchallenge.model.DonoRestaurante;
import br.com.fiap.techchallenge.model.TipoUsuario;
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
                        usuario.getEndereco(),
                        usuario instanceof DonoRestaurante dono ? dono.getNomeDoRestaurante() : null,
                        usuario instanceof Cliente c ? c.getNumeroFidelidade() : null))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> get(@PathVariable("id") String id) {
        if (id == null || id.isEmpty()) return ResponseEntity.badRequest().build();

        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        var opt = svc.buscarUsuarioPorId(Long.parseLong(id));
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario u = opt.get();

        UsuarioResponse response = new UsuarioResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getUsername(),
                u.getEndereco(),
                u instanceof DonoRestaurante d ? d.getNomeDoRestaurante() : null,
                u instanceof Cliente c ? c.getNumeroFidelidade() : null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/tipo")
    public ResponseEntity<List<UsuarioResponse>> listarPorTipo(
            @RequestParam(name = "tipo") String tipoStr) {

        TipoUsuario tipo = TipoUsuario.from(tipoStr);
        if (tipo == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Usuario> usuarios = svc.listarPorTipo(tipo);
        List<UsuarioResponse> lista = usuarios.stream()
            .map(usuario -> new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getEndereco(),
                usuario instanceof DonoRestaurante dono ? dono.getNomeDoRestaurante() : null,
                usuario instanceof Cliente c ? c.getNumeroFidelidade() : null
            ))
            .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> create(@RequestBody CreateUsuarioRequest dto) {
        if (dto.username() == null || dto.username().isEmpty()
                || dto.password() == null || dto.password().isEmpty()
                || dto.tipo() == null || dto.tipo().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (svc.buscarUsuarioPorUserame(dto.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        TipoUsuario tipo = TipoUsuario.from(dto.tipo());
        if (tipo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Usuario usuario = tipo.criar();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setUsername(dto.username());
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setEndereco(dto.endereco());

        if (usuario instanceof DonoRestaurante dono) {
            if (dto.nomeDoRestaurante() == null || dto.nomeDoRestaurante().isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            dono.setNomeDoRestaurante(dto.nomeDoRestaurante());
        }

        if (usuario instanceof Cliente cliente) {
            if (dto.numeroFidelidade() == null || dto.numeroFidelidade().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            if (svc.buscarPorNumeroFidelidade(dto.numeroFidelidade()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            cliente.setNumeroFidelidade(dto.numeroFidelidade());
        }

        Usuario salvo = svc.criarUsuario(usuario);

        String nomeRestaurante = (salvo instanceof DonoRestaurante d) ? d.getNomeDoRestaurante() : null;
        String numeroFidelidade = (salvo instanceof Cliente c) ? c.getNumeroFidelidade() : null;

        UsuarioResponse response = new UsuarioResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getUsername(),
                salvo.getEndereco(),
                nomeRestaurante,
                numeroFidelidade
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/usuarios")
    public ResponseEntity<Void> update(@RequestBody UpdateUsuarioRequest dto) {
        if (dto.id() == null) return ResponseEntity.badRequest().build();

        var existenteOpt = svc.buscarUsuarioPorId(dto.id());
        if (existenteOpt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario usuario = existenteOpt.get();

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setEndereco(dto.endereco());

        if (usuario instanceof DonoRestaurante dono) {
            dono.setNomeDoRestaurante(dto.nomeDoRestaurante());
            if (dto.numeroFidelidade() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        if (usuario instanceof Cliente cliente) {
            // Verifica se está tentando alterar para um número de fidelidade já usado
            if (dto.numeroFidelidade() != null &&
                !dto.numeroFidelidade().equals(cliente.getNumeroFidelidade())) {

                var outro = svc.buscarPorNumeroFidelidade(dto.numeroFidelidade());
                if (outro.isPresent() && !outro.get().getId().equals(cliente.getId())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }

                cliente.setNumeroFidelidade(dto.numeroFidelidade());
            }

            if (dto.nomeDoRestaurante() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

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