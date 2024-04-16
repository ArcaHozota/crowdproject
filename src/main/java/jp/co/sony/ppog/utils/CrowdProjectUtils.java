package jp.co.sony.ppog.utils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;

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
	 * 共通権限管理ストリーム
	 *
	 * @param stream 権限ストリーム
	 * @return List<String>
	 */
	public static final List<String> getAuthNames(final Stream<GrantedAuthority> stream) {
		return stream.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
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
