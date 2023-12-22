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
	 * IDによって情報を挿入する
	 *
	 * @param role 役割エンティティ
	 */
	void insertById(Role role);

	/**
	 * キーワードによって役割情報を検索する
	 *
	 * @param keyword  キーワード
	 * @param offset   オフセット
	 * @param pageSize ページサイズ
	 * @return List<Role>
	 */
	List<Role> paginationByKeyword(@Param("keyword") String keyword, @Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);

	/**
	 * IDによって論理削除を行う
	 *
	 * @param id 役割ID
	 */
	void removeById(@Param("id") Long id);

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
	Role selectByIdWithAuth(@Param("id") Long id);

	/**
	 * IDによって情報を更新する
	 *
	 * @param role 役割エンティティ
	 */
	void updateById(Role role);
}
