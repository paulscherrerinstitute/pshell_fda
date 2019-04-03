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

package ch.psi.fda.core.loops.cr;

import ch.psi.jcae.Channel;
import ch.psi.jcae.annotation.CaChannel;

public class TemplateEncoder {
	
	/**
	 * Resolution	- $(P)$(E)_SCL
	 */
	@CaChannel(type=Double.class, name="${PREFIX}_SCL")
	private Channel<Double> resolution;

	/**
	 * Offset	- $(P)$(E)_OFF
	 */
	@CaChannel(type=Double.class, name ="${PREFIX}_OFF")
	private Channel<Double> offset;

	/**
	 * Direction	- $(P)$(E)_DIR
	 */
	public enum Direction {Negative, Positive};
	@CaChannel(type=Integer.class, name ="${PREFIX}_DIR")
	private Channel<Integer> direction;

	
	
	public Channel<Double> getResolution() {
		return resolution;
	}
	public Channel<Double> getOffset() {
		return offset;
	}
	public Channel<Integer> getDirection() {
		return direction;
	}
	
	
}
