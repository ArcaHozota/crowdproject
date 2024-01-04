package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sony.ppog.entity.District;

/**
 * 地域マッパー
 *
 * @author ArkamaHozota
 * @since 2.29
 */
@Mapper
public interface DistrictMapper {

	/**
	 * キーワードによって地域情報の数を取得する
	 *
	 * @param keyword 検索キーワード
	 * @param delFlg  論理削除フラグ
	 * @return Long
	 */
	Long countByKeyword(@Param("keyword") String keyword, @Param("delFlg") String delFlg);

	/**
	 * キーワードによって地域情報を検索する
	 *
	 * @param keyword  キーワード
	 * @param delFlg   論理削除フラグ
	 * @param offset   オフセット
	 * @param pageSize ページサイズ
	 * @return List<Role>
	 */
	List<District> paginationByKeyword(@Param("keyword") String keyword, @Param("delFlg") String delFlg,
			@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

	/**
	 * 全件検索を行う
	 *
	 * @param delFlg 論理削除フラグ
	 * @return List<District>
	 */
	List<District> selectAll(@Param("delFlg") String delFlg);

	/**
	 * IDによって情報を更新する
	 *
	 * @param district 地域エンティティ
	 */
	void updateById(District district);
}
