package com.mitocode.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND) //se comento porque en la clase que conntrola todas las excepciones (ResponseExceptionHandler) ya se hace referencia a una similar
public class ModeloNotFoundException extends RuntimeException{

	public ModeloNotFoundException(String mensaje) {
		super(mensaje);
	}
}
