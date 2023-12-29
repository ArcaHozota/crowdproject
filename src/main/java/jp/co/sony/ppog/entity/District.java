package jp.co.sony.ppog.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 地域エンティティ
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
	 * 州都ID
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

	/**
	 * 地域都市連携エンティティ都市ID
	 */
	private City city;
}
