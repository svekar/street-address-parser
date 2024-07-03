package org.example.streetaddressparser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

final class StreetAddressResourceTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("params")
	final void testParseStreetAddress(ParseTestParams params) {
		StreetAddressResource streetAddressResource = new StreetAddressResource();
		assertEquals(params.expected(), streetAddressResource.parseStreetAddress(params.input()));
	}

	private static record ParseTestParams(String description, String input, StreetAddress expected) {
	}

	private static Stream<ParseTestParams> params() {
		return Stream.of(
				new ParseTestParams("Street name and number", "Gate 1", new StreetAddress("Gate", 1)),
				new ParseTestParams("Place name wo number", "Strawberry Field",
						new StreetAddress("Strawberry Field", null)),
				new ParseTestParams("Missing street name", "42", null));
	}

}
