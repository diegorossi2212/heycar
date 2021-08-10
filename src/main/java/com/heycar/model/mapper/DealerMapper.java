package com.heycar.model.mapper;

import com.heycar.model.Dealer;

public interface DealerMapper {

    Dealer selectById(Long id);

    Integer insert(Dealer dealer);
    
}
