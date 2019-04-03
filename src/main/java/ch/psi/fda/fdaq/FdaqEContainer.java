/**
 * 
 * Copyright 2014 Paul Scherrer Institute. All rights reserved.
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
package ch.psi.fda.fdaq;

import java.io.File;

import javax.inject.Inject;

import ch.psi.fda.EContainer;
import ch.psi.fda.fdaq.FdaqConfiguration;
import ch.psi.fda.serializer.SerializerTXT;

import com.google.common.eventbus.EventBus;

public class FdaqEContainer implements EContainer {

	private Fdaq fdaq;
	private EventBus bus;
	
	private FdaqEDescriptor edescriptor;
	
	@Inject
	public FdaqEContainer(FdaqEDescriptor edescriptor, EventBus ebus){
		this.bus = ebus;
		this.edescriptor = edescriptor;
	}

	
	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
		if(fdaq!=null && fdaq.isRunning()){
			throw new IllegalStateException("FDAQ is already running");
		}
		
		fdaq = new Fdaq(bus, new FdaqConfiguration());

		File file = new File(edescriptor.getFileName());
		file.getParentFile().mkdirs(); // Create data base directory
		
		SerializerTXT serializer = new SerializerTXT(file);
		serializer.setShowDimensionHeader(false);
		bus.register(serializer);
		
		fdaq.acquire();
	}

	@Override
	public void abort() {
		fdaq.stop();
	}

	@Override
	public boolean isActive() {
		return fdaq.isRunning();
	}

	@Override
	public void destroy() {
	}
}
