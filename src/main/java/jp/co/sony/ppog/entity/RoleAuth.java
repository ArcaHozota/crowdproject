package jp.co.sony.ppog.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 役割権限連携エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class RoleAuth implements Serializable {

	private static final long serialVersionUID = 4995165208601855074L;

	/**
	 * 権限ID
	 */
	private Long authId;

	/**
	 * 役割ID
	 */
	private Long roleId;
}
