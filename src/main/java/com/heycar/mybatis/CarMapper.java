package com.heycar.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.heycar.model.Car;
import com.heycar.search.CarSearch;

public interface CarMapper {

    Integer insert(Car car);

    List<Car> selectBySearch(@Param("search") CarSearch search);
    
    Car selectByUniqueKey(@Param("idDealer") Long idDealer, @Param("code") String code);

    Integer update(Car car);
    
}
