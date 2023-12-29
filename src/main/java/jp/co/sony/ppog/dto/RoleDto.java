package jp.co.sony.ppog.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 役割情報転送クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class RoleDto implements Serializable {

	private static final long serialVersionUID = 9004635352261624444L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;
}
