package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.dto.*;
import br.com.fiap.techchallenge.exception.CredenciaisInvalidasException;
import br.com.fiap.techchallenge.exception.DadoDuplicadoException;
import br.com.fiap.techchallenge.exception.RecursoNaoEncontradoException;
import br.com.fiap.techchallenge.exception.TipoUsuarioInvalidoException;
import br.com.fiap.techchallenge.model.Cliente;
import br.com.fiap.techchallenge.model.DonoRestaurante;
import br.com.fiap.techchallenge.model.TipoUsuario;
import br.com.fiap.techchallenge.model.Usuario;
import br.com.fiap.techchallenge.repository.ClienteRepository;
import br.com.fiap.techchallenge.repository.DonoRestauranteRepository;
import br.com.fiap.techchallenge.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final DonoRestauranteRepository donoRestauranteRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          ClienteRepository clienteRepository,
                          DonoRestauranteRepository donoRestauranteRepository,
                          ModelMapper modelMapper,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.donoRestauranteRepository = donoRestauranteRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public List<? extends UsuarioResponseBase> listarEConverterPorNome(String nome) {
        return this.listarUsuarioPorNome(nome).stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public UsuarioResponseBase buscarEConverterPorId(Long id) {
        Usuario usuario = this.buscarUsuarioPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com ID " + id + " não encontrado."));
        return this.converterParaResponseDTO(usuario);
    }

    public List<? extends UsuarioResponseBase> listarEConverterPorTipo(String tipoStr) {
        TipoUsuario tipo = TipoUsuario.from(tipoStr);
        if (tipo == null) {
            throw new TipoUsuarioInvalidoException("O tipo de usuário '" + tipoStr + "' é inválido. Valores aceitos: CLIENTE, DONO.");
        }

        List<Usuario> usuarios = this.listarPorTipo(tipo);
        return usuarios.stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    //Método uníco para converter usuários em DTOs
    private UsuarioResponseBase converterParaResponseDTO(Usuario usuario) {
        if (usuario instanceof DonoRestaurante) {
            return modelMapper.map(usuario, DonoRestauranteResponse.class);
        } else if (usuario instanceof Cliente) {
            return modelMapper.map(usuario, ClienteResponse.class);
        }
        throw new IllegalStateException("Tipo de usuário não mapeado para DTO: " + usuario.getClass().getName());
    }

    private Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    public UsuarioResponseBase criarNovoUsuario(CreateUsuarioRequest dto) {

        if (this.buscarUsuarioPorUserame(dto.username()).isPresent()) {
            throw new DadoDuplicadoException("O username '" + dto.username() + "' já está em uso.");
        }

        TipoUsuario tipo = TipoUsuario.from(dto.tipo());
        if (tipo == null) {
            throw new TipoUsuarioInvalidoException("O tipo de usuário '" + dto.tipo() + "' é inválido. Valores aceitos: CLIENTE, DONO.");
        }

        Usuario usuario = tipo.criar();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setUsername(dto.username());
        usuario.setEndereco(dto.endereco());
        usuario.setPassword(passwordEncoder.encode(dto.password()));

        if (usuario instanceof DonoRestaurante dono) {
            if (dto.nomeDoRestaurante() == null || dto.nomeDoRestaurante().isBlank()) {
                throw new IllegalArgumentException("O nome do restaurante é obrigatório para o tipo DONO.");
            }
            dono.setNomeDoRestaurante(dto.nomeDoRestaurante());
        }

        if (usuario instanceof Cliente cliente) {
            if (dto.numeroFidelidade() == null || dto.numeroFidelidade().isBlank()) {
                throw new IllegalArgumentException("O número de fidelidade é obrigatório para o tipo CLIENTE.");
            }
            if (this.buscarPorNumeroFidelidade(dto.numeroFidelidade()).isPresent()) {
                throw new DadoDuplicadoException("O número de fidelidade '" + dto.numeroFidelidade() + "' já está em uso.");
            }
            cliente.setNumeroFidelidade(dto.numeroFidelidade());
        }

        Usuario salvo = this.criarUsuario(usuario);
        return this.converterParaResponseDTO(salvo);
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    public UsuarioResponseBase atualizarUsuarioExistente(UpdateUsuarioRequest dto) {
        Usuario usuario = this.buscarUsuarioPorId(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com ID " + dto.id() + " não encontrado para atualização."));

        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }
        if (dto.email() != null) {
            usuario.setEmail(dto.email());
        }
        if (dto.endereco() != null) {
            usuario.setEndereco(dto.endereco());
        }

        if (usuario instanceof DonoRestaurante dono) {
            if (dto.numeroFidelidade() != null) {
                throw new IllegalArgumentException("O campo 'numeroFidelidade' não pode ser atualizado para um DONO.");
            }
            if (dto.nomeDoRestaurante() != null) {
                dono.setNomeDoRestaurante(dto.nomeDoRestaurante());
            }
        }

        if (usuario instanceof Cliente cliente) {
            if (dto.nomeDoRestaurante() != null) {
                throw new IllegalArgumentException("O campo 'nomeDoRestaurante' não pode ser atualizado para um CLIENTE.");
            }
            if (dto.numeroFidelidade() != null && !dto.numeroFidelidade().equals(cliente.getNumeroFidelidade())) {
                this.buscarPorNumeroFidelidade(dto.numeroFidelidade()).ifPresent(outroCliente -> {
                    if (!outroCliente.getId().equals(cliente.getId())) {
                        throw new DadoDuplicadoException("O número de fidelidade '" + dto.numeroFidelidade() + "' já está em uso por outro cliente.");
                    }
                });
                cliente.setNumeroFidelidade(dto.numeroFidelidade());
            }
        }
        Usuario salvo = this.atualizarUsuario(usuario);
        return this.converterParaResponseDTO(salvo);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não é possível deletar. Usuário com ID " + id + " não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    public void alterarSenha(PasswordRequest dto) {
        Usuario usuario = this.buscarUsuarioPorUserame(dto.username())
                .filter(user -> passwordEncoder.matches(dto.password(), user.getPassword()))
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário ou senha inválida."));

        usuario.setPassword(passwordEncoder.encode(dto.newPassword()));
        this.atualizarUsuario(usuario);
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

    public List<Usuario> listarPorTipo(TipoUsuario tipo) {
        return switch (tipo) {
            case CLIENTE -> new ArrayList<>(clienteRepository.findAll());
            case DONO -> new ArrayList<>(donoRestauranteRepository.findAll());
        };
    }

    public Optional<Cliente> buscarPorNumeroFidelidade(String numero) {
        return clienteRepository.findByNumeroFidelidade(numero);
    }
}