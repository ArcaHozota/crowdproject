package jp.co.sony.ppog.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.alibaba.fastjson2.JSON;

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
public final class CrowdProjectUtils {

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
				&& acceptInformation.contains(MediaType.APPLICATION_JSON_VALUE))
				|| ((xRequestInformation != null) && (xRequestInformation.length() > 0)
						&& "XMLHttpRequest".equals(xRequestInformation));
	}

	/**
	 * 文字列をクライアントにレンダリングする
	 *
	 * @param response リスポンス
	 * @param string   ストリング
	 */
	@SuppressWarnings("deprecation")
	public static void renderString(final HttpServletResponse response, final ResponseLoginDto aResult) {
		try {
			response.setStatus(aResult.getCode());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.getWriter().print(JSON.toJSONString(aResult));
		} catch (final IOException e) {
			// do nothing
		}
	}
}
