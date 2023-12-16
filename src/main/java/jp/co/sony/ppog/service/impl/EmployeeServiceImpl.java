package jp.co.sony.ppog.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.stereotype.Service;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.dto.EmployeeDto;
import jp.co.sony.ppog.entity.Employee;
import jp.co.sony.ppog.entity.EmployeeRole;
import jp.co.sony.ppog.mapper.EmployeeMapper;
import jp.co.sony.ppog.mapper.EmployeeRoleMapper;
import jp.co.sony.ppog.service.IEmployeeService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.SecondBeanUtils;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 社員サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeServiceImpl implements IEmployeeService {

	/**
	 * 社員管理マッパー
	 */
	private final EmployeeMapper employeeMapper;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleMapper employeeRoleMapper;

	@Override
	public ResultDto<String> check(final String loginAccount) {
		final Integer checkInteger = this.employeeMapper.checkDuplicated(loginAccount);
		return checkInteger > 0 ? ResultDto.failed(CrowdPlusConstants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public Employee getEmployeeById(final Long id) {
		return this.employeeMapper.selectById(id);
	}

	@Override
	public Pagination<Employee> getEmployeesByKeyword(final Integer pageNum, final String keyword) {
		final String searchString = "%" + keyword + "%";
		final List<Employee> pages = this.employeeMapper.selectByKeyword(searchString);
		return Pagination.of(pages, pages.size(), pageNum, CrowdPlusConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public void removeById(final Long userId) {
		this.employeeMapper.removeById(userId);
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		final Long saibanId = this.employeeMapper.saiban();
		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2Y, 7);
		final String password = encoder.encode(employeeDto.getPassword());
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(saibanId);
		employee.setPassword(password);
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDelFlg(CrowdPlusConstants.LOGIC_DELETE_INITIAL);
		if ((employeeDto.getRoleId() != null) && !Objects.equals(Long.valueOf(0L), employeeDto.getRoleId())) {
			final EmployeeRole employeeEx = new EmployeeRole();
			employeeEx.setEmployeeId(employeeDto.getId());
			employeeEx.setRoleId(employeeDto.getRoleId());
			this.employeeRoleMapper.insertById(employeeEx);
		}
		this.employeeMapper.insertById(employee);
	}

	@Override
	public void update(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		if (StringUtils.isNotEmpty(employeeDto.getPassword())) {
			final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2Y, 7);
			final String encoded = encoder.encode(employeeDto.getPassword());
			employee.setPassword(encoded);
		}
		if ((employeeDto.getRoleId() != null) && !Objects.equals(Long.valueOf(0L), employeeDto.getRoleId())) {
			final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(employeeDto.getId());
			if (!Objects.equals(employeeRole.getRoleId(), employeeDto.getRoleId())) {
				employeeRole.setRoleId(employeeDto.getRoleId());
				this.employeeRoleMapper.updateById(employeeRole);
			}
		}
		this.employeeMapper.updateById(employee);
	}
}
