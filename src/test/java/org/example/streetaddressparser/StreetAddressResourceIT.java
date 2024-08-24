package org.example.streetaddressparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

class StreetAddressResourceIT {

	private static HttpServer server;
	private static WebTarget target;

	@BeforeAll
	static void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
		Client c = ClientBuilder.newClient();

		// uncomment the following line if you want to enable
		// support for JSON in the client (you also have to uncomment
		// dependency on jersey-media-json module in pom.xml and
		// Main.startServer())
		// --
		// c.configuration().enable(new
		// org.glassfish.jersey.media.json.JsonJaxbFeature());

		target = c.target(Main.BASE_URI);
	}

	@AfterAll
	static void tearDown() throws Exception {
		server.shutdownNow();
	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	@Test
	void testGetIt() {
		StreetAddress responseMsg = target.path("street-addresses")
				.path("Street 123").request().get(StreetAddress.class);
		assertEquals(new StreetAddress("Street", 123), responseMsg);
	}
}
