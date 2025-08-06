package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.*;
import br.com.fiap.techchallenge.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UsuarioController {

    private final UsuarioService svc;

    public UsuarioController(UsuarioService usuarioService) {
        this.svc = usuarioService;
    }

    // ----------------------- LISTAR POR NOME -----------------------
    @GetMapping("/usuarios")
    public ResponseEntity<List<? extends UsuarioResponseBase>> listByNome(
            @RequestParam(name = "q", required = false, defaultValue = "") String nome) {

        List<? extends UsuarioResponseBase> listaDeUsuarios = svc.listarEConverterPorNome(nome);
        return ResponseEntity.ok(listaDeUsuarios);
    }

    // ----------------------- LISTAR POR ID -----------------------
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseBase> get(@PathVariable("id") Long id) {
        UsuarioResponseBase usuarioResponse = svc.buscarEConverterPorId(id);
        return ResponseEntity.ok(usuarioResponse);
    }

    // ----------------------- LISTAR POR TIPO -----------------------
    @GetMapping("/usuarios/tipo")
    public ResponseEntity<List<? extends UsuarioResponseBase>> listarPorTipo(
            @RequestParam(name = "tipo") String tipo) {

        List<? extends UsuarioResponseBase> lista = svc.listarEConverterPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }

    // ----------------------- CRIAR USUÁRIO -----------------------
    @PostMapping("/usuarios")
    public ResponseEntity<? extends UsuarioResponseBase> create(@RequestBody @Valid CreateUsuarioRequest dto) {
        UsuarioResponseBase novoUsuario = svc.criarNovoUsuario(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(novoUsuario);
    }

    // ----------------------- ATUALIZAR USUÁRIO -----------------------
    @PutMapping("/usuarios")
    public ResponseEntity<? extends UsuarioResponseBase> update(@RequestBody @Valid UpdateUsuarioRequest dto) {
        UsuarioResponseBase usuarioAtualizado = svc.atualizarUsuarioExistente(dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    // ----------------------- DELETAR USUÁRIO -----------------------
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        svc.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------- ALTERAR SENHA -----------------------
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid PasswordRequest dto) {
        svc.alterarSenha(dto);
        return ResponseEntity.noContent().build();
    }
}