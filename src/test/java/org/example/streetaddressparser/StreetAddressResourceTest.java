package org.example.streetaddressparser;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public final class StreetAddressResourceTest {

	// Used only by JUnit (parameterized test).
	@SuppressWarnings("unused")
	private final String description;
	private final String input;
	private final StreetAddress expected;

	public StreetAddressResourceTest(String description, String input,
			StreetAddress expected) {
		this.description = description;
		this.input = input;
		this.expected = expected;
	}

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"Street name and number", "Gate 1",
						new StreetAddress("Gate", 1)},
				{"Place name wo number", "Strawberry Field",
						new StreetAddress("Strawberry Field", null)},
				{"Missing street name", "42", null},
		});
	}

	@Test
	public final void testParseStreetAddress() {
		StreetAddressResource streetAddressResource =
				new StreetAddressResource();
		assertEquals(expected, streetAddressResource.parseStreetAddress(input));
	}

}
