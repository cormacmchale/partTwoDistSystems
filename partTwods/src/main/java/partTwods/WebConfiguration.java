package partTwods;

import io.dropwizard.Configuration;

public class WebConfiguration extends Configuration {
	//config for service
	//info for connecting to the password service
	private String hostService = "127.0.0.1";
	private int hostPort = 40000;
	public String getHostService() {
		return hostService;
	}
	public int getHostPort() {
		return hostPort;
	}	
}
