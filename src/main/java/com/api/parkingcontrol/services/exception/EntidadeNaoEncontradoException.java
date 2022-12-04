package com.api.parkingcontrol.services.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntidadeNaoEncontradoException extends RuntimeException {

	public EntidadeNaoEncontradoException(String msg){
		super(msg);
	}
}
