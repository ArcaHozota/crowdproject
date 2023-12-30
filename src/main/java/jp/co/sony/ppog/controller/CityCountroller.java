package jp.co.sony.ppog.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sony.ppog.dto.CityDto;
import jp.co.sony.ppog.dto.DistrictDto;
import jp.co.sony.ppog.service.ICityService;
import jp.co.sony.ppog.service.IDistrictService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 都市コントローラ
 *
 * @author ArkamaHozota
 * @since 2.33
 */
@RestController
@RequestMapping("/pgcrowd/city")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CityCountroller {

	/**
	 * 都市サービスインターフェス
	 */
	private final ICityService iCityService;

	/**
	 * 地域サービスインターフェス
	 */
	private final IDistrictService iDistrictService;

	/**
	 * 都市名称重複チェック
	 *
	 * @param cityDto 都市情報転送クラス
	 * @return ResultDto<String>
	 */
	@PostMapping("/check")
	public ResultDto<String> checkDuplicated(@RequestBody final CityDto cityDto) {
		return this.iCityService.check(cityDto);
	}

	/**
	 * 地域情報初期表示
	 *
	 * @param cityId 都市ID
	 * @return ResultDto<String>
	 */
	@GetMapping("/districtlist")
	@PreAuthorize("hasAuthority('city%retrieve')")
	public ResultDto<List<DistrictDto>> getDistrictList(@RequestParam(value = "districtId") final String districtId) {
		final List<DistrictDto> districtDtos = this.iDistrictService.getDistrictList(districtId);
		return ResultDto.successWithData(districtDtos);
	}

	/**
	 * キーワードによってページング検索
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return ResultDto<Pagination<CityDto>>
	 */
	@GetMapping("/pagination")
	@PreAuthorize("hasAuthority('city%retrieve')")
	public ResultDto<Pagination<CityDto>> pagination(
			@RequestParam(name = "pageNum", defaultValue = "1") final Integer pageNum,
			@RequestParam(name = "keyword", defaultValue = StringUtils.EMPTY_STRING) final String keyword) {
		final Pagination<CityDto> cities = this.iCityService.getCitiesByKeyword(pageNum, keyword);
		return ResultDto.successWithData(cities);
	}

	/**
	 * 情報追加
	 *
	 * @param cityDto 都市情報DTO
	 * @return ResultDto<String>
	 */
	@PostMapping("/infosave")
	@PreAuthorize("hasAuthority('city%edition')")
	public ResultDto<String> saveInfo(@RequestBody final CityDto cityDto) {
		this.iCityService.save(cityDto);
		return ResultDto.successWithoutData();
	}
}
