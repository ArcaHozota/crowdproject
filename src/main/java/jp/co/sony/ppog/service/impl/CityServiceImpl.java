package jp.co.sony.ppog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sony.ppog.dto.CityDto;
import jp.co.sony.ppog.service.ICityService;
import jp.co.sony.ppog.utils.Pagination;
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

	@Override
	public Pagination<CityDto> getDistrictsByKeyword(final Integer pageNum, final String keyword) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
