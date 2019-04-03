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

package ch.psi.fda.aq;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import ch.psi.fda.core.Manipulation;
import ch.psi.fda.messages.DataMessage;
import ch.psi.fda.messages.Message;
import ch.psi.fda.messages.Metadata;

/**
 * Applies manipulations to the data stream
 */
public class Manipulator {
	
	private EventBus bus;
	
	private final List<Manipulation> manipulations;
	private boolean first = true;
	private List<Metadata> metadata = new ArrayList<>();
	
	public Manipulator(EventBus b, List<Manipulation> manipulations){
		this.bus = b;
		this.manipulations = manipulations;
	}

	@Subscribe
	public void onMessage(Message message){
		if(message instanceof DataMessage){
			if(first){
				first=false;
				
				metadata.addAll(((DataMessage) message).getMetadata());
				
				for(Manipulation manipulation: this.manipulations){
					manipulation.initialize(this.metadata);
					
					// Add manipulation id to metadata
					this.metadata.add(new Metadata(manipulation.getId(),0)); // Calculated component always belongs to lowes dimension
				}
			}
			
			
			DataMessage dm = (DataMessage) message;
//			message = new DataMessage(metadata);
			for(Manipulation manipulation: manipulations){
//                          ((DataMessage)message).getData().add(manipulation.execute(dm));
                            dm.getData().add(manipulation.execute(dm));
                            
                            // Need to update the metadata of the message
                            dm.setMetadata(this.metadata);
			}
		}                
		bus.post(message);
                //System.out.println(Thread.currentThread());
	}
}
