package jp.co.sony.ppog.utils;

import lombok.Data;

/**
 * Ajaxリクエストの戻るクラス
 *
 * @param <T> データタイプ
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public class ResultDto<T> {

	/**
	 * 処理成功のステータス
	 */
	private static final String SUCCESS = "SUCCESS";

	/**
	 * 処理異常終了のステータス
	 */
	private static final String FAILED = "FAILURE";

	/**
	 * エラーメッセージが無し
	 */
	private static final String NO_MESSAGE = "NO_MESSAGE";

	/**
	 * リターン操作の結果はデー​​タなしで失敗となります
	 *
	 * @param message
	 * @return ResultDto<T>
	 */
	public static <T> ResultDto<T> failed(final String message) {
		return new ResultDto<>(FAILED, message, null);
	}

	/**
	 * リターン操作の結果は成功してデータが送信されます
	 *
	 * @param data
	 * @return ResultDto<T>
	 */
	public static <T> ResultDto<T> successWithData(final T data) {
		return new ResultDto<>(SUCCESS, NO_MESSAGE, data);
	}

	/**
	 * リターン操作の結果はデー​​タなしで成功となります
	 *
	 * @return
	 */
	public static <T> ResultDto<T> successWithoutData() {
		return new ResultDto<>(SUCCESS, NO_MESSAGE, null);
	}

	/**
	 * ステータス
	 */
	private String status;

	/**
	 * メッセージ
	 */
	private String message;

	/**
	 * 検索したデータ
	 */
	private T data;

	/**
	 * コンストラクタ
	 *
	 * @param status  ステータス
	 * @param message メッセージ
	 * @param data    検索したデータ
	 */
	private ResultDto(final String status, final String message, final T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
}
