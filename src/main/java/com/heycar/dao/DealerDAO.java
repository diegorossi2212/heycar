package com.heycar.dao;

import org.springframework.stereotype.Repository;

import com.heycar.model.Dealer;
import com.heycar.model.mapper.DealerMapper;

@Repository
public class DealerDAO {

    private DealerMapper dealerMapper;

    public DealerDAO (DealerMapper dealerMapper) {
    	this.dealerMapper = dealerMapper;
    }
    
    public Dealer selectById(Long id) {
    	return dealerMapper.selectById(id);
    }

    public Integer insert(Dealer dealer) {
    	return dealerMapper.insert(dealer);
    }

}
