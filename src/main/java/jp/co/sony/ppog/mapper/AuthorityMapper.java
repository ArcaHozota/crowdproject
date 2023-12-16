package jp.co.sony.ppog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sony.ppog.entity.Authority;
import jp.co.sony.ppog.entity.Role;

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

	/**
	 * IDリストによって検索を行う
	 *
	 * @param authIds 権限ID集合
	 * @return List<Authority>
	 */
	List<Role> selectByIds(@Param("ids") List<Long> authIds);
}
