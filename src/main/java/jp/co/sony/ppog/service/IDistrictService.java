package jp.co.sony.ppog.service;

import java.util.List;

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
	 * IDによって地域情報を取得する
	 *
	 * @param id 地域ID
	 * @return List<District>
	 */
	List<DistrictDto> getDistrictList(String id);

	/**
	 * キーワードによって地域情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<Role>
	 */
	Pagination<DistrictDto> getDistrictsByKeyword(Integer pageNum, String keyword);
}
