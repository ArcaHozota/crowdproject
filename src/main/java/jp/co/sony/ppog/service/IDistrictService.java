package jp.co.sony.ppog.service;

import java.util.List;

import jp.co.sony.ppog.dto.DistrictDto;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;

/**
 * 地域サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 2.26
 */
public interface IDistrictService {

	/**
	 * IDによって地域情報を取得する
	 *
	 * @param cityId 都市ID
	 * @return List<District>
	 */
	List<DistrictDto> getDistrictsByCityId(String cityId);

	/**
	 * キーワードによって地域情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Role>
	 */
	Pagination<DistrictDto> getDistrictsByKeyword(Integer pageNum, String keyword);

	/**
	 * 地域情報更新
	 *
	 * @param districtDto 地域情報転送クラス
	 */
	ResultDto<String> update(DistrictDto districtDto);
}
