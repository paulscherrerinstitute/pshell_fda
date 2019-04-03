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

package ch.psi.fda.core.actors;

import ch.psi.fda.core.Actor;
import ch.psi.fda.core.Sensor;

/**
 * Pseudo actor that is literally doing nothing for n times
 */
public class PseudoActuatorSensor implements Actor, Sensor {

	/**
	 * Execution count of actuator.
	 */
	private int count;
	
	/**
	 * Number of counts for this actuator
	 */
	private final int counts;
	
	private final String id;
	
	/**
	 * @param counts
	 * @param id		Id of the Actor/Sensor
	 */
	public PseudoActuatorSensor(String id, int counts){
		if(counts < 1){
			throw new IllegalArgumentException("Count ["+counts+"] must be > 0");
		}
		this.id = id;
		this.counts = counts;
		
		init();
	}
	
	
	@Override
	public void set() {
		if(!hasNext()){
			throw new IllegalStateException("The actuator does not have any next step.");
		}
		
		count++;
	}

	@Override
	public boolean hasNext() {
		return (count<counts);
	}

	@Override
	public void init() {
		count=0;
	}

	@Override
	public void reverse() {
	}

	@Override
	public void reset() {
	}

	@Override
	public Object read() {
		return new Double(count); // Return actual count
	}

	@Override
	public String getId() {
		return this.id;
	}
}
