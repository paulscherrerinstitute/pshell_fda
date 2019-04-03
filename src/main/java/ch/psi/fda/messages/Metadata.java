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

package ch.psi.fda.messages;

import java.io.Serializable;

/**
 * Metadata of a component of a message. Each component has a global id.
 * Optionally the component can also belong to a dimension. However, depending on the 
 * view the number of the dimension might vary. Therefore the dimension number
 * might change during the lifetime of a message (component).
 */
public class Metadata implements Serializable{

	private static final long serialVersionUID = 1L;

	private final String id;
	private int dimension;
	
	public Metadata(String id){
		this.id = id;
		this.dimension = 0;
	}
	
	public Metadata(String id, int dimension){
		this.id = id;
		this.dimension = dimension;
	}

	public void setDimension(int dimension){
		this.dimension = dimension;
	}
	
	public int getDimension() {
		return dimension;
	}

	public String getId() {
		return id;
	}
}
