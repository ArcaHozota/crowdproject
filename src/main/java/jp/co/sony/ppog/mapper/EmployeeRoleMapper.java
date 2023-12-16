package jp.co.sony.ppog.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sony.ppog.entity.EmployeeRole;

/**
 * 社員役割連携マッパー
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Mapper
public interface EmployeeRoleMapper {

	/**
	 * 社員役割連携
	 *
	 * @param employeeRole
	 */
	void saveById(EmployeeRole employeeRole);
}
