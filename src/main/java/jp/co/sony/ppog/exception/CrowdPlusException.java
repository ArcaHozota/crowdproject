package jp.co.sony.ppog.exception;

import jp.co.sony.ppog.utils.StringUtils;

/**
 * プロジェクト業務ロジック例外
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public class CrowdPlusException extends RuntimeException {

	private static final long serialVersionUID = 8469408957890840211L;

	/**
	 * メッセージ
	 */
	private final String message;

	public CrowdPlusException() {
		super();
		this.message = StringUtils.EMPTY_STRING;
	}

	public CrowdPlusException(final String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
