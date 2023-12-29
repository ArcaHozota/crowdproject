package jp.co.sony.ppog.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 地域情報転送クラス
 *
 * @author ArkamaHozota
 * @since 2.26
 */
@Data
public final class DistrictDto implements Serializable {

	private static final long serialVersionUID = -3273942722972094600L;

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
	private String shutoName;

	/**
	 * 地方
	 */
	private String chiho;

	/**
	 * 人口数量
	 */
	private Long population;
}
