package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sony.ppog.entity.Role;

/**
 * 役割マッパー
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Mapper
public interface RoleMapper {

	/**
	 * 役割名称を重複するかどうかを確認する
	 *
	 * @param name 役割名称
	 * @return Integer
	 */
	Integer checkDuplicated(@Param("name") String name);

	/**
	 * キーワードによって役割情報の数を取得する
	 *
	 * @param keyword 検索キーワード
	 * @return Integer
	 */
	Long countByKeyword(@Param("keyword") String keyword);

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
	 * @param delFlg   論理削除フラグ
	 * @param offset   オフセット
	 * @param pageSize ページサイズ
	 * @return List<Role>
	 */
	List<Role> paginationByKeyword(@Param("keyword") String keyword, @Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);

	/**
	 * IDによって論理削除を行う
	 *
	 * @param role 役割エンティティ
	 */
	void removeById(Role role);

	/**
	 * 全件検索を行う
	 *
	 * @return List<Role>
	 */
	List<Role> selectAll();

	/**
	 * IDによって情報を検索する(権限情報含め)
	 *
	 * @param id 役割ID
	 * @return Role
	 */
	Role selectByIdWithAuth(@Param("id") Long id);

	/**
	 * IDによって情報を検索する(権限情報含め)
	 *
	 * @param role 役割エンティティ
	 * @return Role
	 */
	Role selectByIdWithAuth(Role role);

	/**
	 * IDによって情報を更新する
	 *
	 * @param role 役割エンティティ
	 */
	void updateById(Role role);
}
