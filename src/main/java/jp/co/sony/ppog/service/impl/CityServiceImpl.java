package jp.co.sony.ppog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.dto.CityDto;
import jp.co.sony.ppog.mapper.CityMapper;
import jp.co.sony.ppog.service.ICityService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.SecondBeanUtils;
import jp.co.sony.ppog.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleSQLException;

/**
 * 都市サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 2.33
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(rollbackFor = OracleSQLException.class)
public class CityServiceImpl implements ICityService {

	/**
	 * 都市マッパー
	 */
	private final CityMapper cityMapper;

	@Override
	public Pagination<CityDto> getCitiesByKeyword(final Integer pageNum, final String keyword) {
		final Integer pageSize = CrowdPlusConstants.DEFAULT_PAGE_SIZE;
		final Integer offset = (pageNum - 1) * pageSize;
		String searchStr;
		if (StringUtils.isDigital(keyword)) {
			searchStr = "%".concat(keyword).concat("%");
		} else {
			searchStr = StringUtils.getDetailKeyword(keyword);
		}
		final Long records = this.cityMapper.countByKeyword(searchStr);
		final List<CityDto> pages = this.cityMapper.paginationByKeyword(searchStr, offset, pageSize).stream()
				.map(item -> {
					final CityDto cityDto = new CityDto();
					SecondBeanUtils.copyNullableProperties(item, cityDto);
					return cityDto;
				}).collect(Collectors.toList());
		return Pagination.of(pages, records, pageNum, pageSize);
	}

}
