package jp.co.sony.ppog.commons;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクトURLコンスタント
 *
 * @author ArkamaHozota
 * @since 6.51
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrowdProjectURLConstants {

	public static final RequestMatcher URL_STATIC_RESOURCE = new AntPathRequestMatcher("/static/**",
			RequestMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_TO_PAGES = new AntPathRequestMatcher("/pgcrowd/employee/to/pages",
			RequestMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_PAGINATION = new AntPathRequestMatcher(
			"/pgcrowd/employee/pagination", RequestMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_INFOSAVE = new AntPathRequestMatcher("/pgcrowd/employee/infosave",
			RequestMethod.POST.toString());

	public static final RequestMatcher URL_EMPLOYEE_TO_ADDITION = new AntPathRequestMatcher(
			"/pgcrowd/employee/to/addition", RequestMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_TO_EDITION = new AntPathRequestMatcher(
			"/pgcrowd/employee/to/edition", RequestMethod.GET.toString());

	public static final RequestMatcher URL_EMPLOYEE_INFOUPD = new AntPathRequestMatcher("/pgcrowd/employee/infoupd",
			RequestMethod.PUT.toString());

	public static final RequestMatcher URL_EMPLOYEE_DELETE = new AntPathRequestMatcher("/pgcrowd/employee/delete/**",
			RequestMethod.DELETE.toString());

	public static final RequestMatcher URL_ROLE_TO_PAGES = new AntPathRequestMatcher("/pgcrowd/role/to/pages",
			RequestMethod.GET.toString());

	public static final RequestMatcher URL_ROLE_PAGINATION = new AntPathRequestMatcher("/pgcrowd/role/pagination",
			RequestMethod.GET.toString());

	public static final RequestMatcher URL_ROLE_GET_ASSIGNED = new AntPathRequestMatcher("/pgcrowd/role/getAssigned",
			RequestMethod.GET.toString());

	public static final RequestMatcher URL_ROLE_INFOSAVE = new AntPathRequestMatcher("/pgcrowd/role/infosave",
			RequestMethod.POST.toString());

	public static final RequestMatcher URL_ROLE_INFOUPD = new AntPathRequestMatcher("/pgcrowd/role/infoupd",
			RequestMethod.PUT.toString());

	public static final RequestMatcher URL_ROLE_DO_ASSIGNMENT = new AntPathRequestMatcher("/pgcrowd/role/do/assignment",
			RequestMethod.DELETE.toString());

	public static final RequestMatcher URL_ROLE_DELETE = new AntPathRequestMatcher("/pgcrowd/role/delete/**",
			RequestMethod.DELETE.toString());

	public static final RequestMatcher URL_DISTRICT_PAGIANTION = new AntPathRequestMatcher(
			"/pgcrowd/district/pagination", RequestMethod.GET.toString());

	public static final RequestMatcher URL_DISTRICT_INFOUPD = new AntPathRequestMatcher("/pgcrowd/district/infoupd",
			RequestMethod.PUT.toString());

	public static final RequestMatcher URL_CITY_PAGIANTION = new AntPathRequestMatcher("/pgcrowd/city/pagination",
			RequestMethod.GET.toString());

	public static final RequestMatcher URL_CITY_DISTRICT_LIST = new AntPathRequestMatcher("/pgcrowd/city/districtlist",
			RequestMethod.GET.toString());

	public static final RequestMatcher URL_CITY_INFOSAVE = new AntPathRequestMatcher("/pgcrowd/city/infosave",
			RequestMethod.POST.toString());

	public static final RequestMatcher URL_CITY_INFOUPD = new AntPathRequestMatcher("/pgcrowd/city/infoupd",
			RequestMethod.PUT.toString());
}
