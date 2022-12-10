package com.api.parkingcontrol.services.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntidadeNaoEncontradaException extends RuntimeException {

	public EntidadeNaoEncontradaException(String msg){
		super(msg);
	}
}
