package ua.org.egov.utils.storage.exceptions;

import java.io.IOException;

public class RecordNotFoundException extends IOException {

	public RecordNotFoundException() {
		super();
	}

	public RecordNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordNotFoundException(String message) {
		super(message);
	}

	public RecordNotFoundException(Throwable cause) {
		super(cause);
	}

}
