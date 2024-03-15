package jp.co.sony.ppog.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import jp.co.sony.ppog.commons.CrowdProjectConstants;
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
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/pgcrowd/category/static/image/flags/**")
				.addResourceLocations("classpath:/static/image/flags/");
	}

	/**
	 * ビューのコントローラを定義する
	 *
	 * @param registry
	 */
	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/pgcrowd/employee/to/signup").setViewName("toroku");
		registry.addViewController("/pgcrowd/employee/login").setViewName("admin-login");
		registry.addViewController("/pgcrowd/to/mainmenu").setViewName("mainmenu");
		registry.addViewController("/pgcrowd/menu/initial").setViewName("menukanri");
		registry.addViewController("/pgcrowd/employee/to/pages").setViewName("admin-pages");
		registry.addViewController("/pgcrowd/role/to/pages").setViewName("role-pages");
		registry.addViewController("/pgcrowd/category/initial").setViewName("categorykanri");
		registry.addViewController("/pgcrowd/category/to/districtPages").setViewName("district-pages");
		registry.addViewController("/pgcrowd/category/to/cityPages").setViewName("city-pages");
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
