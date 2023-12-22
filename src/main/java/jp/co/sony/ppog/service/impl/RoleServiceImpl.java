package jp.co.sony.ppog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.dto.RoleDto;
import jp.co.sony.ppog.entity.Authority;
import jp.co.sony.ppog.entity.EmployeeRole;
import jp.co.sony.ppog.entity.Role;
import jp.co.sony.ppog.entity.RoleAuth;
import jp.co.sony.ppog.mapper.AuthorityMapper;
import jp.co.sony.ppog.mapper.EmployeeRoleMapper;
import jp.co.sony.ppog.mapper.RoleMapper;
import jp.co.sony.ppog.service.IRoleService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
import jp.co.sony.ppog.utils.SecondBeanUtils;
import jp.co.sony.ppog.utils.SnowflakeUtils;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleSQLException;

/**
 * 役割サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(rollbackFor = OracleSQLException.class)
public class RoleServiceImpl implements IRoleService {

	/**
	 * 役割マッパー
	 */
	private final RoleMapper roleMapper;

	/**
	 * 権限マッパー
	 */
	private final AuthorityMapper authorityMapper;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleMapper employeeRoleMapper;

	@Override
	public ResultDto<String> check(final String name) {
		return this.roleMapper.checkDuplicated(name) > 0
				? ResultDto.failed(CrowdPlusConstants.MESSAGE_ROLE_NAME_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public ResultDto<String> doAssignment(final Map<String, List<Long>> paramMap) {
		final Long roleId = paramMap.get("roleId").get(0);
		this.roleMapper.batchDeleteByRoleId(roleId);
		final List<Long> authIds = paramMap.get("authIdArray");
		final List<RoleAuth> list = authIds.stream().filter(a -> (!Objects.equals(a, 1L) && !Objects.equals(a, 5L)))
				.map(item -> {
					final RoleAuth roleEx = new RoleAuth();
					roleEx.setAuthId(item);
					roleEx.setRoleId(roleId);
					return roleEx;
				}).collect(Collectors.toList());
		try {
			this.roleMapper.batchInsertByIds(list);
		} catch (final Exception e) {
			return ResultDto.failed(CrowdPlusConstants.MESSAGE_STRING_FORBIDDEN2);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public List<Long> getAuthIdListByRoleId(final Long roleId) {
		final Role role = this.roleMapper.selectByIdWithAuth(roleId);
		return role.getRoleAuths().stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
	}

	@Override
	public List<Authority> getAuthlist() {
		return this.authorityMapper.selectAll();
	}

	@Override
	public List<Role> getEmployeeRolesById(final Long id) {
		final List<Role> secondRoles = new ArrayList<>();
		final Role secondRole = new Role();
		secondRole.setId(0L);
		secondRole.setName(CrowdPlusConstants.DEFAULT_ROLE_NAME);
		final List<Role> roles = this.roleMapper.selectAll();
		secondRoles.add(secondRole);
		secondRoles.addAll(roles);
		if (id == null) {
			return secondRoles;
		}
		final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(id);
		if (employeeRole == null) {
			return secondRoles;
		}
		secondRoles.clear();
		final Long roleId = employeeRole.getRoleId();
		final List<Role> selectedRole = roles.stream().filter(a -> Objects.equals(a.getId(), roleId))
				.collect(Collectors.toList());
		secondRoles.addAll(selectedRole);
		secondRoles.addAll(roles);
		return secondRoles.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Role getRoleById(final Long roleId) {
		return this.roleMapper.selectById(roleId);
	}

	@Override
	public Pagination<Role> getRolesByKeyword(final Integer pageNum, final String keyword) {
		if (StringUtils.isDigital(keyword)) {
			final List<Role> pages = this.roleMapper.selectByKeyword(keyword);
			return Pagination.of(pages, pages.size(), pageNum, CrowdPlusConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		final List<Role> pages = this.roleMapper.selectByKeyword(searchStr);
		return Pagination.of(pages, pages.size(), pageNum, CrowdPlusConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> removeById(final Long roleId) {
		final List<EmployeeRole> list = this.employeeRoleMapper.selectByRoleId(roleId);
		if (!list.isEmpty()) {
			return ResultDto.failed(CrowdPlusConstants.MESSAGE_STRING_FORBIDDEN);
		}
		this.roleMapper.removeById(roleId);
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final RoleDto roleDto) {
		final Role role = new Role();
		SecondBeanUtils.copyNullableProperties(roleDto, role);
		role.setId(SnowflakeUtils.snowflakeId());
		role.setDelFlg(CrowdPlusConstants.LOGIC_DELETE_INITIAL);
		this.roleMapper.insertById(role);
	}

	@Override
	public ResultDto<String> update(final RoleDto roleDto) {
		final Role role = new Role();
		SecondBeanUtils.copyNullableProperties(roleDto, role);
		try {
			this.roleMapper.updateById(role);
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(CrowdPlusConstants.MESSAGE_ROLE_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
