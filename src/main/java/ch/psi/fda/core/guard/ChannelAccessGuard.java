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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.psi.fda.core.Guard;
import ch.psi.jcae.ChannelException;

/**
 * Guard checking channels to meet a certain condition
 */
public class ChannelAccessGuard implements Guard {
	
	private static Logger logger = Logger.getLogger(ChannelAccessGuard.class.getName());
	
	/**
	 * Flag to indicate whether a guard condition failed since the last init call
	 * true: all conditions met, false: at least one condition failed
	 */
	private volatile boolean check = true;
	
	private final List<ChannelAccessGuardCondition<?>> conditions;
	
	public ChannelAccessGuard(List<ChannelAccessGuardCondition<?>> conditions){

		this.conditions = conditions;
		
		for(final ChannelAccessGuardCondition<?> condition: conditions){
			condition.getChannel().addPropertyChangeListener(new PropertyChangeListener() {		
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(! evt.getNewValue().equals(condition.getValue())){
						check=false;
					}
				}
			});
		}
	}
	
	@Override
	public void init() {
		check = true;
		
		// Check one time if all conditions are met
		for(ChannelAccessGuardCondition<?> condition: conditions){
			try{
				if(! (condition.getChannel().getValue(true)).equals(condition.getValue()) ){
					check=false;
					break;
				}
			}
			catch (InterruptedException e) {
				throw new RuntimeException("Guard interrupted ",e);
			} catch (TimeoutException | ChannelException | ExecutionException e) {
				logger.log(Level.WARNING, "Unable ", e);
				check=false;
			}
		}
	}

	@Override
	public boolean check() {
		return check;
	}
}
