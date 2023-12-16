package jp.co.sony.ppog.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.stereotype.Service;

import apple.laf.JRSUIConstants.Direction;
import jp.co.sony.ppog.dto.EmployeeDto;
import jp.co.sony.ppog.entity.Employee;
import jp.co.sony.ppog.mapper.EmployeeMapper;
import jp.co.sony.ppog.mapper.EmployeeRoleMapper;
import jp.co.sony.ppog.service.IEmployeeService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.SecondBeanUtils;
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
		final Employee employee = new Employee();
		employee.setLoginAccount(loginAccount);
		final Example<Employee> example = Example.of(employee, ExampleMatcher.matching());
		return this.employeeRepository.findOne(example).isPresent()
				? ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public Employee getAdminByLoginAccount(final String account, final String password) {
		final String plainToMD5 = PgCrowdUtils.plainToMD5(password);
		final Employee employee = new Employee();
		employee.setLoginAccount(account);
		employee.setPassword(plainToMD5);
		final Example<Employee> example = Example.of(employee, ExampleMatcher.matchingAll());
		return this.employeeRepository.findOne(example).orElseThrow(() -> {
			throw new LoginFailedException(PgCrowdConstants.MESSAGE_STRING_PROHIBITED);
		});
	}

	@Override
	public Employee getEmployeeById(final Long id) {
		return this.employeeRepository.findById(id).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_PROHIBITED);
		});
	}

	@Override
	public List<Role> getEmployeeRolesById(final Long id) {
		final List<Role> secondRoles = new ArrayList<>();
		final Role secondRole = new Role();
		secondRole.setId(0L);
		secondRole.setName(PgCrowdConstants.DEFAULT_ROLE_NAME);
		final Specification<Role> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Specification<Role> specification1 = Specification.where(where1);
		final List<Role> roles = this.roleRepository.findAll(specification1);
		if (id == null) {
			secondRoles.add(secondRole);
			secondRoles.addAll(roles);
			return secondRoles;
		}
		final Optional<EmployeeEx> roledOptional = this.employeeExRepository.findById(id);
		if (roledOptional.isEmpty()) {
			secondRoles.add(secondRole);
			secondRoles.addAll(roles);
			return secondRoles;
		}
		final Long roleId = roledOptional.get().getRoleId();
		final List<Role> selectedRole = roles.stream().filter(a -> Objects.equals(a.getId(), roleId))
				.collect(Collectors.toList());
		secondRoles.addAll(selectedRole);
		secondRoles.addAll(roles);
		return secondRoles.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<Employee> getEmployeesByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final String searchStr = "%" + keyword + "%";
		final Specification<Employee> status = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("status"), PgCrowdConstants.EMPLOYEE_NORMAL_STATUS);
		final Specification<Employee> where1 = StringUtils.isEmpty(keyword) ? null
				: (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("loginAccount"), searchStr);
		final Specification<Employee> where2 = StringUtils.isEmpty(keyword) ? null
				: (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), searchStr);
		final Specification<Employee> where3 = StringUtils.isEmpty(keyword) ? null
				: (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), searchStr);
		final Specification<Employee> specification = Specification.where(status)
				.and(Specification.where(where1).or(where2).or(where3));
		final Page<Employee> pages = this.employeeRepository.findAll(specification, pageRequest);
		return Pagination.of(pages.getContent(), pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public void removeById(final Long userId) {
		final Employee employee = this.employeeRepository.findById(userId).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_PROHIBITED);
		});
		employee.setStatus(PgCrowdConstants.EMPLOYEE_ABNORMAL_STATUS);
		this.employeeRepository.saveAndFlush(employee);
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final Long saibanId = this.employeeRepository.saiban();
		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2A, 7);
		final String password = encoder.encode(employeeDto.getPassword());
		final Employee employee = new Employee();
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		employee.setId(saibanId);
		employee.setPassword(password);
		employee.setStatus(PgCrowdConstants.EMPLOYEE_NORMAL_STATUS);
		employee.setCreatedTime(LocalDateTime.now());
		if (employeeDto.getRoleId() != null && !Objects.equals(Long.valueOf(0L), employeeDto.getRoleId())) {
			final EmployeeEx employeeEx = new EmployeeEx();
			employeeEx.setEmployeeId(employeeDto.getId());
			employeeEx.setRoleId(employeeDto.getRoleId());
			this.employeeExRepository.saveAndFlush(employeeEx);
		}
		this.employeeRepository.saveAndFlush(employee);
	}

	@Override
	public void update(final EmployeeDto employeeDto) {
		final Employee employee = this.employeeRepository.findById(employeeDto.getId()).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_PROHIBITED);
		});
		if (!Objects.equals(employeeDto.getRoleId(), 0L)) {
			this.employeeExRepository.findById(employeeDto.getId()).ifPresentOrElse(value -> {
				value.setRoleId(employeeDto.getRoleId());
				this.employeeExRepository.saveAndFlush(value);
			}, () -> {
				final EmployeeEx employeeEx = new EmployeeEx();
				employeeEx.setEmployeeId(employeeDto.getId());
				employeeEx.setRoleId(employeeDto.getRoleId());
				this.employeeExRepository.saveAndFlush(employeeEx);
			});
		}
		SecondBeanUtils.copyNullableProperties(employeeDto, employee);
		if (StringUtils.isNotEmpty(employeeDto.getPassword())) {
			final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2A, 7);
			final String encoded = encoder.encode(employeeDto.getPassword());
			employee.setPassword(encoded);
		}
		this.employeeRepository.saveAndFlush(employee);
	}
}
