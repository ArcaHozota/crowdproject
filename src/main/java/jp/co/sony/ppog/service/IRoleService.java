package jp.co.sony.ppog.service;

import java.util.List;
import java.util.Map;

import jp.co.sony.ppog.dto.RoleDto;
import jp.co.sony.ppog.entity.Authority;
import jp.co.sony.ppog.entity.Role;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;

/**
 * 役割サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 1.00
 */
public interface IRoleService {

	/**
	 * 役割名称が重複するかどうかをチェックする
	 *
	 * @param name 役割名称
	 * @return true:重複する; false: 重複しない;
	 */
	ResultDto<String> check(String name);

	/**
	 * 権限を付与する
	 *
	 * @param paramMap パラメータ
	 * @return ResultDto<String>
	 */
	ResultDto<String> doAssignment(Map<String, List<Long>> paramMap);

	/**
	 * 付与された権限を表示する
	 *
	 * @param id 役割ID
	 * @return List<Long>
	 */
	List<Long> getAuthIdsById(Long id);

	/**
	 * 権限リストを取得する
	 *
	 * @return List<PgAuth>
	 */
	List<Authority> getAuthlist();

	/**
	 * 社員役割連携情報を取得する
	 *
	 * @param employeeId 社員ID
	 * @return List<String>
	 */
	List<RoleDto> getEmployeeRolesByEmployeeId(Long employeeId);

	/**
	 * IDによって情報を取得する
	 *
	 * @param id 役割ID
	 * @return Role
	 */
	Role getRoleById(Long id);

	/**
	 * キーワードによって役割情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Role>
	 */
	Pagination<RoleDto> getRolesByKeyword(Integer pageNum, String keyword);

	/**
	 * IDによって情報を削除する
	 *
	 * @param id 役割ID
	 * @return ResultDto<String>
	 */
	ResultDto<String> removeById(Long id);

	/**
	 * 役割情報追加
	 *
	 * @param roleDto 役割情報転送クラス
	 */
	void save(RoleDto roleDto);

	/**
	 * 役割情報更新
	 *
	 * @param roleDto 役割情報転送クラス
	 */
	ResultDto<String> update(RoleDto roleDto);
}
