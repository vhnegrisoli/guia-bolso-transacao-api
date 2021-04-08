package br.com.guiabolso.transacaoapi.config;

import br.com.guiabolso.transacaoapi.modulos.comum.exceptions.ValidacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidacaoHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<?> tratarValidacaoEntradaExceptionParaPadraoRest(ValidacaoException exception) {
        var httpStatus400 = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<> (
            ExceptionDetalhes
                .builder()
                .mensagem(exception.getMessage())
                .status(httpStatus400.value())
                .build(),
            httpStatus400);
    }
}
