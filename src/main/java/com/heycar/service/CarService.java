package com.heycar.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heycar.dao.CarDAO;
import com.heycar.dao.DealerDAO;
import com.heycar.model.Car;
import com.heycar.model.Dealer;
import com.heycar.search.CarSearch;

@Service
public class CarService {
	
	private static final Logger log = LogManager.getLogger(CarService.class);

	private CarDAO carDao;	
	private DealerDAO dealerDAO;
	
	public CarService (CarDAO carDAO, DealerDAO dealerDAO) {
		this.carDao = carDAO;
		this.dealerDAO = dealerDAO;
	}

	@Transactional
	public void upsert(Long dealerId, List<Car> cars) {
		
		log.info("upsert - NUMBER OF CARS TO MANAGE = {}", CollectionUtils.size(cars));
		
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
				log.info("upsert - CAR DOES NOT EXIST - WILL BE INSERTED - {}", car);
				carDao.insert(car);
			}else {
				car.setId(existingCar.getId());
				log.info("upsert - CAR ALREADY EXIST - WILL BE UPDATED - {}", car);				
				carDao.update(car);
			}
		}
		
		log.info("upsert - END");
		
	}
	
	public List<Car> search(CarSearch carSearch){
		return carDao.selectBySearch(carSearch);
	}
	
}
