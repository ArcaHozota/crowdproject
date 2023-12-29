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
	 * @return Integer
	 */
	Long countByKeyword(String searchStr);

	/**
	 * キーワードによって地域情報を検索する
	 *
	 * @param keyword  キーワード
	 * @param offset   オフセット
	 * @param pageSize ページサイズ
	 * @return List<Role>
	 */
	List<District> paginationByKeyword(@Param("keyword") String keyword, @Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);
}
