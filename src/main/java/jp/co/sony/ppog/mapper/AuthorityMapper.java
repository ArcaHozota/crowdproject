package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sony.ppog.entity.Authority;

/**
 * 権限マッパー
 *
 * @author ArkamaHozota
 * @since 1.00
 */
@Mapper
public interface AuthorityMapper {

	/**
	 * 全件検索を行う
	 *
	 * @return List<Authority>
	 */
	List<Authority> selectAll();
}
