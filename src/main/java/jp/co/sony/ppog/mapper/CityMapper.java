package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sony.ppog.entity.City;

/**
 * 都市マッパー
 *
 * @author ArkamaHozota
 * @since 2.40
 */
@Mapper
public interface CityMapper {

	/**
	 * キーワードによって都市情報の数を取得する
	 *
	 * @param keyword 検索キーワード
	 * @return Integer
	 */
	Long countByKeyword(String searchStr);

	/**
	 * IDによって情報を挿入する
	 *
	 * @param city 都市エンティティ
	 */
	void insertById(City city);

	/**
	 * キーワードによって都市情報を検索する
	 *
	 * @param keyword  キーワード
	 * @param offset   オフセット
	 * @param pageSize ページサイズ
	 * @return List<Role>
	 */
	List<City> paginationByKeyword(@Param("keyword") String keyword, @Param("offset") Integer offset,
			@Param("pageSize") Integer pageSize);
}
