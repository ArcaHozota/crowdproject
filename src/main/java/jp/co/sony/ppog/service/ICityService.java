package jp.co.sony.ppog.service;

import jp.co.sony.ppog.dto.CityDto;
import jp.co.sony.ppog.utils.Pagination;

/**
 * 都市サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 2.33
 */
public interface ICityService {

	/**
	 * キーワードによって都市情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Role>
	 */
	Pagination<CityDto> getCitiesByKeyword(Integer pageNum, String keyword);

	/**
	 * 都市情報追加
	 *
	 * @param cityDto 都市情報転送クラス
	 */
	void save(CityDto cityDto);
}
