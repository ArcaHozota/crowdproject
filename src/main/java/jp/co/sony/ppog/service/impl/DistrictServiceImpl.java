package jp.co.sony.ppog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sony.ppog.commons.CrowdProjectConstants;
import jp.co.sony.ppog.dto.DistrictDto;
import jp.co.sony.ppog.entity.District;
import jp.co.sony.ppog.mapper.CityMapper;
import jp.co.sony.ppog.mapper.DistrictMapper;
import jp.co.sony.ppog.service.IDistrictService;
import jp.co.sony.ppog.utils.Pagination;
import jp.co.sony.ppog.utils.ResultDto;
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
	 * 都市マッパー
	 */
	private final CityMapper cityMapper;

	/**
	 * 地域マッパー
	 */
	private final DistrictMapper districtMapper;

	@Override
	public List<DistrictDto> getDistrictList(final String id) {
		final List<DistrictDto> list = new ArrayList<>();
		final List<DistrictDto> districtDtos = this.districtMapper.selectAll().stream().map(item -> {
			final DistrictDto districtDto = new DistrictDto();
			SecondBeanUtils.copyNullableProperties(item, districtDto);
			return districtDto;
		}).collect(Collectors.toList());
		if (StringUtils.isEmpty(id) || StringUtils.isEqual("null", id)) {
			final DistrictDto districtDto = new DistrictDto();
			districtDto.setId(0L);
			districtDto.setName(CrowdProjectConstants.DEFAULT_ROLE_NAME);
			list.add(districtDto);
			list.addAll(districtDtos);
			return list;
		}
		final Long districtId = this.cityMapper.selectById(Long.parseLong(id)).getDistrictId();
		final List<DistrictDto> collect = districtDtos.stream().filter(a -> Objects.equals(districtId, a.getId()))
				.collect(Collectors.toList());
		list.addAll(collect);
		list.addAll(districtDtos);
		return list.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<DistrictDto> getDistrictsByKeyword(final Integer pageNum, final String keyword) {
		final Integer pageSize = CrowdProjectConstants.DEFAULT_PAGE_SIZE;
		final Integer offset = (pageNum - 1) * pageSize;
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		final Long records = this.districtMapper.countByKeyword(searchStr);
		final List<DistrictDto> pages = this.districtMapper.paginationByKeyword(searchStr, offset, pageSize).stream()
				.map(item -> {
					final DistrictDto districtDto = new DistrictDto();
					final Long population = this.cityMapper.countPopulationById(item.getId());
					SecondBeanUtils.copyNullableProperties(item, districtDto);
					districtDto.setPopulation(population);
					return districtDto;
				}).collect(Collectors.toList());
		return Pagination.of(pages, records, pageNum, pageSize);
	}

	@Override
	public ResultDto<String> update(final DistrictDto districtDto) {
		final District entity = new District();
		entity.setId(districtDto.getId());
		final District district = this.districtMapper.selectById(entity);
		SecondBeanUtils.copyNullableProperties(district, entity);
		SecondBeanUtils.copyNullableProperties(districtDto, district);
		if (district.equals(entity)) {
			return ResultDto.failed(CrowdProjectConstants.MESSAGE_STRING_NOCHANGE);
		}
		this.districtMapper.updateById(district);
		return ResultDto.successWithoutData();
	}
}
