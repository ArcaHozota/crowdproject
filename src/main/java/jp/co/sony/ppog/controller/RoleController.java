package jp.co.sony.ppog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sony.ppog.dto.RoleDto;
import jp.co.sony.ppog.entity.Authority;
import jp.co.sony.ppog.service.IRoleService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 役割コントローラ
 *
 * @author ArkamaHozota
 * @since 1.16
 */
@RestController
@RequestMapping("/pgcrowd/role")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleController {

	/**
	 * 役割サービスインターフェス
	 */
	private final IRoleService iRoleService;

	/**
	 * 権限付与モダルを初期表示する
	 *
	 * @return ResultDto<List<Authority>>
	 */
	@GetMapping("/authlists")
	public ResultDto<List<Authority>> authlists() {
		final List<Authority> list = this.iRoleService.getAuthlist();
		return ResultDto.successWithData(list);
	}

	/**
	 * 役割名称重複チェック
	 *
	 * @param name 役割名称
	 * @return ResultDto<String>
	 */
	@GetMapping("/checkname")
	public ResultDto<String> checkDuplicated(
			@RequestParam(name = "name", defaultValue = StringUtils.EMPTY_STRING) final String name) {
		return this.iRoleService.check(name);
	}

	/**
	 * 役割情報削除
	 *
	 * @param roleId 役割ID
	 * @return ResultDto<String>
	 */
	@DeleteMapping("/delete/{roleId}")
	@PreAuthorize("hasAuthority('role%delete')")
	public ResultDto<String> deleteInfo(@PathVariable("roleId") final Long roleId) {
		return this.iRoleService.removeById(roleId);
	}

	/**
	 * 権限付与実行
	 *
	 * @param paramMap パラメータ
	 * @return ResultDto<String>
	 */
	@PutMapping("/do/assignment")
	@PreAuthorize("hasAuthority('role%delete')")
	public ResultDto<String> doAssignment(@RequestBody final Map<String, List<Long>> paramMap) {
		return this.iRoleService.doAssignment(paramMap);
	}

	/**
	 * 付与された権限を表示する
	 *
	 * @return ResultDto<List<Long>>
	 */
	@GetMapping("/getAssigned")
	@PreAuthorize("hasAuthority('role%retrieve')")
	public ResultDto<List<Long>> getAssignedAuth(@RequestParam("fuyoId") final Long roleId) {
		final List<Long> authIds = this.iRoleService.getAuthIdListByRoleId(roleId);
		return ResultDto.successWithData(authIds);
	}

	/**
	 * キーワードによってページング検索
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return ResultDto<Pagination<RoleDto>>
	 */
	@GetMapping("/pagination")
	@PreAuthorize("hasAuthority('role%retrieve')")
	public ResultDto<Pagination<RoleDto>> pagination(
			@RequestParam(name = "pageNum", defaultValue = "1") final Integer pageNum,
			@RequestParam(name = "keyword", defaultValue = StringUtils.EMPTY_STRING) final String keyword) {
		final Pagination<RoleDto> roles = this.iRoleService.getRolesByKeyword(pageNum, keyword);
		return ResultDto.successWithData(roles);
	}

	/**
	 * 情報追加
	 *
	 * @param roleDto 役割情報DTO
	 * @return ResultDto<String>
	 */
	@PostMapping("/infosave")
	@PreAuthorize("hasAuthority('role%edition')")
	public ResultDto<String> saveInfo(@RequestBody final RoleDto roleDto) {
		this.iRoleService.save(roleDto);
		return ResultDto.successWithoutData();
	}

	/**
	 * 情報更新
	 *
	 * @param roleDto 役割情報DTO
	 * @return ResultDto<String>
	 */
	@PutMapping("/infoupd")
	@PreAuthorize("hasAuthority('role%edition')")
	public ResultDto<String> updateInfo(@RequestBody final RoleDto roleDto) {
		return this.iRoleService.update(roleDto);
	}
}
