package com.heycar.dto;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Splitter;
import com.heycar.model.Car;

@Service
public class CarDTOMapper {

	public Car getCarFromDto(Long idDealer, CarDTO carDTO) {
		Car car = new Car();
		car.setIdDealer(idDealer);
		car.setCode(carDTO.getCode());
		car.setColor(carDTO.getColor());
		car.setKw(carDTO.getKw());
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
		carDto.setKw(car.getKw());
		carDto.setMake(car.getMake());
		carDto.setModel(car.getModel());
		carDto.setPrice(car.getPrice());
		carDto.setYear(car.getYear());
		return carDto;
	}

	public List<CarDTO> getDtosFromCars(List<Car> cars){
		return cars.stream().map(this::getCarDtoFromEntity).collect(Collectors.toUnmodifiableList());
	}
	
	public List<Car> getCarsFromDto(Long idDealer, List<CarDTO> dtos){
		return dtos.stream().map(dto -> getCarFromDto(idDealer, dto)).collect(Collectors.toUnmodifiableList());
	}	
	
	public List<Car> getCarsFromCsv(Long idDealer, MultipartFile file) throws IOException {

		try (InputStream is = file.getInputStream()) {
			List<String> lines = IOUtils.readLines(file.getInputStream());
			List<Car> cars = new ArrayList<>(lines.size());
			for (String line : lines) {
				StringUtils.trimToNull(line);
				if (StringUtils.isNotBlank(line)) {
					List<String> carAttributes = Splitter.on(",").omitEmptyStrings().splitToList(line);
					if (carAttributes.size() != 6) {
						throw new IllegalArgumentException(line);
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
		}

	}

}
