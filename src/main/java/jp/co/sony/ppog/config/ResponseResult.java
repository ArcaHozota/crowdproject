package jp.co.sony.ppog.config;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * リスポンスエンティティ
 *
 * @author ArkamaHozota
 * @since 1.50
 */
@Getter
@Setter
public class ResponseResult implements Serializable {

	private static final long serialVersionUID = -5033762283136118856L;

	/**
	 * HTTPステータスコード
	 */
	private Integer code;

	/**
	 * エラーメッセージ
	 */
	private String message;

	/**
	 * コンストラクタ
	 */
	protected ResponseResult() {
		this.code = 200;
	}

	/**
	 * パラメータつきコンストラクタ
	 *
	 * @param code    コード
	 * @param message メッセージ
	 */
	protected ResponseResult(final Integer code, final String message) {
		this.code = code;
		this.message = message;
	}
}
