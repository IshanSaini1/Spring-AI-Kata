package com.ai.robot.model;

import java.util.List;

import lombok.Data;

@Data
public class CountryCitiesPOJO {

	private List<CountriesStructure> data;
	
	@Data
	private static class CountriesStructure{
		String country;
		List<String> cities;
	}
}
