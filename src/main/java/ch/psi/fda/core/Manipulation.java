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

import java.util.List;

import ch.psi.fda.messages.DataMessage;
import ch.psi.fda.messages.Metadata;

public interface Manipulation {

	/**
	 * Get the id of the manipulation
	 * @return	id of manipulation
	 */
	public String getId();
	
	/**
	 * Initialize the manipulation
	 * @param metadata	Metadata of the incomming data message
	 */
	public void initialize(List<Metadata> metadata);
	
	/**
	 * Execute the manipulation on the passed data message
	 * @param message Message to manipulate
	 * @return	Result of the manipulation
	 */
	public Object execute(DataMessage message);
}
