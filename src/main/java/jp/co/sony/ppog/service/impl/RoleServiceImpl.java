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

import jp.co.sony.ppog.commons.CrowdProjectConstants;
import jp.co.sony.ppog.dto.RoleDto;
import jp.co.sony.ppog.entity.Authority;
import jp.co.sony.ppog.entity.EmployeeRole;
import jp.co.sony.ppog.entity.Role;
import jp.co.sony.ppog.entity.RoleAuth;
import jp.co.sony.ppog.mapper.AuthorityMapper;
import jp.co.sony.ppog.mapper.EmployeeRoleMapper;
import jp.co.sony.ppog.mapper.RoleAuthMapper;
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
	 * 役割権限連携マッパー
	 */
	private final RoleAuthMapper roleAuthMapper;

	/**
	 * 権限マッパー
	 */
	private final AuthorityMapper authorityMapper;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleMapper employeeRoleMapper;

	@Override
	public ResultDto<String> checkDuplicated(final String name) {
		return this.roleMapper.checkDuplicated(name) > 0
				? ResultDto.failed(CrowdProjectConstants.MESSAGE_ROLE_NAME_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public ResultDto<String> doAssignment(final Map<String, List<Long>> paramMap) {
		final Long[] idArray = { 1L, 5L, 9L, 12L };
		final Long roleId = paramMap.get("roleId").get(0);
		final List<RoleAuth> list1 = this.roleAuthMapper.selectByRoleId(roleId);
		final List<Long> list2 = list1.stream().map(RoleAuth::getAuthId).sorted().collect(Collectors.toList());
		final List<Long> authIds = paramMap.get("authIdArray").stream().filter(a -> !Arrays.asList(idArray).contains(a))
				.sorted().collect(Collectors.toList());
		if (list2.equals(authIds)) {
			return ResultDto.failed(CrowdProjectConstants.MESSAGE_STRING_NOCHANGE);
		}
		this.roleAuthMapper.batchDeleteByRoleId(roleId);
		final List<RoleAuth> list = authIds.stream().map(item -> {
			final RoleAuth roleAuth = new RoleAuth();
			roleAuth.setAuthId(item);
			roleAuth.setRoleId(roleId);
			return roleAuth;
		}).collect(Collectors.toList());
		try {
			this.roleAuthMapper.batchInsertByIds(list);
		} catch (final Exception e) {
			return ResultDto.failed(CrowdProjectConstants.MESSAGE_STRING_FORBIDDEN2);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public List<Long> getAuthIdsById(final Long id) {
		final Role role = this.roleMapper.selectByIdWithAuth(id);
		return role.getRoleAuths().stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
	}

	@Override
	public List<Authority> getAuthlist() {
		return this.authorityMapper.selectAll();
	}

	@Override
	public List<RoleDto> getEmployeeRolesByEmployeeId(final Long employeeId) {
		final List<RoleDto> secondRoles = new ArrayList<>();
		final RoleDto secondRole = new RoleDto();
		secondRole.setId(0L);
		secondRole.setName(CrowdProjectConstants.DEFAULT_ROLE_NAME);
		final List<RoleDto> roleDtos = this.roleMapper.selectAll().stream().map(item -> {
			final RoleDto roleDto = new RoleDto();
			SecondBeanUtils.copyNullableProperties(item, roleDto);
			return roleDto;
		}).collect(Collectors.toList());
		secondRoles.add(secondRole);
		secondRoles.addAll(roleDtos);
		if (employeeId == null) {
			return secondRoles;
		}
		final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(employeeId);
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
	public Role getRoleById(final Long id) {
		return this.roleMapper.selectByIdWithAuth(id);
	}

	@Override
	public Pagination<RoleDto> getRolesByKeyword(final Integer pageNum, final String keyword) {
		final Integer pageSize = CrowdProjectConstants.DEFAULT_PAGE_SIZE;
		final Integer offset = (pageNum - 1) * pageSize;
		String searchStr;
		if (StringUtils.isDigital(keyword)) {
			searchStr = "%".concat(keyword).concat("%");
		} else {
			searchStr = StringUtils.getDetailKeyword(keyword);
		}
		final Long records = this.roleMapper.countByKeyword(searchStr);
		final Integer pageMax = (int) ((records / pageSize) + ((records % pageSize) != 0 ? 1 : 0));
		if (pageNum > pageMax) {
			final List<RoleDto> pages = this.roleMapper
					.paginationByKeyword(searchStr, (pageMax - 1) * pageSize, pageSize).stream().map(item -> {
						final RoleDto roleDto = new RoleDto();
						SecondBeanUtils.copyNullableProperties(item, roleDto);
						return roleDto;
					}).collect(Collectors.toList());
			return Pagination.of(pages, records, pageMax, pageSize);
		}
		final List<RoleDto> pages = this.roleMapper.paginationByKeyword(searchStr, offset, pageSize).stream()
				.map(item -> {
					final RoleDto roleDto = new RoleDto();
					SecondBeanUtils.copyNullableProperties(item, roleDto);
					return roleDto;
				}).collect(Collectors.toList());
		return Pagination.of(pages, records, pageNum, pageSize);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		final List<EmployeeRole> list = this.employeeRoleMapper.selectByRoleId(id);
		if (!list.isEmpty()) {
			return ResultDto.failed(CrowdProjectConstants.MESSAGE_STRING_FORBIDDEN);
		}
		final Role entity = new Role();
		entity.setId(id);
		entity.setDelFlg(CrowdProjectConstants.LOGIC_DELETE_FLG);
		this.roleMapper.removeById(entity);
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final RoleDto roleDto) {
		final Role role = new Role();
		SecondBeanUtils.copyNullableProperties(roleDto, role);
		role.setId(SnowflakeUtils.snowflakeId());
		role.setDelFlg(CrowdProjectConstants.LOGIC_DELETE_INITIAL);
		this.roleMapper.insertById(role);
	}

	@Override
	public ResultDto<String> update(final RoleDto roleDto) {
		final Role entity = new Role();
		entity.setId(roleDto.getId());
		final Role role = this.roleMapper.selectByIdWithAuth(entity);
		SecondBeanUtils.copyNullableProperties(role, entity);
		SecondBeanUtils.copyNullableProperties(roleDto, role);
		if (role.equals(entity)) {
			return ResultDto.failed(CrowdProjectConstants.MESSAGE_STRING_NOCHANGE);
		}
		try {
			this.roleMapper.updateById(role);
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(CrowdProjectConstants.MESSAGE_ROLE_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
