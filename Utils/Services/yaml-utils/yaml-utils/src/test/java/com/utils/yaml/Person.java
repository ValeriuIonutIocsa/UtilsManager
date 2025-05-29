package com.utils.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utils.string.StrUtils;

public class Person {

	@JsonProperty("name")
	@SuppressWarnings("all")
	private String name;

	@JsonProperty("age")
	@SuppressWarnings("all")
	private int age;

	@JsonProperty("job_title")
	@SuppressWarnings("all")
	private String jobTitle;

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
