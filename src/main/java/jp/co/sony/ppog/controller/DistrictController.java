package jp.co.sony.ppog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sony.ppog.dto.DistrictDto;
import jp.co.sony.ppog.service.IDistrictService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 地域コントローラ
 *
 * @author ArkamaHozota
 * @since 2.25
 */
@RestController
@RequestMapping("/pgcrowd/district")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DistrictController {

	/**
	 * 地域サービスインターフェス
	 */
	private final IDistrictService iDistrictService;

	/**
	 * キーワードによってページング検索
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return ResultDto<Pagination<DistrictDto>>
	 */
	@GetMapping("/pagination")
	@PreAuthorize("hasAuthority('district%retrieve')")
	public ResultDto<Pagination<DistrictDto>> pagination(
			@RequestParam(name = "pageNum", defaultValue = "1") final Integer pageNum,
			@RequestParam(name = "keyword", defaultValue = StringUtils.EMPTY_STRING) final String keyword) {
		final Pagination<DistrictDto> districts = this.iDistrictService.getDistrictsByKeyword(pageNum, keyword);
		return ResultDto.successWithData(districts);
	}

	/**
	 * 情報更新
	 *
	 * @param districtDto 地域情報転送クラス
	 * @return ResultDto<String>
	 */
	@PutMapping("/infoupd")
	@PreAuthorize("hasAuthority('district%edition')")
	public ResultDto<String> updateInfo(@RequestBody final DistrictDto districtDto) {
		return this.iDistrictService.update(districtDto);
	}
}
