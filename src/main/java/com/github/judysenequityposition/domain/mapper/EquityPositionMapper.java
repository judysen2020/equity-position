package com.github.judysenequityposition.domain.mapper;

import com.github.judysenequityposition.domain.dto.EquityPositionDto;

import java.util.List;

public interface EquityPositionMapper {
    /**
     * 数据库插入一条记录
     * @param equityPositionDto
     * @return
     */
    int insert(EquityPositionDto equityPositionDto);

    /**
     * 获取版本最大的那一条记录
     * @param tradeId
     * @return
     */
    EquityPositionDto getMaxVersion(int tradeId);
    List<EquityPositionDto> getOutput();
}
