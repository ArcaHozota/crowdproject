package jp.co.sony.ppog.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jp.co.sony.ppog.dto.SecurityAdmin;
import jp.co.sony.ppog.entity.Employee;
import jp.co.sony.ppog.entity.EmployeeRole;
import jp.co.sony.ppog.entity.Role;
import jp.co.sony.ppog.mapper.AuthorityMapper;
import jp.co.sony.ppog.mapper.EmployeeMapper;
import jp.co.sony.ppog.mapper.EmployeeRoleMapper;
import jp.co.sony.ppog.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;

/**
 * ログインコントローラ(SpringSecurity関連)
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Component
@RequiredArgsConstructor
public final class CrowdPlusUserDetailsService implements UserDetailsService {

	/**
	 * 権限マッパー
	 */
	private final AuthorityMapper authorityMapper;

	/**
	 * 社員管理マッパー
	 */
	private final EmployeeMapper employeeMapper;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleMapper employeeRoleMapper;

	/**
	 * 役割マッパー
	 */
	private final RoleMapper roleMapper;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Employee employee = this.employeeMapper.selectByLoginAcct(username);
		if (employee == null) {
			return null;
		}
		final EmployeeRole employeeRole = this.employeeRoleMapper.selectById(employee.getId());
		if (employeeRole == null) {
			return null;
		}
		final List<Role> selectByIdWithAuth = this.roleMapper.selectByIdWithAuth(employeeRole.getRoleId());
		final List<Long> authIds = selectByIdWithAuth.stream().map(item -> item.getRoleAuth().getAuthId())
				.collect(Collectors.toList());
		if (authIds.isEmpty()) {
			return null;
		}
		final List<GrantedAuthority> authorities = this.authorityMapper.selectByIds(authIds).stream()
				.map(item -> new SimpleGrantedAuthority(item.getName())).collect(Collectors.toList());
		return new SecurityAdmin(employee, authorities);
	}

}
