package com.github.judysenequityposition.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.judysenequityposition.common.Constant;
import com.github.judysenequityposition.common.ObjectUtil;
import com.github.judysenequityposition.domain.dto.EquityPositionDto;
import com.github.judysenequityposition.domain.mapper.EquityPositionMapper;
import com.github.judysenequityposition.exception.ServiceException;
import com.github.judysenequityposition.service.EquityPositionService;
import com.github.judysenequityposition.service.models.EquityPositionVO;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;

@Service
public class EquityPositionServiceImpl implements EquityPositionService {
    Logger logger = LoggerFactory.getLogger(EquityPositionServiceImpl.class);
    @Autowired
    private EquityPositionMapper mapper;

    /**
     * 判断是否已经执行了Cancel-action
     *
     * @param tradeId
     * @return
     */
    @Override
    public boolean isCanceled(Integer tradeId) {
        Assert.notNull(tradeId, "tradeId is required");
        logger.info("isCanceled start ,tradeId=" + tradeId);
        var flag = true;
        var ep = mapper.getMaxVersion(tradeId);
        if (ep.getAction().intValue() != 2) {
            flag = false;
        }
        logger.info("isCanceled end flag=" + flag);
        return flag;
    }

    /**
     * 获取最新的一条记录
     *
     * @param tradeId
     * @return
     */
    @Override
    public EquityPositionVO getLatest(Integer tradeId) {
        Assert.notNull(tradeId,"tradeId is required");
        logger.info("getLatest start,tradeId="+tradeId);
        var ep=mapper.getMaxVersion(tradeId);
        logger.info("getLatest end ep="+ JSON.toJSONString(ep));

        return ObjectUtil.map(ep,EquityPositionVO.class);
    }

    /**
     * 保存equity position
     *
     * @param equityPositionVO
     * @return
     */
    @Override
    public Integer insertOrUpdateEquityPosition(EquityPositionVO equityPositionVO) {
        AssertParams(equityPositionVO);
        var equityPosition=ObjectUtil.map(equityPositionVO, EquityPositionDto.class);
        synchronized (equityPositionVO.getTradeID()){
            var ep=mapper.getMaxVersion(equityPositionVO.getTradeID());
            if(ep==null){
                equityPosition.setAction(Constant.INSERT);
                equityPosition.setVersion(1);
            }else{
                if(isCanceled(equityPosition.getTradeID())){
                    throw new ServiceException("the equity position is canceled",400);
                }
                equityPosition.setAction(Constant.UPDATE);
                equityPosition.setVersion(ep.getVersion()+1);
            }
            if(equityPosition.getMode()==Constant.Sell){
                equityPosition.setQuantity(-equityPosition.getQuantity());
            }
            var flag=mapper.insert(equityPosition);
            return flag;
        }
    }

    private void AssertParams(EquityPositionVO equityPositionVO) {
        Assert.notNull(equityPositionVO.getTradeID(),"tradeId is required");
        Assert.notNull(equityPositionVO.getMode(),"mode is required");
        Assert.notNull(equityPositionVO.getSecurityCode(),"securityCode is required");
        Assert.notNull(equityPositionVO.getQuantity(),"the quantity is required");
    }

    /**
     * @param equityPositionVO
     * @return
     */
    @Override
    public Integer cancelEquityPosition(EquityPositionVO equityPositionVO) {
        AssertParams(equityPositionVO);
        var equityPosition=ObjectUtil.map(equityPositionVO, EquityPositionDto.class);
        synchronized (equityPositionVO.getTradeID()){
            var ep=mapper.getMaxVersion(equityPositionVO.getTradeID());
            if(ep==null){
                throw new ServiceException("the tradeId isnot insert",400);
            }else if(isCanceled(equityPosition.getTradeID())){
                throw new ServiceException("the tradeId was canceled",400);
            }else{
                equityPosition.setVersion(ep.getVersion()+1);
                equityPosition.setAction(Constant.CANCEL);
            }
            int flag=mapper.insert(equityPosition);
            return flag;
        }
    }

    /**
     * @return
     */
    @Override
    public HashMap<String, Integer> getOutput() {
        var list=mapper.getOutput();
        HashMap<String,Integer> map=new HashMap<>();
        map.put("REL",0);
        map.put("ITC",0);
        map.put("INF",0);
        for ( var ep:list) {
            if(ep.getAction()==Constant.CANCEL){        //if cancel then continue
                continue;
            }

            if(ep.getSecurityCode()==Constant.REL){
                map.put("REL",map.get("REL")+ep.getQuantity());
            }else if(ep.getSecurityCode()==Constant.ITC){
                map.put("ITC",map.get("ITC")+ep.getQuantity());
            }else if(ep.getSecurityCode()== Constant.INF){
                map.put("INF",map.get("INF")+ep.getQuantity());
            }
        }

        return map;
    }
}
