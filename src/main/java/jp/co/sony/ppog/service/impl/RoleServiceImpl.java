package jp.co.sony.ppog.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
		final Long[] idArray = { 1L, 5L, 9L, 12L };
		final Long roleId = paramMap.get("roleId").get(0);
		this.roleMapper.batchDeleteByRoleId(roleId);
		final List<Long> authIds = paramMap.get("authIdArray");
		final List<RoleAuth> list = authIds.stream().filter(a -> !Arrays.asList(idArray).contains(a)).map(item -> {
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
		final Role entity = new Role();
		entity.setId(roleId);
		entity.setDelFlg(CrowdPlusConstants.LOGIC_DELETE_INITIAL);
		final Role role = this.roleMapper.selectByIdWithAuth(entity);
		return role.getRoleAuths().stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
	}

	@Override
	public List<Authority> getAuthlist() {
		return this.authorityMapper.selectAll();
	}

	@Override
	public List<RoleDto> getEmployeeRolesById(final Long id) {
		final List<RoleDto> secondRoles = new ArrayList<>();
		final RoleDto secondRole = new RoleDto();
		secondRole.setId(0L);
		secondRole.setName(CrowdPlusConstants.DEFAULT_ROLE_NAME);
		final List<RoleDto> roleDtos = this.roleMapper.selectAll(CrowdPlusConstants.LOGIC_DELETE_INITIAL).stream()
				.map(item -> {
					final RoleDto roleDto = new RoleDto();
					SecondBeanUtils.copyNullableProperties(item, roleDto);
					return roleDto;
				}).collect(Collectors.toList());
		secondRoles.add(secondRole);
		secondRoles.addAll(roleDtos);
		if (id == null) {
			return secondRoles;
		}
		final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(id);
		if (employeeRole == null) {
			return secondRoles;
		}
		secondRoles.clear();
		final Long roleId = employeeRole.getRoleId();
		final List<RoleDto> selectedRole = roleDtos.stream().filter(a -> Objects.equals(a.getId(), roleId))
				.collect(Collectors.toList());
		secondRoles.addAll(selectedRole);
		secondRoles.addAll(roleDtos);
		return secondRoles.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<RoleDto> getRolesByKeyword(final Integer pageNum, final String keyword) {
		final Integer pageSize = CrowdPlusConstants.DEFAULT_PAGE_SIZE;
		final Integer offset = (pageNum - 1) * pageSize;
		String searchStr;
		if (StringUtils.isDigital(keyword)) {
			searchStr = "%".concat(keyword).concat("%");
		} else {
			searchStr = StringUtils.getDetailKeyword(keyword);
		}
		final Long records = this.roleMapper.countByKeyword(searchStr, CrowdPlusConstants.LOGIC_DELETE_INITIAL);
		final Integer pageMax = (int) ((records / pageSize) + ((records % pageSize) != 0 ? 1 : 0));
		if (pageNum > pageMax) {
			final List<RoleDto> pages = this.roleMapper.paginationByKeyword(searchStr,
					CrowdPlusConstants.LOGIC_DELETE_INITIAL, (pageMax - 1) * pageSize, pageSize).stream().map(item -> {
						final RoleDto roleDto = new RoleDto();
						SecondBeanUtils.copyNullableProperties(item, roleDto);
						return roleDto;
					}).collect(Collectors.toList());
			return Pagination.of(pages, records, pageMax, pageSize);
		}
		final List<RoleDto> pages = this.roleMapper
				.paginationByKeyword(searchStr, CrowdPlusConstants.LOGIC_DELETE_INITIAL, offset, pageSize).stream()
				.map(item -> {
					final RoleDto roleDto = new RoleDto();
					SecondBeanUtils.copyNullableProperties(item, roleDto);
					return roleDto;
				}).collect(Collectors.toList());
		return Pagination.of(pages, records, pageNum, pageSize);
	}

	@Override
	public ResultDto<String> removeById(final Long roleId) {
		final List<EmployeeRole> list = this.employeeRoleMapper.selectByRoleId(roleId);
		if (!list.isEmpty()) {
			return ResultDto.failed(CrowdPlusConstants.MESSAGE_STRING_FORBIDDEN);
		}
		final Role entity = new Role();
		entity.setId(roleId);
		entity.setDelFlg(CrowdPlusConstants.LOGIC_DELETE_INITIAL);
		this.roleMapper.removeById(entity);
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
		final Role entity = new Role();
		entity.setId(roleDto.getId());
		entity.setDelFlg(CrowdPlusConstants.LOGIC_DELETE_INITIAL);
		final Role role1 = this.roleMapper.selectByIdWithAuth(entity);
		final Role role2 = role1;
		SecondBeanUtils.copyNullableProperties(roleDto, role1);
		if (role1.equals(role2)) {
			return ResultDto.failed(CrowdPlusConstants.MESSAGE_STRING_NOCHANGE);
		}
		try {
			this.roleMapper.updateById(role1);
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(CrowdPlusConstants.MESSAGE_ROLE_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
