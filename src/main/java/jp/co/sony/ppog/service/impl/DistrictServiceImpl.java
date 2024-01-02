package jp.co.sony.ppog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sony.ppog.commons.CrowdPlusConstants;
import jp.co.sony.ppog.dto.DistrictDto;
import jp.co.sony.ppog.mapper.DistrictMapper;
import jp.co.sony.ppog.service.IDistrictService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.SecondBeanUtils;
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
	public List<DistrictDto> getDistrictList(final String id) {
		final List<DistrictDto> list = new ArrayList<>();
		final List<DistrictDto> districtDtos = this.districtMapper.selectAll(CrowdPlusConstants.LOGIC_DELETE_INITIAL)
				.stream().map(item -> {
					final DistrictDto districtDto = new DistrictDto();
					SecondBeanUtils.copyNullableProperties(item, districtDto);
					districtDto.setShutoName(item.getCity().getName());
					return districtDto;
				}).collect(Collectors.toList());
		if (StringUtils.isEmpty(id) || StringUtils.isEqual("null", id)) {
			final DistrictDto districtDto = new DistrictDto();
			districtDto.setId(0L);
			districtDto.setName(CrowdPlusConstants.DEFAULT_ROLE_NAME);
			list.add(districtDto);
			list.addAll(districtDtos);
			return list;
		}
		final List<DistrictDto> collect = districtDtos.stream()
				.filter(a -> Objects.equals(Long.parseLong(id), a.getId())).collect(Collectors.toList());
		list.addAll(collect);
		list.addAll(districtDtos);
		return list.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<DistrictDto> getDistrictsByKeyword(final Integer pageNum, final String keyword) {
		final Integer pageSize = CrowdPlusConstants.DEFAULT_PAGE_SIZE;
		final Integer offset = (pageNum - 1) * pageSize;
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		final Long records = this.districtMapper.countByKeyword(searchStr, CrowdPlusConstants.LOGIC_DELETE_INITIAL);
		final List<DistrictDto> pages = this.districtMapper
				.paginationByKeyword(searchStr, CrowdPlusConstants.LOGIC_DELETE_INITIAL, offset, pageSize).stream()
				.map(item -> {
					final DistrictDto districtDto = new DistrictDto();
					SecondBeanUtils.copyNullableProperties(item, districtDto);
					districtDto.setShutoName(item.getCity().getName());
					return districtDto;
				}).collect(Collectors.toList());
		return Pagination.of(pages, records, pageNum, pageSize);
	}
}
