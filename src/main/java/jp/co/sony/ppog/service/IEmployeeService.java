package jp.co.sony.ppog.service;

import jp.co.sony.ppog.dto.EmployeeDto;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;

/**
 * 社員サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 1.00
 */
public interface IEmployeeService {

	/**
	 * ログインアカウントを重複するかどうかを確認する
	 *
	 * @param loginAccount ログインアカウント
	 */
	ResultDto<String> checkDuplicated(String loginAccount);

	/**
	 * IDによって社員情報を取得する
	 *
	 * @param id 社員ID
	 * @return Employee
	 */
	EmployeeDto getEmployeeById(Long id);

	/**
	 * キーワードによって社員情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Employee>
	 */
	Pagination<EmployeeDto> getEmployeesByKeyword(Integer pageNum, String keyword, Long userId, String authChkFlag);

	/**
	 * 社員登録
	 *
	 * @param employeeDto 社員情報転送クラス
	 * @return Boolean
	 */
	Boolean register(EmployeeDto employeeDto);

	/**
	 * 社員情報削除
	 *
	 * @param id 社員ID
	 */
	void remove(Long id);

	/**
	 * パスワードをリセットする
	 *
	 * @param employeeDto 社員情報転送クラス
	 * @return Boolean
	 */
	Boolean resetPassword(EmployeeDto employeeDto);

	/**
	 * 社員情報追加
	 *
	 * @param employeeDto 社員情報転送クラス
	 */
	void save(EmployeeDto employeeDto);

	/**
	 * 社員情報行更新
	 *
	 * @param employeeDto 社員情報転送クラス
	 */
	ResultDto<String> update(EmployeeDto employeeDto);
}
