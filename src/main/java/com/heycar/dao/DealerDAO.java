package com.heycar.dao;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.heycar.model.Dealer;
import com.heycar.model.mapper.DealerMapper;

@Repository
public class DealerDAO {

    @Resource
    @Qualifier("dealerMapper")
    private DealerMapper dealerMapper;

    public Dealer selectById(Long id) {
    	return dealerMapper.selectById(id);
    }

    public Integer insert(Dealer dealer) {
    	return dealerMapper.insert(dealer);
    }

}
