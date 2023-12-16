package jp.co.sony.ppog.listener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.exception.CrowdPlusException;
import jp.co.sony.ppog.utils.CrowdPlusUtils;
import jp.co.sony.ppog.utils.ResultDto;

/**
 * アノテーションに基づく例外ハンドラークラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@ControllerAdvice
public final class CrowdPlusExceptionResolver {

	/**
	 * コア例外処理メソッド
	 *
	 * @param exception 例外
	 * @param request   リクエスト
	 * @param response  リスポンス
	 * @param viewName  ビュー名称
	 * @return ModelAndView
	 * @throws IOException
	 */
	private ModelAndView commonResolveException(final Exception exception, final HttpServletRequest request,
			final HttpServletResponse response, final String viewName) throws IOException {
		// 1.リクエストが「通常のリクエスト」であるか「AJAXリクエスト」であるかを判断する。
		final boolean ajaxOrNot = CrowdPlusUtils.discernRequestType(request);
		// 2.AJAXリクエストの場合。
		if (ajaxOrNot) {
			// 3.例外オブジェクトから例外情報を取得する。
			final String message = exception.getMessage();
			// 4.ResultDtoオブジェクトを作成する。
			final ResultDto<Object> resultEntity = ResultDto.failed(message);
			// 5.GSONオブジェクトを作成する。
			final Gson gson = new Gson();
			// 6.JSONストリングに変換する。
			final String json = gson.toJson(resultEntity);
			// 7.PrintWriterオブジェクトを取得する。
			final PrintWriter writer = response.getWriter();
			// 8.JSONデータをライトしてNULLを返却する。
			writer.write(json);
			return null;
		}
		// 9.ModelAndViewオブジェクトを作成する。
		final ModelAndView modelAndView = new ModelAndView();
		// 10.ターゲットビューの名称を設定する。
		modelAndView.setViewName(viewName);
		// 11.例外オブジェクトをモデルに保存する。
		modelAndView.addObject(CrowdPlusConstants.ATTRNAME_EXCEPTION, exception);
		// 12.ModelAndViewオブジェクトを返却する。
		return modelAndView;
	}

	/**
	 * 業務ロジック例外を処理する
	 *
	 * @param exception 例外名
	 * @param request   リクエスト
	 * @param response  リスポンス
	 * @return ModelAndView モデルビューオブジェクト
	 * @throws IOException
	 */
	@ExceptionHandler(value = CrowdPlusException.class)
	public ModelAndView resolveCrowdPlusException(final CrowdPlusException exception, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		// 現在の例外に対応するページを指定する
		final String viewName = "system-error";
		return this.commonResolveException(exception, request, response, viewName);
	}

	/**
	 * ログイン例外を処理する
	 *
	 * @param exception 例外名
	 * @param request   リクエスト
	 * @param response  リスポンス
	 * @return ModelAndView モデルビューオブジェクト
	 * @throws IOException
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public String resolveUsernameNotFoundException(final UsernameNotFoundException exception, final Model model) {
		// 現在の例外に対応するページを指定する
		final String viewName = "admin-login";
		model.addAttribute(CrowdPlusConstants.ATTRNAME_EXCEPTION, exception);
		return viewName;
	}
}
