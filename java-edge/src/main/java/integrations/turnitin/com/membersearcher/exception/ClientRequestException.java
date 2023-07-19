package integrations.turnitin.com.membersearcher.exception;

public class ClientRequestException extends RuntimeException {
	public ClientRequestException() {
	}

	public ClientRequestException(String message) {
		super(message);
	}

	public ClientRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientRequestException(Throwable cause) {
		super(cause);
	}

	public ClientRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
