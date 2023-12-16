package jp.co.sony.ppog.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 役割エンティティ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class Role implements Serializable {

	private static final long serialVersionUID = 4360593022825424340L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 論理削除フラグ
	 */
	private String delFlg;

	/**
	 * 役割権限連携エンティティ
	 */
	private RoleAuth roleAuth;
}
