package jp.co.sony.ppog.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sony.ppog.commons.CrowdProjectConstants;
import jp.co.sony.ppog.config.CrowdProjectPasswordEncoder;
import jp.co.sony.ppog.dto.EmployeeDto;
import jp.co.sony.ppog.entity.Employee;
import jp.co.sony.ppog.entity.EmployeeRole;
import jp.co.sony.ppog.mapper.EmployeeMapper;
import jp.co.sony.ppog.mapper.EmployeeRoleMapper;
import jp.co.sony.ppog.service.IEmployeeService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.SecondBeanUtils;
import jp.co.sony.ppog.utils.SnowflakeUtils;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleSQLException;

/**
 * 社員サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(rollbackFor = OracleSQLException.class)
public class EmployeeServiceImpl implements IEmployeeService {

	/**
	 * 日時フォマーター
	 */
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * Randomナンバー
	 */
	private static final Random RANDOM = new Random();

	/**
	 * 社員管理マッパー
	 */
	private final EmployeeMapper employeeMapper;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleMapper employeeRoleMapper;

	/**
	 * パスワードエンコーダ
	 */
	private final CrowdProjectPasswordEncoder passwordEncoder = new CrowdProjectPasswordEncoder();

	@Override
	public ResultDto<String> checkDuplicated(final String loginAccount) {
		return this.employeeMapper.checkDuplicated(loginAccount) > 0
				? ResultDto.failed(CrowdProjectConstants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public EmployeeDto getEmployeeById(final Long id) {
		final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(id);
		final Employee employee = this.employeeMapper.selectById(id);
		final EmployeeDto employeeDto = new EmployeeDto();
		SecondBeanUtils.copyNullableProperties(employee, employeeDto);
		employeeDto.setPassword(CrowdProjectConstants.DEFAULT_ROLE_NAME);
		employeeDto.setDateOfBirth(employee.getDateOfBirth().format(DATE_TIME_FORMATTER));
		employeeDto.setRoleId(employeeRole.getRoleId());
		return employeeDto;
	}

	@Override
	public Pagination<EmployeeDto> getEmployeesByKeyword(final Integer pageNum, final String keyword, final Long userId,
			final String authChkFlag) {
		final Integer pageSize = CrowdProjectConstants.DEFAULT_PAGE_SIZE;
		if (Boolean.FALSE.equals(Boolean.valueOf(authChkFlag))) {
			final Employee employee = this.employeeMapper.selectById(userId);
			final EmployeeDto employeeDto = new EmployeeDto();
			SecondBeanUtils.copyNullableProperties(employee, employeeDto);
			employeeDto.setDateOfBirth(employee.getDateOfBirth().format(DATE_TIME_FORMATTER));
			final List<EmployeeDto> dtoList = new ArrayList<>();
			dtoList.add(employeeDto);
			return Pagination.of(dtoList, dtoList.size(), pageNum, pageSize);
		}
		final Integer offset = (pageNum - 1) * pageSize;
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		final Long records = this.employeeMapper.countByKeyword(searchStr);
		final List<EmployeeDto> pages = this.employeeMapper.paginationByKeyword(searchStr, offset, pageSize).stream()
				.map(item -> {
					final EmployeeDto employeeDto = new EmployeeDto();
					SecondBeanUtils.copyNullableProperties(item, employeeDto);
					employeeDto.setDateOfBirth(item.getDateOfBirth().format(DATE_TIME_FORMATTER));
					return employeeDto;
				}).collect(Collectors.toList());
		return Pagination.of(pages, records, pageNum, pageSize);
	}

	/**
	 * ランダムのストリングを生成する
	 *
	 * @return ランダムストリング
	 */
	private String getRandomStr() {
		final String stry = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final char[] cr1 = stry.toCharArray();
		final char[] cr2 = stry.toLowerCase().toCharArray();
		final StringBuilder builder = new StringBuilder();
		builder.append(cr1[RANDOM.nextInt(cr1.length)]);
		for (int i = 0; i < 7; i++) {
			builder.append(cr2[RANDOM.nextInt(cr2.length)]);
		}
		return builder.toString();
	}

	@Override
	public Boolean register(final EmployeeDto employeeDto) {
		final Employee selectByLoginAcct = this.employeeMapper.selectByLoginAcct(employeeDto.getEmail());
		if (selectByLoginAcct != null) {
			return Boolean.FALSE;
		}
		final String password = this.passwordEncoder.encode(employeeDto.getPassword());
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(SnowflakeUtils.snowflakeId());
		employee.setLoginAccount(this.getRandomStr());
		employee.setPassword(password);
		employee.setDelFlg(CrowdProjectConstants.LOGIC_DELETE_INITIAL);
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), EmployeeServiceImpl.DATE_TIME_FORMATTER));
		this.employeeMapper.insertById(employee);
		return Boolean.TRUE;
	}

	@Override
	public void remove(final Long id) {
		final Employee entity = new Employee();
		entity.setId(id);
		entity.setDelFlg(CrowdProjectConstants.LOGIC_DELETE_FLG);
		this.employeeMapper.removeById(entity);
	}

	@Override
	public Boolean resetPassword(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		final Employee selectByAccountAndEmail = this.employeeMapper.selectByAccountAndEmail(employee);
		if (selectByAccountAndEmail == null) {
			return Boolean.FALSE;
		}
		selectByAccountAndEmail.setPassword(this.passwordEncoder.encode(CrowdProjectConstants.DEFAULT_PASSWORD));
		this.employeeMapper.insertById(selectByAccountAndEmail);
		return Boolean.TRUE;
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final Employee employee = new Employee();
		final String password = this.passwordEncoder.encode(employeeDto.getPassword());
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(SnowflakeUtils.snowflakeId());
		employee.setPassword(password);
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), EmployeeServiceImpl.DATE_TIME_FORMATTER));
		employee.setCreatedTime(LocalDateTime.now());
		employee.setDelFlg(CrowdProjectConstants.LOGIC_DELETE_INITIAL);
		this.employeeMapper.insertById(employee);
		if ((employeeDto.getRoleId() != null) && !Objects.equals(Long.valueOf(0L), employeeDto.getRoleId())) {
			final EmployeeRole employeeEx = new EmployeeRole();
			employeeEx.setEmployeeId(employee.getId());
			employeeEx.setRoleId(employeeDto.getRoleId());
			this.employeeRoleMapper.insertById(employeeEx);
		}
	}

	@Override
	public ResultDto<String> update(final EmployeeDto employeeDto) {
		final Employee originalEntity = new Employee();
		final Employee employee = this.employeeMapper.selectById(employeeDto.getId());
		SecondBeanUtils.copyNullableProperties(employee, originalEntity);
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), EmployeeServiceImpl.DATE_TIME_FORMATTER));
		if (StringUtils.isNotEmpty(employeeDto.getPassword())) {
			final String encoded = this.passwordEncoder.encode(employeeDto.getPassword());
			employee.setPassword(encoded);
		}
		final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(employeeDto.getId());
		if (originalEntity.equals(employee) && Objects.equals(employeeDto.getRoleId(), employeeRole.getRoleId())) {
			return ResultDto.failed(CrowdProjectConstants.MESSAGE_STRING_NOCHANGE);
		}
		employeeRole.setRoleId(employeeDto.getRoleId());
		this.employeeRoleMapper.updateById(employeeRole);
		this.employeeMapper.updateById(employee);
		return ResultDto.successWithoutData();
	}
}
