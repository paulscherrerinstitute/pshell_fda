/**
 * 
 * Copyright 2010 Paul Scherrer Institute. All rights reserved.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of
 * merchantability or fitness for a particular purpose. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this code. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.psi.fda.cdump;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class CdumpConfiguration {
	
	public final static String CDUMP_CONFIG = "ch.psi.fda.cdump.config.file";
	
	private String dataChannel;
	private int nelements = 65536;
	private String controlChannel;
	private String samplingRateChannel;

	public CdumpConfiguration(){
		String config = System.getProperty(CDUMP_CONFIG);
		
		if(config != null){
			loadFile(new File(config));
		}
		else{
                    //logger.fine("No configuration file specified via -D"+CDUMP_CONFIG+"=... - using defaults");
	   }
	}

	public void loadFile(File file) {
		Properties properties = new Properties();
		
		if(file!=null){
			try {
				properties.load(new FileReader(file));
			} catch (IOException e) {
				throw new RuntimeException("Cannot read file "+file, e);
			}
		}
		
		dataChannel = properties.getProperty(CdumpConfiguration.class.getPackage().getName()+".dataChannel", "");
		controlChannel = properties.getProperty(CdumpConfiguration.class.getPackage().getName()+".controlChannel", "");
		samplingRateChannel = properties.getProperty(CdumpConfiguration.class.getPackage().getName()+".samplingRateChannel", "");
		nelements = Integer.parseInt(properties.getProperty(CdumpConfiguration.class.getPackage().getName()+".nelements", "65536"));
		
	}
	
	public String getDataChannel() {
		return dataChannel;
	}
	public void setDataChannel(String dataChannel) {
		this.dataChannel = dataChannel;
	}
	public String getControlChannel() {
		return controlChannel;
	}
	public void setControlChannel(String controlChannel) {
		this.controlChannel = controlChannel;
	}
	public String getSamplingRateChannel() {
		return samplingRateChannel;
	}
	public void setSamplingRateChannel(String samplingRateChannel) {
		this.samplingRateChannel = samplingRateChannel;
	}
	public int getNelements() {
		return nelements;
	}
        public void setNelements(int nelements) {
		this.nelements = nelements;
	}        
}
