package br.com.fiap.techchallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TipoUsuarioInvalidoException extends RuntimeException {
    public TipoUsuarioInvalidoException(String message) {
        super(message);
    }
}
