package org.example.streetaddressparser;

import java.util.regex.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("street-addresses")
public class StreetAddressResource {

	private static final Pattern STREET_ADDR_PATTERN =
			Pattern.compile("(\\D+)(\\s+(\\d+))?");

	@Path("{stringRepr}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public StreetAddress parseStreetAddress(
		@PathParam("stringRepr") String address) {
		return parse(address);
	}

	private static StreetAddress parse(String address) {
		Matcher matcher = STREET_ADDR_PATTERN.matcher(address);
		if (matcher.matches()) {
			String sno = matcher.group(3);
			Integer no = sno != null ? Integer.valueOf(sno) : null;
			return new StreetAddress(matcher.group(1), no);
		}
		return null;
	}
}
