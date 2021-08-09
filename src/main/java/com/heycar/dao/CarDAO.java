package com.heycar.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.heycar.model.Car;
import com.heycar.model.mapper.CarMapper;
import com.heycar.model.search.CarSearch;

@Repository
public class CarDAO {

    @Resource
    @Qualifier("carMapper")
    private CarMapper carMapper;

    public List<Car> selectBySearch(CarSearch carSearch) {
    	return carMapper.selectBySearch(carSearch);
    }

    public Integer insert(Car car) {
    	return carMapper.insert(car);
    }
    
    public Car selectByUniqueKey(Long idDealer, String code) {
    	return carMapper.selectByUniqueKey(idDealer, code);
    }

    public Integer update(Car car) {
    	return carMapper.update(car);
    }
    
}
