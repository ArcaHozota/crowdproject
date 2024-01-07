package jp.co.sony.ppog.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 都市情報転送クラス
 *
 * @author ArkamaHozota
 * @since 2.33
 */
@Data
public final class CityDto implements Serializable {

	private static final long serialVersionUID = 6320829658849729484L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 都道府県ID
	 */
	private Long districtId;

	/**
	 * 読み方
	 */
	private String pronounciation;

	/**
	 * 都道府県名称
	 */
	private String districtName;

	/**
	 * 人口数量
	 */
	private Long population;

	/**
	 * 市町村旗
	 */
	private String cityFlag;
}
