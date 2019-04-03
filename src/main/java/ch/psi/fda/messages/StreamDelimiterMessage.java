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

/**
 * Message that is send at the end of the action loop inside an ActionLoop implementation
 */
public class StreamDelimiterMessage extends ControlMessage{
	private static final long serialVersionUID = 1L;
	/**
	 * Number of the dimension this delimiter belongs to.
	 */
	private final int number;
	
	/**
	 * Intersect flag - flag to indicate that stream should be intersected
	 * after this message.
	 */
	private final boolean iflag;
	
	/**
	 * @param number	Number of the dimension this delimiter belongs to
	 */
	public StreamDelimiterMessage(int number){
		this(number, false);
	}
	
	/**
	 * @param number
	 * @param iflag		Flag to indicate that data is grouped
	 */
	public StreamDelimiterMessage(int number, boolean iflag){
		this.number = number;
		this.iflag = iflag;
	}

	public int getNumber() {
		return number;
	}
	
	public boolean isIflag(){
		return iflag;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Message [ c message: delimiter dimension "+number+" ]";
	}
}
