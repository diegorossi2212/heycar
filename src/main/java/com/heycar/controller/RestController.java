package com.heycar.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.heycar.dto.CarDTOMapper;
import com.heycar.model.Car;
import com.heycar.model.search.CarSearch;
import com.heycar.model.service.CarService;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	private static final Logger log = LogManager.getLogger(RestController.class);
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private CarDTOMapper carDTOMapper;

	@GetMapping(path = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CarDTO> search(@RequestParam(required = false) String make, @RequestParam(required = false) String model, 
			@RequestParam(required = false) Integer year, @RequestParam(required = false) String color, 
			@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer length) {
		log.info("search - START");
		CarSearch carSearch = carDTOMapper.getCarSearch(make, model, year, color, start, length);
		log.info("search - SEARCH IS {}", carSearch);
		List<CarDTO> cars =carDTOMapper.getDtosFromCars(carService.search(carSearch));	
		log.info("search - WE FOUND {} CARS", cars.size());
		return cars;
	}

	@PostMapping(value = "/listings/{dealerId}/")
	@ResponseStatus(HttpStatus.CREATED)
	public void upsert(@PathVariable(required = true) Long dealerId, @Valid @RequestBody List<CarDTO> dtos) {
		log.info("insert - START");
		List<Car> cars = carDTOMapper.getCarsFromDto(dealerId, dtos);
		carService.upsert(dealerId, cars);				
		log.info("insert - END");
	}

	@PostMapping(value = "/listings/csv/{dealerId}/")
	@ResponseStatus(HttpStatus.CREATED)
	public void upsertViaCsv(@PathVariable(required = true) Long dealerId, @RequestParam("csv") MultipartFile csv) throws IOException {
		log.info("insertViaCsv - START");		
		List<Car> cars = carDTOMapper.getCarsFromCsv(dealerId, csv);
		carService.upsert(dealerId, cars);		
		log.info("insertViaCsv - END");
	}

}
