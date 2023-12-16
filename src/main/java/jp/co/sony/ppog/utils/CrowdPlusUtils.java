package jp.co.sony.ppog.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.exception.CrowdPlusException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクト共通ツールクラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrowdPlusUtils {

	/**
	 * 現在のリクエストがAJAXリクエストであるかどうかを判断する
	 *
	 * @param request リクエスト
	 * @return true: ajax-request, false: no-ajax
	 */
	public static boolean discernRequestType(final HttpServletRequest request) {
		// リクエストヘッダー情報の取得する
		final String acceptInformation = request.getHeader("Accept");
		final String xRequestInformation = request.getHeader("X-Requested-With");
		// 判断して返却する
		return acceptInformation != null && acceptInformation.length() > 0
				&& acceptInformation.contains("application/json")
				|| xRequestInformation != null && xRequestInformation.length() > 0
						&& "XMLHttpRequest".equals(xRequestInformation);
	}

	/**
	 * 平文文字列をMD5暗号化する
	 *
	 * @param source 文字列
	 * @return String
	 */
	public static String plainToMD5(final String source) {
		// 1.ソースが有効かどうかを判断する
		if (source == null || source.length() == 0) {
			// 2.有効な文字列でない場合は例外をスローする
			throw new CrowdPlusException(CrowdPlusConstants.MESSAGE_STRING_INVALIDATE);
		}
		try {
			// 3.MessageDigestオブジェクトを取得する
			final MessageDigest messageDigest = MessageDigest.getInstance("md5");
			// 4.平文文字列に対応するバイト配列を取得する
			final byte[] input = source.getBytes();
			// 5.暗号化を実行する
			final byte[] output = messageDigest.digest(input);
			// 6.BigIntegerオブジェクトを作成する
			final int signum = 1;
			final BigInteger bigInteger = new BigInteger(signum, output);
			// 7.BigIntegerの値を16進数に従って文字列に変換する
			final int radix = 16;
			return bigInteger.toString(radix).toUpperCase();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY_STRING;
	}
}
