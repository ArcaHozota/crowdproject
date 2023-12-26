package jp.co.sony.ppog.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクトURLコンスタント
 *
 * @author ArkamaHozota
 * @since 1.93
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class URIConstants {

	public static final String EMPLOYEE_PAGINATION = "/pgcrowd/employee/pagination";

	public static final String EMPLOYEE_CHECK = "/pgcrowd/employee/check";

	public static final String EMPLOYEE_DELETE = "/pgcrowd/employee/delete/{userId}";

	public static final String EMPLOYEE_ADDITION = "/pgcrowd/employee/to/addition";

	public static final String EMPLOYEE_EDITION = "/pgcrowd/employee/to/edition";
}
