package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	 * IDによって情報を挿入する
	 *
	 * @param employeeRole 社員役割連携エンティティ
	 */
	void insertById(EmployeeRole employeeRole);

	/**
	 * IDによって情報を検索する
	 *
	 * @param employeeRole 社員役割連携エンティティ
	 * @return EmployeeRole
	 */
	EmployeeRole selectById(EmployeeRole employeeRole);

	/**
	 * IDによって情報を検索する
	 *
	 * @param id 社員役割連携ID
	 * @return EmployeeRole
	 */
	EmployeeRole selectById(@Param("id") Long id);

	/**
	 * 役割IDによって情報を検索する
	 *
	 * @param roleId 役割ID
	 * @return EmployeeRole
	 */
	List<EmployeeRole> selectByRoleId(@Param("roleId") Long roleId);

	/**
	 * IDによって情報を更新する
	 *
	 * @param employeeRole 社員役割連携エンティティ
	 */
	void updateById(EmployeeRole employeeRole);
}
