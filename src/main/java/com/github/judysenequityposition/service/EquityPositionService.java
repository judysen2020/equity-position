package com.github.judysenequityposition.service;

import com.github.judysenequityposition.domain.dto.EquityPositionDto;
import com.github.judysenequityposition.service.models.EquityPositionVO;

import java.util.HashMap;

public interface EquityPositionService {
    /**
     * 判断是否已经执行了Cancel-action
     * @param tradeId
     * @return
     */
    boolean isCanceled(Integer tradeId);

    /**
     * 获取最新的一条记录
     * @param tradeId
     * @return
     */
    EquityPositionVO getLatest(Integer tradeId);

    /**
     * 保存equity position
     * @param equityPositionVO
     * @return
     */
    Integer insertOrUpdateEquityPosition(EquityPositionVO equityPositionVO);

    /**
     *
     * @param equityPositionVO
     * @return
     */
    Integer cancelEquityPosition(EquityPositionVO equityPositionVO);

    /**
     *
     * @return
     */
    HashMap<String,Integer> getOutput();
}