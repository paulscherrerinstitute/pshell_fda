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

package ch.psi.fda.core.actions;

import ch.psi.fda.core.Action;

/**
 * Wait a specific time until executing the next action ...
 */
public class Delay implements Action {

	private final long time;
	
	/**
	 * @param time		Time to wait in milliseconds
	 */
	public Delay(long time){
		
		// Check if delay time is positive and >0
		if(time<=0){
			throw new IllegalArgumentException("Wait time must be >0");
		}
		
		this.time = time;
	}
	
	@Override
	public void execute() throws InterruptedException {
		Thread.sleep(time);
	}

	@Override
	public void abort() {
	}
}
