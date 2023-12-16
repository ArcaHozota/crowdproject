package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sony.ppog.entity.Role;
import jp.co.sony.ppog.entity.RoleAuth;

/**
 * 役割マッパー
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Mapper
public interface RoleMapper {

	/**
	 * IDによってバッチ削除を行う
	 *
	 * @param collect
	 */
	void batchDeleteByRoleId(@Param("roleId") Long roleId);

	/**
	 * IDによってバッチ挿入を行う
	 *
	 * @param roleAuths 役割権限連携エンティティList
	 */
	void batchInsertByIds(@Param("roleAuths") List<RoleAuth> roleAuths);

	/**
	 * 役割名称を重複するかどうかを確認する
	 *
	 * @param name 役割名称
	 * @return Integer
	 */
	Integer checkDuplicated(@Param("name") String name);

	/**
	 * 全件検索を行う
	 *
	 * @return List<Role>
	 */
	List<Role> selectAll();

	/**
	 * IDによって情報を検索する
	 *
	 * @param id 役割ID
	 * @return Role
	 */
	Role selectById(@Param("id") Long id);

	/**
	 * IDによって情報を検索する(権限情報含め)
	 *
	 * @param id 役割ID
	 * @return Role
	 */
	List<Role> selectByIdWithAuth(@Param("id") Long id);

	/**
	 * キーワードによって役割情報を検索する
	 *
	 * @param keyword キーワード
	 * @return List<Role>
	 */
	List<Role> selectByKeyword(String keyword);
}
