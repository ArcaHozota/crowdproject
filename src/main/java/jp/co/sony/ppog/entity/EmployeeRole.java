package jp.co.sony.ppog.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 社員役割連携エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class EmployeeRole implements Serializable {

	private static final long serialVersionUID = 8049959021603519067L;

	/**
	 * 社員ID
	 */
	private Long employeeId;

	/**
	 * 役割ID
	 */
	private Long roleId;
}
