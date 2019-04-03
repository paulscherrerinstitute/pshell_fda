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

package ch.psi.fda.core.guard;

import ch.psi.jcae.Channel;

public class ChannelAccessGuardCondition<T> {
	
	private final Channel<T> channel;
	private final T value; // Value of the channel to meet condition

	public ChannelAccessGuardCondition(Channel<T> channel, T value){
		this.channel = channel;
		this.value = value;
	}
	
	public Channel<T> getChannel() {
		return channel;
	}

	public T getValue() {
		return value;
	}
}
