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
package ch.psi.fda.cdump;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import ch.psi.fda.messages.DataMessage;
import ch.psi.fda.messages.EndOfStreamMessage;
import ch.psi.fda.messages.Metadata;

import com.google.common.eventbus.EventBus;

/**
 * Listener that monitors the adc data channel and splitting up the data
 */
public class CdumpListener implements PropertyChangeListener {
	
	private final EventBus bus;
	private final int numberOfElements;

	private boolean first = true;
	private int numberOfWaveforms = 0;
	private final List<Metadata> metadata = new ArrayList<>();
	
	public CdumpListener(EventBus bus, int numberOfElements){
		this.bus = bus;
		this.numberOfElements = numberOfElements;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("value")){
			transform((int[]) evt.getNewValue());
		}
	}
	
	
	/**
	 * Transform received waveform
	 * 1. Take channel waveform
	 * [wavefrom .......................................................]
	 * 2. Split up waveform
	 * [1-number of elements][2-number of elements][3-number of elements]
	 * 3. Rotate splitted waveforms
	 * [1-0,2-0,3-0]   thats one message
	 * [1-1,2-1,3-1]
	 * ...
	 * [1-noe, 2-noe, 3-noe]
	 * 
	 */
	public void transform(int[] value){
		
		// The first time check whether received waveform is a multiple of the specified number of elements number
		// Calculate how many waveforms are within the received waveform
		if(first){
			first=false;
			int nelements = value.length;
			int n = nelements % numberOfElements;
			if (n != 0) {
				throw new RuntimeException("Array size is not a multiple of "+numberOfElements);
			}
			numberOfWaveforms = nelements / numberOfElements;
			for(int i=0;i<numberOfWaveforms;i++){
				metadata.add(new Metadata("waveform-"+i));
			}
		}
		

		// Split and rotate waveform
		for (int x = 0; x < numberOfElements; x++) {
			
			DataMessage m = new DataMessage(metadata);
			for (int t = 0; t < numberOfWaveforms; t++) {
				m.getData().add(value[x + t * numberOfElements]);
			}
			bus.post(m);
		}
	}

	public void terminate(){
		bus.post(new EndOfStreamMessage());
	}
}
