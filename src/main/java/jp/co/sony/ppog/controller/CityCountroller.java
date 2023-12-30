package jp.co.sony.ppog.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jp.co.sony.ppog.dto.CityDto;
import jp.co.sony.ppog.entity.District;
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
@Controller
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
	 * 地域情報初期表示
	 *
	 * @param cityId 都市ID
	 * @return ResultDto<String>
	 */
	@GetMapping("/districtlist")
	@PreAuthorize("hasAuthority('city%retrieve')")
	public ModelAndView getDistrictList(
			@RequestParam(value = "districtId", defaultValue = StringUtils.EMPTY_STRING) final String districtId) {
		final ModelAndView modelAndView = new ModelAndView("city-pages");
		final List<District> districts = this.iDistrictService.getDistrictList(districtId);
		modelAndView.addObject("districts", districts);
		return modelAndView;
	}

	/**
	 * キーワードによってページング検索
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return ResultDto<Pagination<CityDto>>
	 */
	@GetMapping("/pagination")
	@ResponseBody
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
	@ResponseBody
	@PreAuthorize("hasAuthority('city%edition')")
	public ResultDto<String> saveInfo(@RequestBody final CityDto cityDto) {
		this.iCityService.save(cityDto);
		return ResultDto.successWithoutData();
	}
}
