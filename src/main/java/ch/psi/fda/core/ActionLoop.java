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

import com.google.common.eventbus.EventBus;

/**
 * Loop of actions to accomplish a task. Depending on the loop
 * actions may be executed in a different way.
 */
public interface ActionLoop extends Action {
	
	/**
	 * Prepare ActionLoop for execution.
	 */
	public void prepare();
	
	/**
	 * Cleanup resources used by this ActionLoop while it was executed.
	 */
	public void cleanup();
	
	/**
	 * Get the pre actions of the Loop
	 * @return	pre actions
	 */
	public List<Action> getPreActions();
	
	/**
	 * Get the post actions of the loop
	 * @return post actions
	 */
	public List<Action> getPostActions();
	
	/**
	 * @return is a datagroup
	 */
	public boolean isDataGroup();

	/**
	 * Set whether data of the loop belongs to a own data group
	 * @param dataGroup
	 */
	public void setDataGroup(boolean dataGroup);
	
	public EventBus getEventBus();
}
