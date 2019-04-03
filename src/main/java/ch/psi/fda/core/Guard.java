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
 * Guard to protect specific activities. A guard can be used to check for environment changes while a
 * certain activity was executed. 
 * 
 * Example:
 * An Guard can be used to check whether an injection happened while the the detector were read. 
 */
public interface Guard {
	
	/**
	 * Initialize guard object and its internal state.
	 */
	public void init();
	
	/**
	 * Check the status of the guard.
	 * @return	Returns true if the guard condition was not constrainted since the last init call. False otherwise.
	 */
	public boolean check();
}
