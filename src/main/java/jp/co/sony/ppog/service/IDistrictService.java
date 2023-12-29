package jp.co.sony.ppog.service;

import jp.co.sony.ppog.dto.DistrictDto;
import jp.co.sony.ppog.utils.Pagination;

/**
 * 地域サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 2.26
 */
public interface IDistrictService {

	/**
	 * キーワードによって地域情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Role>
	 */
	Pagination<DistrictDto> getDistrictsByKeyword(Integer pageNum, String keyword);
}
