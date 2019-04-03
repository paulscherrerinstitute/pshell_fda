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

import ch.psi.fda.messages.DataMessage;
import ch.psi.fda.messages.EndOfStreamMessage;
import ch.psi.fda.messages.Message;
import ch.psi.fda.messages.Metadata;
import ch.psi.fda.messages.StreamDelimiterMessage;

/**
 * Collector class that is collecting and merging data from different Queues.
 */
public class Collector {
	
	private EventBus bus;
	private List<MessageListener> listeners = new ArrayList<>();
	
	private List<Metadata> metadata;
	private boolean first = true;
	
	public Collector(EventBus b){
		this.bus = b;
	}
	
	public void addEventBus(EventBus b){
		MessageListener l = new MessageListener();
		listeners.add(l);
		b.register(l);
	}

	
	
	private class MessageListener{
		
		private DataMessage message;
		
		@Subscribe
		public void onMessage(Message message){
			int level = listeners.indexOf(this);
			if(message instanceof DataMessage){
				this.message = (DataMessage) message;
				
				if(level==0){
					if(first){
						metadata = new ArrayList<>();
						for(int i=listeners.size()-1;i>=0;i--){
							// Correct/Add dimension information
							for(Metadata m: listeners.get(i).getMessage().getMetadata()){
								m.setDimension(i);
							}
							metadata.addAll(listeners.get(i).getMessage().getMetadata());
						}
						
					}
					DataMessage m = new DataMessage(metadata);
					for(int i=listeners.size()-1;i>=0;i--){
						m.getData().addAll(listeners.get(i).getMessage().getData());
					}
					bus.post(m);
				}
			}
			if(message instanceof EndOfStreamMessage){
				
				StreamDelimiterMessage ddm = new StreamDelimiterMessage(level, ((EndOfStreamMessage)message).isIflag());
				bus.post(ddm);
				if(level==(listeners.size()-1)){ // if highest dimension then send end of stream
					bus.post(new EndOfStreamMessage());
				}
			}
		}
		
		public DataMessage getMessage(){
			return message;
		}
	}
	
}

