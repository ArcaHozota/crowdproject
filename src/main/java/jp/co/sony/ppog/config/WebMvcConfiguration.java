package jp.co.sony.ppog.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import jp.co.sony.ppog.commons.CrowdProjectConstants;
import jp.co.sony.ppog.commons.CrowdProjectURLConstants;
import lombok.extern.log4j.Log4j2;

/**
 * SpringMVC配置クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Log4j2
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

	/**
	 * 静的なリソースのマッピングを設定する
	 *
	 * @param registry レジストリ
	 */
	@Override
	protected void addResourceHandlers(final ResourceHandlerRegistry registry) {
		log.info(CrowdProjectConstants.MESSAGE_SPRING_MAPPER);
		registry.addResourceHandler(CrowdProjectURLConstants.URL_STATIC_RESOURCE.getPattern())
				.addResourceLocations("classpath:/static/");
	}

	/**
	 * ビューのコントローラを定義する
	 *
	 * @param registry
	 */
	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController(CrowdProjectURLConstants.URL_TO_SIGN_UP.getPattern()).setViewName("admin-toroku");
		registry.addViewController(CrowdProjectURLConstants.URL_TO_LOGIN.getPattern()).setViewName("admin-login");
		registry.addViewController(CrowdProjectURLConstants.URL_TO_MAINMENU.getPattern()).setViewName("mainmenu");
		registry.addViewController(CrowdProjectURLConstants.URL_MENU_INITIAL.getPattern()).setViewName("menukanri");
		registry.addViewController(CrowdProjectURLConstants.URL_EMPLOYEE_TO_PAGES.getPattern())
				.setViewName("admin-pages");
		registry.addViewController(CrowdProjectURLConstants.URL_ROLE_TO_PAGES.getPattern()).setViewName("role-pages");
		registry.addViewController(CrowdProjectURLConstants.URL_CATEGORY_INITIAL.getPattern())
				.setViewName("categorykanri");
		registry.addViewController(CrowdProjectURLConstants.URL_TO_DISTRICT_PAGES.getPattern())
				.setViewName("district-pages");
		registry.addViewController(CrowdProjectURLConstants.URL_TO_CITY_PAGES.getPattern()).setViewName("city-pages");
	}

	/**
	 * SpringMVCフレームワークを拡張するメッセージ・コンバーター
	 *
	 * @param converters コンバーター
	 */
	@Override
	protected void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
		log.info(CrowdProjectConstants.MESSAGE_SPRING_MVCCONVERTOR);
		// メッセージコンバータオブジェクトを作成する。
		final MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		// オブジェクトコンバータを設定し、Jacksonを使用してJavaオブジェクトをJSONに変換する。
		messageConverter.setObjectMapper(new JacksonObjectMapper());
		// 上記のメッセージコンバータをSpringMVCフレームワークのコンバータコンテナに追加する。
		converters.add(0, messageConverter);
	}
}
