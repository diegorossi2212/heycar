package com.heycar.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Splitter;
import com.heycar.dto.CarDTO;
import com.heycar.exception.ValidationException;
import com.heycar.model.Car;
import com.heycar.search.CarSearch;

@Service
public class TransformService {

	private static final Logger log = LogManager.getLogger(TransformService.class);
	
	public Car getCarFromDto(Long idDealer, CarDTO carDTO) {
		Car car = new Car();
		car.setIdDealer(idDealer);
		car.setCode(carDTO.getCode());
		car.setColor(carDTO.getColor());
		car.setKw(carDTO.getkW());
		car.setMake(carDTO.getMake());
		car.setModel(carDTO.getModel());
		car.setPrice(carDTO.getPrice());
		car.setYear(carDTO.getYear());
		return car;
	}

	public CarDTO getCarDtoFromEntity(Car car) {
		CarDTO carDto = new CarDTO();
		carDto.setCode(car.getCode());
		carDto.setColor(car.getColor());
		carDto.setkW(car.getKw());
		carDto.setMake(car.getMake());
		carDto.setModel(car.getModel());
		carDto.setPrice(car.getPrice());
		carDto.setYear(car.getYear());
		return carDto;
	}
	
	public CarSearch getCarSearch(String make, String model, Integer year, String color, Integer start, Integer length) {
		CarSearch carSearch = new CarSearch();
		carSearch.setMake(make);
		carSearch.setModel(model);
		carSearch.setYear(year);
		carSearch.setColor(color);
		carSearch.setStart(start == null ? 0 : start);
		carSearch.setLength(length == null ? 20 : length);
		return carSearch;
	}

	public List<CarDTO> getDtosFromCars(List<Car> cars){
		return cars.stream().map(this::getCarDtoFromEntity).collect(Collectors.toUnmodifiableList());
	}
	
	public List<Car> getCarsFromDto(Long idDealer, List<CarDTO> dtos){
		return dtos.stream().map(dto -> getCarFromDto(idDealer, dto)).collect(Collectors.toUnmodifiableList());
	}	
	
	public List<Car> getCarsFromCsv(Long idDealer, MultipartFile file) {

		try (InputStream is = file.getInputStream()) {
			List<String> lines = IOUtils.readLines(file.getInputStream());
			log.info("getCarsFromCsv - DEALER {} - THE CSV HAS {} LINES / CARS", idDealer, CollectionUtils.size(lines));
			List<Car> cars = new ArrayList<>(lines.size());
			Splitter csvCommaSplitter = Splitter.on(",");
			for (int i = 1; i < lines.size(); i++) {				
				String line = StringUtils.trimToNull(lines.get(i));
				if (StringUtils.isNotBlank(line)) {
					List<String> carAttributes = csvCommaSplitter.splitToList(line);
					log.info("getCarsFromCsv - DEALER {} - THE LINE {} HAS {} ATTRIBUTES", idDealer, line, CollectionUtils.size(carAttributes));
					if (carAttributes.size() != 6) {
						throw new ValidationException(List.of(line));
					} else {
						Car car = new Car();
						car.setIdDealer(idDealer);
						car.setCode(carAttributes.get(0));
						car.setMake(StringUtils.substringBefore(carAttributes.get(1), "/"));
						car.setModel(StringUtils.substringAfter(carAttributes.get(1), "/"));
						car.setKw(Integer.parseInt(carAttributes.get(2)));
						car.setYear(Integer.parseInt(carAttributes.get(3)));
						car.setColor(carAttributes.get(4));
						car.setPrice(new BigDecimal(carAttributes.get(5)));
						cars.add(car);
					}
				}
			}
			return cars;
		}catch(IOException ioException) {
			log.error("getCarsFromCsv, IOException", ioException);
			throw new ValidationException(List.of("There was some problem in reading the csv multipart file"));
		}

	}

}
