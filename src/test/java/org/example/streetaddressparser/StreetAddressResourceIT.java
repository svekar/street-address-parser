package org.example.streetaddressparser;

import static org.junit.Assert.*;

import javax.ws.rs.client.*;

import org.glassfish.grizzly.http.server.*;
import org.junit.*;

public class StreetAddressResourceIT {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
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

	@After
	public void tearDown() throws Exception {
		server.shutdownNow();
	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	@Test
	public void testGetIt() {
		StreetAddress responseMsg = target.path("street-addresses")
				.path("Street 123").request().get(StreetAddress.class);
		assertEquals(new StreetAddress("Street", 123), responseMsg);
	}
}
