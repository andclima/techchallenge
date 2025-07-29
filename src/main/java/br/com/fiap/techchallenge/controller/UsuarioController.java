package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.ClienteResponse;
import br.com.fiap.techchallenge.dto.CreateUsuarioRequest;
import br.com.fiap.techchallenge.dto.DonoRestauranteResponse;
import br.com.fiap.techchallenge.dto.PasswordRequest;
import br.com.fiap.techchallenge.dto.UpdateUsuarioRequest;
import br.com.fiap.techchallenge.dto.UsuarioResponseBase;
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
    public ResponseEntity<List<? extends UsuarioResponseBase>> listByNome(
            @RequestParam(name = "q", required = false, defaultValue = "") String nome) {

        List<? extends UsuarioResponseBase> lista = svc.listarUsuarioPorNome(nome).stream()
            .map(usuario -> {
                if (usuario instanceof DonoRestaurante dono) {
                    return new DonoRestauranteResponse(
                        dono.getId(),
                        dono.getNome(),
                        dono.getEmail(),
                        dono.getUsername(),
                        dono.getEndereco(),
                        dono.getNomeDoRestaurante()
                    );
                } else if (usuario instanceof Cliente cliente) {
                    return new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getUsername(),
                        cliente.getEndereco(),
                        cliente.getNumeroFidelidade()
                    );
                }
                return null;
            })
            .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseBase> get(@PathVariable("id") Long id) {
        var usuarioOpt = svc.buscarUsuarioPorId(id);
        if (usuarioOpt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario usuario = usuarioOpt.get();
        UsuarioResponseBase response;

        if (usuario instanceof DonoRestaurante dono) {
            response = new DonoRestauranteResponse(
                dono.getId(),
                dono.getNome(),
                dono.getEmail(),
                dono.getUsername(),
                dono.getEndereco(),
                dono.getNomeDoRestaurante()
            );
        } else if (usuario instanceof Cliente cliente) {
            response = new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getUsername(),
                cliente.getEndereco(),
                cliente.getNumeroFidelidade()
            );
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/tipo")
    public ResponseEntity<List<? extends UsuarioResponseBase>> listarPorTipo(
            @RequestParam(name = "tipo") String tipoStr) {

        TipoUsuario tipo = TipoUsuario.from(tipoStr);
        if (tipo == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Usuario> usuarios = svc.listarPorTipo(tipo);

        List<? extends UsuarioResponseBase> lista = usuarios.stream()
            .map(usuario -> {
                if (usuario instanceof DonoRestaurante dono) {
                    return new DonoRestauranteResponse(
                        dono.getId(),
                        dono.getNome(),
                        dono.getEmail(),
                        dono.getUsername(),
                        dono.getEndereco(),
                        dono.getNomeDoRestaurante()
                    );
                } else if (usuario instanceof Cliente cliente) {
                    return new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getUsername(),
                        cliente.getEndereco(),
                        cliente.getNumeroFidelidade()
                    );
                }
                return null;
            })
            .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<? extends UsuarioResponseBase> create(@RequestBody CreateUsuarioRequest dto) {
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

        if (salvo instanceof DonoRestaurante d) {
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new DonoRestauranteResponse(
                            d.getId(), d.getNome(), d.getEmail(), d.getUsername(),
                            d.getEndereco(), d.getNomeDoRestaurante()));
        } else if (salvo instanceof Cliente c) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ClienteResponse(
                            c.getId(), c.getNome(), c.getEmail(), c.getUsername(),
                            c.getEndereco(), c.getNumeroFidelidade()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/usuarios")
    public ResponseEntity<? extends UsuarioResponseBase> update(@RequestBody UpdateUsuarioRequest dto) {
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

        Usuario salvo = svc.atualizarUsuario(usuario);

        if (salvo instanceof DonoRestaurante d) {
            return ResponseEntity.ok(new DonoRestauranteResponse(
                    d.getId(), d.getNome(), d.getEmail(), d.getUsername(),
                    d.getEndereco(), d.getNomeDoRestaurante()));
        } else if (salvo instanceof Cliente c) {
            return ResponseEntity.ok(new ClienteResponse(
                    c.getId(), c.getNome(), c.getEmail(), c.getUsername(),
                    c.getEndereco(), c.getNumeroFidelidade()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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