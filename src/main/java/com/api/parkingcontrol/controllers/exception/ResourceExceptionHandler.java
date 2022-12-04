package com.api.parkingcontrol.controllers.exception;

import java.time.Instant;
import javax.servlet.http.HttpServletRequest;

import com.api.parkingcontrol.services.exception.EntidadeEmUsoException;
import com.api.parkingcontrol.services.exception.EntidadeNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<StandardError> handleResourceNotFound(EntidadeEmUsoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Conflito de dados");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(EntidadeNaoEncontradoException.class)
	public ResponseEntity<StandardError> handleResourceNotFound(EntidadeNaoEncontradoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Entidade não encontrada");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> erroDeSistema(Exception e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Erro de sistema");
		err.setMessage("Ocorreu um erro interno inesperado no sistema");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}
