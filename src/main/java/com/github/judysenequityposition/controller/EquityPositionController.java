package com.github.judysenequityposition.controller;

import com.github.judysenequityposition.service.EquityPositionService;
import com.github.judysenequityposition.service.models.EquityPositionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/position")
public class EquityPositionController {
    @Autowired
    private EquityPositionService service;

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    public Boolean saveEquityPosition(@RequestBody EquityPositionVO equityPositionVO){
        return service.insertOrUpdateEquityPosition(equityPositionVO)>0;
    }
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public Boolean cancelEquityPosition(@RequestBody EquityPositionVO equityPositionVO){
        return service.cancelEquityPosition(equityPositionVO)>0;
    }
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String,Integer> getResult(){
        return service.getOutput();
    }
}
