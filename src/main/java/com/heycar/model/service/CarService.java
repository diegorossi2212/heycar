package com.heycar.model.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heycar.dao.CarDAO;
import com.heycar.dao.DealerDAO;
import com.heycar.dto.CarDTO;
import com.heycar.dto.CarDTOMapper;
import com.heycar.model.Car;
import com.heycar.model.Dealer;
import com.heycar.model.search.CarSearch;

@Service
public class CarService {
	
	private static final Logger log = LogManager.getLogger(CarService.class);
	
	private CarDAO carDao;
	private DealerDAO dealerDAO;
	private CarDTOMapper carDTOMapper;
	
	@Transactional
	public void upsert(Long dealerId, List<Car> cars) {
		
		log.info("upsert - NUMBER OF CARS TO MANAGE = {}", cars);
		
		Dealer dealer = dealerDAO.selectById(dealerId);
		
		if(dealer == null) {
			log.info("upsert - DEALER {} DOES NOT EXIST - WILL BE INSERTED", dealerId);
			dealer = new Dealer();
			dealer.setId(dealerId);
			dealerDAO.insert(dealer);
			log.info("upsert - DEALER {} INSERTION OK", dealerId);
		}
		
		for(Car car: cars) {
			Car existingCar = carDao.selectByUniqueKey(dealerId, car.getCode());			
			if(existingCar == null) {
				carDao.insert(car);
			}else {
				log.info("upsert - CAR ALREADY EXIST - WILL BE UPDATED - {}", car);
				car.setId(existingCar.getId());
				carDao.update(car);
			}
		}
		
		log.info("upsert - END");
	}
	
	public List<Car> search(CarSearch carSearch){
		return carDao.selectBySearch(carSearch);
	}
	
	

}
