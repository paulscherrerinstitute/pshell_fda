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

import java.util.List;

import ch.psi.jcae.Channel;
import ch.psi.jcae.annotation.CaChannel;

public class TemplateVSC16Scaler {

	/**
	 * Command
	 */
	public enum Command {Done, Count};
	@CaChannel(type=Integer.class, name ="${PREFIX}.CNT")
	private Channel<Integer> command;
	
	
	public enum Mode {OneShot, AutoCount};
	/**
	 * Count mode
	 */
	@CaChannel(type=Integer.class, name ="${PREFIX}.CONT")
	private Channel<Integer> mode;
	
	/**
	 * Channel description
	 */
	@CaChannel(type=Boolean.class, name={"${PREFIX}.NM1", "${PREFIX}.NM2", "${PREFIX}.NM3", "${PREFIX}.NM4", "${PREFIX}.NM5", "${PREFIX}.NM6", "${PREFIX}.NM7", "${PREFIX}.NM8", "${PREFIX}.NM9", "${PREFIX}.NM10", "${PREFIX}.NM11", "${PREFIX}.NM12", "${PREFIX}.NM13", "${PREFIX}.NM14", "${PREFIX}.NM15", "${PREFIX}.NM16"})
	private List<Channel<Boolean>> channelDescription;
	
	/**
	 * Channel gate
	 */
	@CaChannel(type=Boolean.class, name={"${PREFIX}.G1", "${PREFIX}.G2", "${PREFIX}.G3", "${PREFIX}.G4", "${PREFIX}.G5", "${PREFIX}.G6", "${PREFIX}.G7", "${PREFIX}.G8", "${PREFIX}.G9", "${PREFIX}.G10", "${PREFIX}.G11", "${PREFIX}.G12", "${PREFIX}.G13", "${PREFIX}.G14", "${PREFIX}.G15", "${PREFIX}.G16"})
	private List<Channel<Boolean>> channelGate;
	
	/**
	 * Channel preset count
	 * If gate is on scaler will only count until this value
	 */
	@CaChannel(type=Integer.class, name={"${PREFIX}.PR1", "${PREFIX}.PR2", "${PREFIX}.PR3", "${PREFIX}.PR4", "${PREFIX}.PR5", "${PREFIX}.PR6", "${PREFIX}.PR7", "${PREFIX}.PR8", "${PREFIX}.PR9", "${PREFIX}.PR10", "${PREFIX}.PR11", "${PREFIX}.PR12", "${PREFIX}.PR13", "${PREFIX}.PR14", "${PREFIX}.PR15", "${PREFIX}.PR16"})
	private List<Channel<Integer>> channelPresetCount;

	
	
	public Channel<Integer> getCommand() {
		return command;
	}
	public Channel<Integer> getMode() {
		return mode;
	}
	public List<Channel<Boolean>> getChannelDescription() {
		return channelDescription;
	}
	public List<Channel<Boolean>> getChannelGate() {
		return channelGate;
	}
	public List<Channel<Integer>> getChannelPresetCount() {
		return channelPresetCount;
	}
}
