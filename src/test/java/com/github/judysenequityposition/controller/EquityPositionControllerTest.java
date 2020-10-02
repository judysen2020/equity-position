package com.github.judysenequityposition.controller;

import com.github.judysenequityposition.common.Constant;
import com.github.judysenequityposition.service.models.EquityPositionVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class EquityPositionControllerTest {
    @Autowired
    private EquityPositionController controller;

    @Test
    public void saveEquityPosition(){
        EquityPositionVO vo=new EquityPositionVO();
        vo.setMode(Constant.Buy);
        vo.setTradeID(1);
        vo.setQuantity(50);
        vo.setSecurityCode(Constant.REL);
        var rtn=controller.saveEquityPosition(vo);

        Assert.isTrue(rtn==true);
    }
    @Test
    public void cancelEquityPosition(){
        saveEquityPosition();

        EquityPositionVO vo=new EquityPositionVO();
        vo.setMode(Constant.Buy);
        vo.setTradeID(1);
        vo.setQuantity(50);
        vo.setSecurityCode(Constant.REL);
        var rtn=controller.cancelEquityPosition(vo);

        Assert.isTrue(rtn==true);
    }
    @Test
    public void cancelEquityPosition2(){
        EquityPositionVO vo=new EquityPositionVO();
        vo.setMode(Constant.Buy);
        vo.setTradeID(1);
        vo.setQuantity(50);
        vo.setSecurityCode(Constant.REL);
        var rtn=controller.cancelEquityPosition(vo);

        Assert.isNull(rtn);
    }
    @Test
    public void cancelEquityPosition3() throws InterruptedException {
        saveEquityPosition();
        EquityPositionVO vo=new EquityPositionVO();
        vo.setMode(Constant.Buy);
        vo.setTradeID(1);
        vo.setQuantity(50);
        vo.setSecurityCode(Constant.REL);
        var rtn=controller.cancelEquityPosition(vo);

        Thread.sleep(1000);
        rtn=controller.cancelEquityPosition(vo);
        Assert.isNull(rtn);
    }
    @Test
    public void getOutput(){
        var rtn=controller.getResult();
        Assert.isTrue(rtn.get("REL")==0);
        Assert.isTrue(rtn.get("ITC")==0);
        Assert.isTrue(rtn.get("INF")==0);
    }
    @Test
    public void getOutput2(){
        saveEquityPosition();
        var rtn=controller.getResult();
        Assert.isTrue(rtn.get("REL")==50);
    }
    @Test
    public void getOutput3(){
        cancelEquityPosition();
        var rtn=controller.getResult();
        Assert.isTrue(rtn.get("REL")==0);
    }
    @Test
    public void getOutput4(){
        initData();

        var rtn=controller.getResult();
        Assert.isTrue(rtn.get("REL")==60);
        Assert.isTrue(rtn.get("ITC")==0);
        Assert.isTrue(rtn.get("INF")==50);
    }

    private void initData(){
        EquityPositionVO vo=new EquityPositionVO();
        vo.setMode(Constant.Buy);
        vo.setTradeID(1);
        vo.setQuantity(50);
        vo.setSecurityCode(Constant.REL);
        var rtn=controller.saveEquityPosition(vo);

        vo.setMode(Constant.Sell);
        vo.setTradeID(2);
        vo.setQuantity(40);
        vo.setSecurityCode(Constant.ITC);
        controller.saveEquityPosition(vo);

        vo.setMode(Constant.Buy);
        vo.setTradeID(3);
        vo.setQuantity(70);
        vo.setSecurityCode(Constant.INF);
        controller.saveEquityPosition(vo);

        vo.setMode(Constant.Buy);
        vo.setTradeID(1);
        vo.setQuantity(60);
        vo.setSecurityCode(Constant.REL);
        controller.saveEquityPosition(vo);

        vo.setMode(Constant.Buy);
        vo.setTradeID(2);
        vo.setQuantity(30);
        vo.setSecurityCode(Constant.ITC);
        controller.cancelEquityPosition(vo);

        vo.setMode(Constant.Sell);
        vo.setTradeID(4);
        vo.setQuantity(20);
        vo.setSecurityCode(Constant.INF);
        controller.saveEquityPosition(vo);
    }
}
