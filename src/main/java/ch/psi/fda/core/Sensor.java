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

package ch.psi.fda.core;

/**
 * The sensor interface describes an entity that can be read out like a
 * simple channel or (image) detector. Depending on the sensor type the
 * returned data is of a certain type.
 */
public interface Sensor {
	/**
	 * Readout sensor. 
	 * @return	Sensor value. The type of the returned value depends on the sensor type.
	 */
	public Object read() throws InterruptedException;
	
	/**
	 * Get the global id of the sensor
	 * @return	id of sensor
	 */
	public String getId();
}
