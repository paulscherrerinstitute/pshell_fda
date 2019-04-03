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

package ch.psi.fda.core.sensors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import ch.psi.fda.core.Sensor;
import ch.psi.jcae.Channel;
import ch.psi.jcae.ChannelException;
import ch.psi.utils.Str;
import java.util.logging.Level;

/**
 * Channel Access sensor capable of reading a channel access channel
 */
public class ChannelAccessSensor<T> implements Sensor {

	private static Logger logger = Logger.getLogger(ChannelAccessSensor.class.getName());
	
	private Channel<T> channel;
	private final String id;
	private boolean failOnSensorError = true;
	
	public ChannelAccessSensor(String id, Channel<T> channel){
		this.id = id;
		this.channel = channel;
	}
	
	public ChannelAccessSensor(String id, Channel<T> channel, boolean failOnSensorError){
		this.id = id;
		this.channel = channel;
		this.failOnSensorError = failOnSensorError;
	}
	
	@Override
	public Object read() throws InterruptedException {				
		T v;
		try {
			v = channel.getValue();
                        if (logger.isLoggable(Level.FINEST)){
                            logger.finest("Read channel ["+channel.getName()+"]: " + Str.toString(v, 10));
                        }                                                
		} catch (TimeoutException | ChannelException | ExecutionException e) {
                        String errMsg = "Unable to get value from channel ["+channel.getName()+"]";
                        logger.fine(errMsg);
			if(failOnSensorError){
				throw new RuntimeException(errMsg,e);
			}
			v = null;
		} 
		return(v);
	}

	@Override
	public String getId() {
		return id;
	}
}
