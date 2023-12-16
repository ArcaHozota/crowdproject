package jp.co.sony.ppog.exception;

/**
 * ログイン失敗の例外
 *
 * @author ArkamaHozota
 * @since 1.31
 */
public class LoginFailedException extends RuntimeException {

	private static final long serialVersionUID = 5960461202384154200L;

	public LoginFailedException() {
		super();
	}

	public LoginFailedException(final String message) {
		super(message);
	}

	public LoginFailedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public LoginFailedException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoginFailedException(final Throwable cause) {
		super(cause);
	}
}
