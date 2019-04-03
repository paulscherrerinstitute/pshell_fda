/**
 * 
 * Copyright 2014 Paul Scherrer Institute. All rights reserved.
 * 
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This code is distributed in the hope that it will be useful, but without any
 * warranty; without even the implied warranty of merchantability or fitness for
 * a particular purpose. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this code. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ch.psi.fda.fdaq;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class FdaqConfiguration {
	
	private static final Logger logger = Logger.getLogger(FdaqConfiguration.class.getName());
	private final static String FDAQ_CONFIG = "ch.psi.fda.fdaq.config.file";
	
	//private String hostname = "mchip015.psi.ch";
        private String hostname = "localhost";
	private int port = 2233;
	private int killPort = 2234;
	
	public FdaqConfiguration(){
		String config = System.getProperty(FDAQ_CONFIG);
		
		if(config != null){
			loadFile(new File(config));
		}
		else{
			//logger.fine("No configuration file specified via -D"+FDAQ_CONFIG+"=... - using defaults");
		}
	}
	
	public void loadFile(File file) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(file));
		} catch (IOException e) {
			throw new RuntimeException("Cannot read file "+file, e);
		}
		
		hostname = properties.getProperty(FdaqConfiguration.class.getPackage().getName()+".hostname", hostname);
		port = Integer.parseInt(properties.getProperty(FdaqConfiguration.class.getPackage().getName()+".port", port+""));
		killPort = Integer.parseInt(properties.getProperty(FdaqConfiguration.class.getPackage().getName()+".killPort", killPort+""));
	}
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getKillPort() {
		return killPort;
	}
	public void setKillPort(int killPort) {
		this.killPort = killPort;
	}
}
