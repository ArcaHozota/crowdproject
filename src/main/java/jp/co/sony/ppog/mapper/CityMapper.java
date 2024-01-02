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
	 * 都市名称を重複するかどうかを確認する
	 *
	 * @param city 都市エンティティ
	 * @return Integer
	 */
	Integer checkDuplicated(City city);

	/**
	 * キーワードによって都市情報の数を取得する
	 *
	 * @param keyword 検索キーワード
	 * @param delFlg  論理削除フラグ
	 * @return Integer
	 */
	Long countByKeyword(@Param("keyword") String keyword, @Param("delFlg") String delFlg);

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
	 * @param delFlg   論理削除フラグ
	 * @param offset   オフセット
	 * @param pageSize ページサイズ
	 * @return List<Role>
	 */
	List<City> paginationByKeyword(@Param("keyword") String keyword, @Param("delFlg") String delFlg,
			@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

	/**
	 * IDによって情報を更新する
	 *
	 * @param city 都市エンティティ
	 */
	void updateById(City city);
}
