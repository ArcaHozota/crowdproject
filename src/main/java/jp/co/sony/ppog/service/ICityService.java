package jp.co.sony.ppog.service;

import jp.co.sony.ppog.dto.CityDto;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;

/**
 * 都市サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 2.33
 */
public interface ICityService {

	/**
	 * 都市名称が重複するかどうかをチェックする
	 *
	 * @param name       都市名称
	 * @param districtId 地域ID
	 * @return true:重複する; false: 重複しない;
	 */
	ResultDto<String> check(String name, Long districtId);

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

	/**
	 * 都市情報更新
	 *
	 * @param cityDto 都市情報転送クラス
	 */
	void update(CityDto cityDto);
}
