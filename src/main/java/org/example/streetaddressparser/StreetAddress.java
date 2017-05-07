package org.example.streetaddressparser;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Data
public final class StreetAddress {

	@JsonProperty
	private final String name;
	@JsonProperty
	private final Integer no;

}
