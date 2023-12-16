package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sony.ppog.entity.Employee;

/**
 * 社員マッパー
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Mapper
public interface EmployeeMapper {

	/**
	 * ログインアカウントを重複するかどうかを確認する
	 *
	 * @param loginAccount ログインアカウント
	 */
	Integer checkDuplicated(@Param("loginAccount") String loginAccount);

	/**
	 * IDによって情報を挿入する
	 *
	 * @param employee 社員エンティティ
	 */
	void insertById(Employee employee);

	/**
	 * IDによって論理削除を行う
	 *
	 * @param id 社員ID
	 */
	void removeById(@Param("id") Long id);

	/**
	 * ID採番値を取得する
	 *
	 * @return Long
	 */
	Long saiban();

	/**
	 * IDによって情報を検索する
	 *
	 * @param id 社員ID
	 * @return Employee
	 */
	Employee selectById(@Param("id") Long id);

	/**
	 * キーワードによって社員情報を検索する
	 *
	 * @param keyword 検索キーワード
	 * @return List<Employee>
	 */
	List<Employee> selectByKeyword(@Param("keyword") String keyword);

	/**
	 * IDによって情報を更新する
	 *
	 * @param employee 社員エンティティ
	 */
	void updateById(Employee employee);
}
