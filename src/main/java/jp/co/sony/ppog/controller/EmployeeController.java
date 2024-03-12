package jp.co.sony.ppog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jp.co.sony.ppog.commons.CrowdProjectConstants;
import jp.co.sony.ppog.dto.EmployeeDto;
import jp.co.sony.ppog.dto.RoleDto;
import jp.co.sony.ppog.entity.Role;
import jp.co.sony.ppog.service.IEmployeeService;
import jp.co.sony.ppog.service.IRoleService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 社員コントローラ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Controller
@RequestMapping("/pgcrowd/employee")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeController {

	/**
	 * 社員サービスインターフェス
	 */
	private final IEmployeeService iEmployeeService;

	/**
	 * 役割サービスインターフェス
	 */
	private final IRoleService iRoleService;

	/**
	 * 削除権限チェック
	 *
	 * @return ResultDto<String>
	 */
	@GetMapping("/checkDelete")
	public ResultDto<String> checkDelete() {
		return ResultDto.successWithoutData();
	}

	/**
	 * ログインアカウントを重複するかどうかを確認する
	 *
	 * @param loginAccount ログインアカウント
	 * @return ResultDto<String>
	 */
	@GetMapping("/check")
	@ResponseBody
	public ResultDto<String> checkDuplicated(@RequestParam("loginAcct") final String loginAccount) {
		return this.iEmployeeService.check(loginAccount);
	}

	/**
	 * IDによって社員情報を削除する
	 *
	 * @param userId 社員ID
	 * @return ResultDto<String>
	 */
	@DeleteMapping("/delete/{userId}")
	@ResponseBody
	public ResultDto<String> deleteInfo(@PathVariable("userId") final Long userId) {
		this.iEmployeeService.removeById(userId);
		return ResultDto.successWithoutData();
	}

	/**
	 * キーワードによってページング検索
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return ResultDto<Pagination<Employee>>
	 */
	@GetMapping("/pagination")
	@ResponseBody
	public ResultDto<Pagination<EmployeeDto>> pagination(
			@RequestParam(name = "pageNum", defaultValue = "1") final Integer pageNum,
			@RequestParam(name = "keyword", defaultValue = StringUtils.EMPTY_STRING) final String keyword,
			@RequestParam(name = "userId") final Long userId) {
		final Pagination<EmployeeDto> employees = this.iEmployeeService.getEmployeesByKeyword(pageNum, keyword, userId);
		return ResultDto.successWithData(employees);
	}

	/**
	 * IDによって社員情報を復元する
	 *
	 * @param editId 編集されるユーザID
	 * @return ResultDto<String>
	 */
	@GetMapping("/inforestore")
	@ResponseBody
	public ResultDto<EmployeeDto> restoreInfo(@RequestParam("editId") final Long editId) {
		final EmployeeDto employee = this.iEmployeeService.getEmployeeById(editId);
		return ResultDto.successWithData(employee);
	}

	/**
	 * 情報追加
	 *
	 * @param employeeDto 社員情報DTO
	 * @return ResultDto<String>
	 */
	@PostMapping("/infosave")
	@ResponseBody
	public ResultDto<String> saveInfo(@RequestBody final EmployeeDto employeeDto) {
		this.iEmployeeService.save(employeeDto);
		return ResultDto.successWithoutData();
	}

	/**
	 * 情報追加初期表示
	 *
	 * @param userId ユーザID
	 * @return ModelAndView
	 */
	@GetMapping("/to/addition")
	public ModelAndView toAddition() {
		final List<RoleDto> employeeRolesById = this.iRoleService.getEmployeeRolesByEmployeeId(null);
		final ModelAndView modelAndView = new ModelAndView("admin-addinfo");
		modelAndView.addObject(CrowdProjectConstants.ATTRNAME_EMPLOYEEROLES, employeeRolesById);
		return modelAndView;
	}

	/**
	 * 情報更新初期表示
	 *
	 * @param id 社員ID
	 * @return ModelAndView
	 */
	@GetMapping("/to/edition")
	public ModelAndView toEdition(@RequestParam("editId") final Long editId,
			@RequestParam("userId") final Long userId) {
		final EmployeeDto employee = this.iEmployeeService.getEmployeeById(userId);
		if (Boolean.FALSE.equals(employee.getCheckFlg())) {
			final ModelAndView modelAndView = new ModelAndView("admin-editinfo2");
			modelAndView.addObject(CrowdProjectConstants.ATTRNAME_EDITED_INFO, employee);
			final Role role = this.iRoleService.getRoleById(employee.getRoleId());
			modelAndView.addObject(CrowdProjectConstants.ATTRNAME_EMPLOYEEROLES, role);
			return modelAndView;
		}
		final List<RoleDto> employeeRolesById = this.iRoleService.getEmployeeRolesByEmployeeId(editId);
		final EmployeeDto employee2 = this.iEmployeeService.getEmployeeById(editId);
		final ModelAndView modelAndView = new ModelAndView("admin-editinfo");
		modelAndView.addObject(CrowdProjectConstants.ATTRNAME_EDITED_INFO, employee2);
		modelAndView.addObject(CrowdProjectConstants.ATTRNAME_EMPLOYEEROLES, employeeRolesById);
		return modelAndView;
	}

	/**
	 * 情報更新
	 *
	 * @param employeeDto 社員情報DTO
	 * @return ResultDto<String>
	 */
	@PutMapping("/infoupd")
	@ResponseBody
	public ResultDto<String> updateInfo(@RequestBody final EmployeeDto employeeDto) {
		return this.iEmployeeService.update(employeeDto);
	}
}
