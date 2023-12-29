package jp.co.sony.ppog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.dto.DistrictDto;
import jp.co.sony.ppog.mapper.DistrictMapper;
import jp.co.sony.ppog.service.IDistrictService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleSQLException;

/**
 * 地域サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 2.29
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(rollbackFor = OracleSQLException.class)
public class DistrictServiceImpl implements IDistrictService {

	/**
	 * 地域マッパー
	 */
	private final DistrictMapper districtMapper;

	@Override
	public Pagination<DistrictDto> getDistrictsByKeyword(final Integer pageNum, final String keyword) {
		final Integer pageSize = CrowdPlusConstants.DEFAULT_PAGE_SIZE;
		final Integer offset = (pageNum - 1) * pageSize;
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		this.districtMapper.paginationByKeyword(searchStr, offset, pageSize);
		return null;
	}
}
