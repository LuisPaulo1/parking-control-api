package com.api.parkingcontrol.services.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntidadeEmUsoException extends RuntimeException {

	public EntidadeEmUsoException(String msg){
		super(msg);
	}
}
