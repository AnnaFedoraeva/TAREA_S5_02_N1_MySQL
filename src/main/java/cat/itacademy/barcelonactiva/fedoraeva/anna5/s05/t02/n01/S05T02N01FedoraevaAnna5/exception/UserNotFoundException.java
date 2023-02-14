package cat.itacademy.barcelonactiva.fedoraeva.anna5.s05.t02.n01.S05T02N01FedoraevaAnna5.exception;

import java.text.MessageFormat;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}

	public UserNotFoundException(Long id) {
		super(MessageFormat.format("Could not find user with id: " + id, id));
	}

}
