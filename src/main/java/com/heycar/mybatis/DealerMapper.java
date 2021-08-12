package com.heycar.mybatis;

import com.heycar.model.Dealer;

public interface DealerMapper {

    Dealer selectById(Long id);

    Integer insert(Dealer dealer);
    
}
