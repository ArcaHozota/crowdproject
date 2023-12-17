package jp.co.sony.ppog.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクトコンスタント
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CrowdPlusConstants {

	public static final Integer DEFAULT_PAGE_SIZE = 7;

	public static final String MESSAGE_SPRING_MVCCONVERTOR = "拡張メッセージコンバーターの設置は完了しました。";

	public static final String MESSAGE_SPRING_MAPPER = "静的リソースのマッピングが開始しました。";

	public static final String MESSAGE_SPRING_APPLICATION = "アプリは正常に起動しました。";

	public static final String ATTRNAME_EXCEPTION = "exception";

	public static final String ATTRNAME_LOGIN_ADMIN = "employee";

	public static final String ATTRNAME_EDITED_INFO = "arawaseta";

	public static final String ATTRNAME_EMPLOYEEROLES = "employeeRoles";

	public static final String ATTRNAME_PAGE_INFO = "pageInfo";

	public static final String ATTRNAME_PAGE_NUMBER = "pageNum";

	public static final String ATTRNAME_AUTHORITY_ROLE = "role";

	public static final String LOGIC_DELETE_FLG = "rejected";

	public static final String LOGIC_DELETE_INITIAL = "approved";

	public static final String MESSAGE_STRING_INVALIDATE = "Invalid password string";

	public static final String MESSAGE_STRING_NOTLOGIN = "ログインしてください";

	public static final String MESSAGE_STRING_PROHIBITED = "ユーザは存在しません、もう一度やり直してください";

	public static final String MESSAGE_STRING_NOTEXISTS = "役割は存在しません、もう一度やり直してください";

	public static final String MESSAGE_STRING_FORBIDDEN = "役割は利用されています、削除できません。";

	public static final String MESSAGE_STRING_FORBIDDEN2 = "権限付与にエラーが発生しました。";

	public static final String MESSAGE_STRING_FATALERROR = "システムエラーが発生しました。";

	public static final String MESSAGE_STRING_DUPLICATED = "ログインアカウントがすでに存在します。";

	public static final String MESSAGE_ROLE_NAME_DUPLICATED = "役割名称がすでに存在します。";

	public static final String MESSAGE_SPRINGSECURITY_LOGINERROR1 = "当ユーザは存在しません。もう一度やり直してください。";

	public static final String MESSAGE_SPRINGSECURITY_LOGINERROR2 = "当ユーザの役割情報が存在しません。ログイン拒否。";

	public static final String MESSAGE_SPRINGSECURITY_LOGINERROR3 = "当ユーザの役割がありますが、役割権限がないのでログイン拒否。";

	public static final String DEFAULT_ROLE_NAME = "---------------------------";

	public static final String MESSAGE_SPRING_SECURITY = "スプリングセキュリティ作動中。";
}
