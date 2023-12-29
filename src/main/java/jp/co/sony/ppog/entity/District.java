package jp.co.sony.ppog.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 権限エンティティ
 *
 * @author ArkamaHozota
 * @since 2.28
 */
@Data
public final class District implements Serializable {

	private static final long serialVersionUID = -7201275886879390519L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 州都名称
	 */
	private String shutoId;

	/**
	 * 地方
	 */
	private String chiho;

	/**
	 * 論理削除フラグ
	 */
	private String delFlg;

}
