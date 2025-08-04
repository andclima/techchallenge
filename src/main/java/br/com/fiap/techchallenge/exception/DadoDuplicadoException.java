package br.com.fiap.techchallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DadoDuplicadoException extends RuntimeException {
    public DadoDuplicadoException(String message) {
        super(message);
    }
}
