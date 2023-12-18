package jp.co.sony.ppog.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.google.gson.Gson;

import jp.co.sony.ppog.config.ResponseLoginDto;
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
		return ((acceptInformation != null) && (acceptInformation.length() > 0)
				&& acceptInformation.contains("application/json"))
				|| ((xRequestInformation != null) && (xRequestInformation.length() > 0)
						&& "XMLHttpRequest".equals(xRequestInformation));
	}

	/**
	 * 文字列をクライアントにレンダリングする
	 *
	 * @param response リスポンス
	 * @param string   ストリング
	 */
	public static void renderString(final HttpServletResponse response, final ResponseLoginDto aResult) {
		final Gson gson = new Gson();
		try {
			response.setStatus(aResult.getCode());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().print(gson.toJson(aResult));
		} catch (final IOException e) {
			// do nothing
		}
	}
}
