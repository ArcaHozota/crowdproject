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
	private String districtId;

	/**
	 * 都道府県名称
	 */
	private String districtName;

	/**
	 * 人口数量
	 */
	private Long population;

	/**
	 * 駅数量
	 */
	private Long stationNo;
}
