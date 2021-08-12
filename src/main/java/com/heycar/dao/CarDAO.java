package com.heycar.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.heycar.model.Car;
import com.heycar.mybatis.CarMapper;
import com.heycar.search.CarSearch;

@Repository
public class CarDAO {

    private CarMapper carMapper;

    public CarDAO (CarMapper carMapper) {
    	this.carMapper = carMapper;
    }
    
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
