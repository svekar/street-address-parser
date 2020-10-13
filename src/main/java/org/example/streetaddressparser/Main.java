package org.example.streetaddressparser;

import java.io.*;
import java.net.*;

import org.glassfish.grizzly.http.server.*;
import org.glassfish.jersey.grizzly2.httpserver.*;
import org.glassfish.jersey.server.*;
import org.slf4j.*;

/**
 * Main class.
 *
 */
public enum Main {
	;

	private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI =
			String.format("http://%s:%s/street-address-parser/",
					getenvOrDefault("server.address", DEFAULT_BIND_ADDRESS),
					getenvOrDefault("server.port", "8080"));

	private static String getenvOrDefault(String varName, String defaultValue) {
		String envVarVal = System.getProperty(varName);
		if (envVarVal == null) {
			envVarVal = System.getenv(varName);
			return envVarVal != null ? envVarVal : defaultValue;
		}
		return envVarVal;
	}

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and
		// providers
		// in org.example.streetaddressparser package
		final ResourceConfig rc = new ResourceConfig()
				.packages("org.example.streetaddressparser");

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		logger.info(BASE_URI);
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI),
				rc);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		try {
			HttpServer server = startServer();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				logger.info("Shutting down Grizzly http server {}.", server);
				server.shutdown();
			}));
			
		} catch (Throwable e) {
			logger.error("Cannot start Grizzly HTTP server: ", e);
			throw e;
		}
		logger.info(
				"Jersey app started with WADL available at "
						+ "{}application.wadl.",
				BASE_URI);
	}
}
