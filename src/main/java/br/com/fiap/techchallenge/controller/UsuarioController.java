package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.*;
import br.com.fiap.techchallenge.model.Cliente;
import br.com.fiap.techchallenge.model.DonoRestaurante;
import br.com.fiap.techchallenge.model.TipoUsuario;
import br.com.fiap.techchallenge.model.Usuario;
import br.com.fiap.techchallenge.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    private final UsuarioService svc;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UsuarioController(UsuarioService usuarioService,
                             PasswordEncoder passwordEncoder,
                             ModelMapper modelMapper) {
        this.svc = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    // ----------------------- LISTAR POR NOME -----------------------
    @GetMapping("/usuarios")
    public ResponseEntity<List<? extends UsuarioResponseBase>> listByNome(
            @RequestParam(name = "q", required = false, defaultValue = "") String nome) {

        List<? extends UsuarioResponseBase> lista = svc.listarUsuarioPorNome(nome).stream()
                .map(usuario -> {
                    if (usuario instanceof DonoRestaurante) {
                        return modelMapper.map(usuario, DonoRestauranteResponse.class);
                    } else if (usuario instanceof Cliente) {
                        return modelMapper.map(usuario, ClienteResponse.class);
                    }
                    return null;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }

    // ----------------------- LISTAR POR ID -----------------------
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseBase> get(@PathVariable("id") Long id) {
        var usuarioOpt = svc.buscarUsuarioPorId(id);
        if (usuarioOpt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario usuario = usuarioOpt.get();

        if (usuario instanceof DonoRestaurante) {
            return ResponseEntity.ok(modelMapper.map(usuario, DonoRestauranteResponse.class));
        } else if (usuario instanceof Cliente) {
            return ResponseEntity.ok(modelMapper.map(usuario, ClienteResponse.class));
        }

        return ResponseEntity.notFound().build();
    }

    // ----------------------- LISTAR POR TIPO -----------------------
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
                    if (usuario instanceof DonoRestaurante) {
                        return modelMapper.map(usuario, DonoRestauranteResponse.class);
                    } else if (usuario instanceof Cliente) {
                        return modelMapper.map(usuario, ClienteResponse.class);
                    }
                    return null;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }

    // ----------------------- CRIAR USUÁRIO -----------------------
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

        if (salvo instanceof DonoRestaurante) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(modelMapper.map(salvo, DonoRestauranteResponse.class));
        } else if (salvo instanceof Cliente) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(modelMapper.map(salvo, ClienteResponse.class));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // ----------------------- ATUALIZAR USUÁRIO -----------------------
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

        if (salvo instanceof DonoRestaurante) {
            return ResponseEntity.ok(modelMapper.map(salvo, DonoRestauranteResponse.class));
        } else if (salvo instanceof Cliente) {
            return ResponseEntity.ok(modelMapper.map(salvo, ClienteResponse.class));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // ----------------------- DELETAR USUÁRIO -----------------------
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

    // ----------------------- ALTERAR SENHA -----------------------
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