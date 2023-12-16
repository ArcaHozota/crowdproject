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
import jp.co.sony.ppog.mapper.EmployeeMapper;
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
	 * 社員管理マッパー
	 */
	private final EmployeeMapper employeeMapper;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Employee selectByLoginAcct = this.employeeMapper.selectByLoginAcct(username);
		if (selectByLoginAcct == null) {
			return null;
		}
		final Specification<RoleEx> where2 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("roleId"),
				roleOptional.get().getRoleId());
		final Specification<RoleEx> specification2 = Specification.where(where2);
		final List<Long> authIds = this.roleExRepository.findAll(specification2).stream().map(RoleEx::getAuthId)
				.collect(Collectors.toList());
		if (authIds.isEmpty()) {
			return null;
		}
		final List<GrantedAuthority> authorities = this.pgAuthRepository.findAllById(authIds).stream()
				.map(item -> new SimpleGrantedAuthority(item.getName())).collect(Collectors.toList());
		return new SecurityAdmin(employee, authorities);
	}

}
