package jp.co.sony.ppog.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 都市エンティティ
 *
 * @author ArkamaHozota
 * @since 2.31
 */
@Data
public final class City implements Serializable {

	private static final long serialVersionUID = 6544430264440743625L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 読み方
	 */
	private String pronouciation;

	/**
	 * 都道府県ID
	 */
	private Long districtId;

	/**
	 * 人口数量
	 */
	private Long population;

	/**
	 * 論理削除フラグ
	 */
	private String delFlg;

	/**
	 * 地域都市連携エンティティ地域ID
	 */
	private District district;
}
