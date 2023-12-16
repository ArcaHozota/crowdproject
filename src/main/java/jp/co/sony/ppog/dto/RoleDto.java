package jp.co.sony.ppog.dto;

import lombok.Data;

/**
 * 役割情報転送クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class RoleDto {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * アカウント
	 */
	private String name;
}
