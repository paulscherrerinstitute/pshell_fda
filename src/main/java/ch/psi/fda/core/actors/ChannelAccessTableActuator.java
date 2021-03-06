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

package ch.psi.fda.core.actors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import ch.psi.fda.core.Actor;
import ch.psi.jcae.Channel;
import ch.psi.jcae.ChannelException;

/**
 * This actuator sets an Channel Access channel by using the positions from the given table.
 */
public class ChannelAccessTableActuator<T> implements Actor {
	
	private static Logger logger = Logger.getLogger(ChannelAccessTableActuator.class.getName());

	private boolean asynchronous = false;
	
	/**
	 * Position table
	 */
	private final double[] table;
	
	/**
	 * Level of accuracy the positioner need to have (e.g. if a positioner is set to 1 the readback set value
	 * of the positioner need to have at lease 1+/-accuracy)
	 */
	private double accuracy = 0.1;
	
	/**
	 * Execution count of actuator. This variable is used to minimize the floating point
	 * rounding errors for calculating the next step. 
	 */
	private int count;
	
	/**
	 * Flag that indicates whether there is a next set value for the Actor
	 */
	private boolean next;

	
	private Channel<Double> channel;
	private Channel<T> doneChannel = null;
	
	private final T doneValue;
	private final long doneDelay;
	
	/**
	 * Flag that indicates whether the actor moves in the positive direction 
	 */
	private boolean positiveDirection = true;
	private final boolean originalPositiveDirection;
	
	/**
	 * Maximum move time (in milliseconds)
	 */
	private Long timeout = null;
	
	private boolean checkActorSet = true;
	
	/**
	 * Constructor - Initialize actor
	 * @param channelName	Name of the channel to set
	 * @param table			Position table with the explicit positions for each step
	 * @param timeout		Maximum move time (in milliseconds)
	 */
	public ChannelAccessTableActuator(Channel<Double> channel, double[] table, Long timeout){
		this(channel, null, null, 0, table, timeout);
	}
	
	/**
	 * Constructor
	 * @param channel
	 * @param doneChannel
	 * @param doneValue
	 * @param doneDelay
	 * @param table
	 * @param timeout		Maximum move time (in milliseconds)
	 */
	public ChannelAccessTableActuator(Channel<Double> channel, Channel<T> doneChannel, T doneValue, double doneDelay, double[] table, Long timeout){
		
		this.doneValue = doneValue;
		this.doneDelay = (long) Math.floor((doneDelay*1000));
		
		if(table==null){
			throw new IllegalArgumentException("Null table is not accepted");
		}
		if(table.length==0){
			throw new IllegalArgumentException("Position table need to have at least one position");
		}
		
		this.table = table;
		
		// Validate and save timeout parameter
		if(timeout!=null && timeout<=0){
			throw new IllegalArgumentException("Timeout must be >0 or null");
		}
		else{
			this.timeout = timeout;
		}
		
		init();
		
		// Save the initial direction
		this.originalPositiveDirection = positiveDirection;
		
		this.channel = channel;
		this.doneChannel = doneChannel;
	}
	
	@Override
	public void set() throws InterruptedException {
		
		// Throw an IllegalStateException in the case that set is called although there is no next step.
		if(!next){
			throw new IllegalStateException("The actuator does not have any next step.");
		}
		
		// Set actuator channel
		logger.finest("Set actuator channel "+channel.getName()+" to value: "+table[count]);
		try {
			if(!asynchronous){
				if(timeout==null){
					channel.setValue(table[count]);
				}
				else{
					channel.setValueAsync(table[count]).get(timeout, TimeUnit.MILLISECONDS);
				}
			}
			else{
				channel.setValueNoWait(table[count]);
			}
			
			
			if(doneChannel != null){
				Thread.sleep(doneDelay);
				doneChannel.waitForValue(doneValue); 
			}
			
			// Check whether the set value is really on the value that was set before.
			if(doneChannel==null && !asynchronous && checkActorSet){
				if ( Math.abs( channel.getValue() - table[count] ) > accuracy ){
					throw new RuntimeException("Actor could not be set to the value "+table[count]+" The readback of the set value does not match the value that was set");
				}
			}
			
		} catch (ExecutionException | ChannelException | TimeoutException e) {
			throw new RuntimeException("Move actuator [channel: "+channel.getName()+"] to value "+table[count],e);
		}
		
		if(positiveDirection){
			count++;
			if(count<table.length){
				this.next = true;
			}
			else{
				// There is no next set value
				this.next = false;
			}
		}
		else{
			count--;
			if(count>=0){
				this.next = true;
			}
			else{
				// There is no next set value
				this.next = false;
			}
		}
	}

	@Override
	public boolean hasNext() {
		return next;
	}

	@Override
	public void init() {
		
		// Set first set value to the start value
		if(positiveDirection){
			this.count = 0; // Set count to the first element
		}
		else{
			this.count = table.length-1; // Set count to the last element
		}
		
		if(table.length>0){
			this.next = true;
		}
	}

	@Override
	public void reverse() {
		if(positiveDirection){
			positiveDirection=false;
		}
		else{
			positiveDirection=true;
		}
	}

	@Override
	public void reset() {
		this.positiveDirection = this.originalPositiveDirection;
	}

	public boolean isAsynchronous() {
		return asynchronous;
	}
	public void setAsynchronous(boolean asynchronous) {
		this.asynchronous = asynchronous;
	}
}
