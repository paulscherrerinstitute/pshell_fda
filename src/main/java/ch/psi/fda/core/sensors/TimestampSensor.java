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

import ch.psi.fda.core.Sensor;

/**
 * Sensor to read the current time in milliseconds
 */
public class TimestampSensor implements Sensor {

	private final String id; // Global id of the sensor
	
	public TimestampSensor(String id){
		this.id = id;
	}
	
	@Override
	public Object read() {
		// Return current time in milliseconds
		return new Double(System.currentTimeMillis());
	}

	@Override
	public String getId() {
		return id;
	}

}
