/**
 * 
 * Copyright 2013 Paul Scherrer Institute. All rights reserved.
 * 
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This code is distributed in the hope that it will be useful, but without any
 * warranty; without even the implied warranty of merchantability or fitness for
 * a particular purpose. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this code. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ch.psi.fda.core.loops.cr;

/**
 * Readout resource of crlogic
 */
public class CrlogicResource {
	
	/**
	 * Id of the data
	 */
	private String id;
	/**
	 * Id of the crlogic resource to be read out. As configured in the CRLOGIC part of the IOC startup script
	 */
	private String key;
	/**
	 * Flag whether to read back the delta of the values of this resource. (delta is done in software)
	 */
	private boolean delta = false;
	
	public CrlogicResource(){
	}
	
	public CrlogicResource(String id, String key){
		this.id = id;
		this.key = key;
	}
	
	public CrlogicResource(String id, String key, boolean delta){
		this.id = id;
		this.key = key;
		this.delta = delta;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isDelta() {
		return delta;
	}
	public void setDelta(boolean delta) {
		this.delta = delta;
	}
}
