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

public interface Actor {
	/**
	 * Set actor value
	 */
	public void set() throws InterruptedException;
	
	/**
	 * Function to check whether the actor has a next set value
	 * @return  Returns true if there is an actor value for the next iteration.
	 * 			False if there is no actor value (i.e. this will be the last iteration in the ActionLoop)
	 */
	public boolean hasNext();
	
	/**
	 * Initialize the actor to the start
	 */
	public void init();
	
	/**
	 * Reverse the set values of the actor
	 */
	public void reverse();
	
	/**
	 * Reset the actuator to its initial configuration
	 */
	public void reset();
}
