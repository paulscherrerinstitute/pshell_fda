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
package ch.psi.fda.fdaq;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;

import ch.psi.fda.messages.DataMessage;
import ch.psi.fda.messages.EndOfStreamMessage;
import ch.psi.fda.messages.Metadata;

public class Fdaq {

	private static final Logger logger = Logger.getLogger(Fdaq.class.getName());

	private volatile boolean stopAcquisition = false;
	private volatile boolean running = false;

	private final EventBus bus;
	
	private FdaqConfiguration configuration;
	private final int numberOfElements = Integer.MAX_VALUE/2;        
	
	public Fdaq(EventBus bus, FdaqConfiguration configuration){
		this.bus = bus;
		this.configuration = configuration;
	}
	
	/**
	 * Acquire data from the fdaq box
	 * @param hostname
	 * @param port
	 * @param numberOfElements
	 */
	public void acquire() {
		running = true; // potential threading problem
		
		final List<Metadata> metadata = new ArrayList<>();
		metadata.add(new Metadata("counter"));
		metadata.add(new Metadata("ain1"));
		metadata.add(new Metadata("ain2"));
		metadata.add(new Metadata("ain3"));
		metadata.add(new Metadata("ain4"));
		metadata.add(new Metadata("enc1"));
		
		Socket echoSocket = null;
		DataOutputStream out = null;
		DataInputStream in = null;

		boolean first = true;
		while(running){	
		
			try {
				
				
					stopAcquisition = false;
					echoSocket = new Socket(configuration.getHostname(), configuration.getPort());
					out = new DataOutputStream(echoSocket.getOutputStream());
					in = new DataInputStream(new BufferedInputStream(echoSocket.getInputStream()));
		
					ByteBuffer bytebuffer = ByteBuffer.allocate(3 * 4); // 3 times Integer
					bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
					bytebuffer.putInt(26); // Function number (index of pointer to function)
					bytebuffer.putInt(numberOfElements);

					if(first){
						bytebuffer.putInt(1); // Reset FIFO flag - The first time there need to be a 1 in int to reset the fifo buffer on the box
						first=false;
					}
					else{
						bytebuffer.putInt(0);
					}
					 
					out.write(bytebuffer.array());
					out.flush();
					
					for (int t = 0; t < numberOfElements; t++) {
						byte[] cbuffer = new byte[4*4]; // 4 times Integers a 4 bytes
						
						try{
							in.readFully(cbuffer);
						}
						catch(EOFException e){
							logger.info("End of Stream");
							break;
						}
						
						if(t<1028){
							// The first 1028 are wrong (not really wrong but there are repeated values/holes)
							// It has to do with the fact that we asynchronously reset the FIFO.
							// This is independent of the frequency
							continue;
						}
		
						ByteBuffer buffer = ByteBuffer.wrap(cbuffer);
						buffer.order(ByteOrder.LITTLE_ENDIAN);
						int a = buffer.getInt();
						int b = buffer.getInt();
						int b1 = b & 0xffff;
						int b2 = (b >> 16) & 0xffff;
						int c = buffer.getInt();
						int c1 = c & 0xffff;
						int c2 = (c >> 16) & 0xffff;
						int d = buffer.getInt();
		
						DataMessage message = new DataMessage(metadata);
						message.getData().add(a);
						message.getData().add(b1);
						message.getData().add(b2);
						message.getData().add(c1);
						message.getData().add(c2);
						message.getData().add(d);
						bus.post(message);
					}
					
					logger.info("Done ...");
				
			} catch (IOException e) {
				// Ignore potential exceptions if stop was triggered before all messages were retrieved
				if (!stopAcquisition) { 
					throw new RuntimeException(e);
				}
			} finally {
				try {
                                    if (out!=null)
					out.close();
                                    if (in!=null)
					in.close();
                                    if (echoSocket!=null)
					echoSocket.close();
				} catch (IOException e) {
					// Ignore because not relevant at this stage
				}
				running = false;
			}
		
		}
		
		bus.post(new EndOfStreamMessage());
	}

	/**
	 * Sending termination command to fdaq box
	 */
	public void stop() {
		try {
			running=false;
			stopAcquisition = true;
			Socket echoSocket = new Socket(configuration.getHostname(), configuration.getKillPort());
			DataOutputStream out = new DataOutputStream(echoSocket.getOutputStream());

			ByteBuffer bytebuffer = ByteBuffer.allocate(1 * 4); // 2
																// times
																// Integers
			bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
			bytebuffer.putInt(666);
			out.write(bytebuffer.array());
			out.flush();

			out.close();
			echoSocket.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	public boolean isRunning() {
		return running;
	}
}
