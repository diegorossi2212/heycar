package com.heycar.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.heycar.dto.CarDTO;
import com.heycar.model.Car;
import com.heycar.model.search.CarSearch;
import com.heycar.model.service.MapperService;
import com.heycar.model.service.CarService;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	private static final Logger log = LogManager.getLogger(RestController.class);
	
	private CarService carService;
		
	private MapperService carDTOMapper;

	public RestController(CarService carService, MapperService carDTOMapper) {
		this.carService = carService;
		this.carDTOMapper = carDTOMapper;
	}
	
	@GetMapping(path = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CarDTO> search(@RequestParam(required = false) String make, @RequestParam(required = false) String model, 
			@RequestParam(required = false) Integer year, @RequestParam(required = false) String color, 
			@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer length) {
		log.info("search - START");
		CarSearch carSearch = carDTOMapper.getCarSearch(make, model, year, color, start, length);
		log.info("search - SEARCH IS {}", carSearch);
		List<CarDTO> cars =carDTOMapper.getDtosFromCars(carService.search(carSearch));	
		log.info("search - WE FOUND {} CARS", CollectionUtils.size(cars));
		return cars;
	}

	@PostMapping(value = "/vehicle_listings/{dealerId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void upsert(@PathVariable(required = true) Long dealerId, @Valid @RequestBody List<CarDTO> dtos) {
		log.info("upsert - START");
		List<Car> cars = carDTOMapper.getCarsFromDto(dealerId, dtos);
		carService.upsert(dealerId, cars);				
		log.info("upsert - END");
	}

	@PostMapping(value = "/upload_csv/{dealerId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void upsertViaCsv(@PathVariable(required = true) Long dealerId, @RequestParam("csv") MultipartFile csv) {
		log.info("upsertViaCsv - START");		
		List<Car> cars = carDTOMapper.getCarsFromCsv(dealerId, csv);
		carService.upsert(dealerId, cars);		
		log.info("upsertViaCsv - END");
	}

}
