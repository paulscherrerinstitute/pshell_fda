/**
 * 
 * Copyright 2010 Paul Scherrer Institute. All rights reserved.
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;

import ch.psi.jcae.Channel;
import ch.psi.jcae.ChannelDescriptor;
import ch.psi.jcae.ChannelException;
import ch.psi.jcae.ChannelService;

/**
 * Cdump readout logic - i.e. put monitor on a data waveform channel of the fast
 * ADC and send data to the eventbus
 */
public class Cdump {
	
	private static final Logger logger = Logger.getLogger(Cdump.class.getName());
	
	public final static String[] SAMPLING_RATES = new String[] {"1Hz","2Hz", "5Hz", "10Hz", "20Hz", "50Hz", "100Hz", "200Hz", "500Hz",
		"1kHz", "2kHz", "5kHz", "10kHz", "20kHz", "50kHz", "100kHz"};
	
	private Channel<int[]> adcData;

	private enum AdcCmd {
		READY, GO
	};

	private Channel<Integer> adcCmd;

	private CdumpListener listener;
	private ChannelService cservice;
	private CdumpConfiguration configuration;

	public Cdump(ChannelService cservice, EventBus ebus, CdumpConfiguration configuration) {
		this.cservice = cservice;
		this.configuration = configuration;
		this.listener = new CdumpListener(ebus, configuration.getNelements());
	}

	/**
	 * Acquire data with the given sampling rate
	 * @param samplingRate
	 */
	public void acquire(String samplingRate) {
		
		logger.info("Start acquisition with sampling rate "+ samplingRate);
		
		try {
			// Set ADC sampling rate
			Channel<Integer> smplRate = cservice.createChannel(new ChannelDescriptor<>(Integer.class, configuration.getSamplingRateChannel(), false));
			smplRate.setValue(getIntSamplingRate(samplingRate));
			smplRate.destroy();

			adcData = cservice.createChannel(new ChannelDescriptor<>(int[].class, configuration.getDataChannel(), true));
			adcCmd = cservice.createChannel(new ChannelDescriptor<>(Integer.class, configuration.getControlChannel(), false));

			adcCmd.setValue(AdcCmd.GO.ordinal());
			adcData.addPropertyChangeListener(listener);
		} catch (ChannelException | TimeoutException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Wait until acquire is done
	 */
	public void waitAcquireDone(){
		try {
			adcCmd.waitForValue(AdcCmd.READY.ordinal());
		} catch (InterruptedException | ExecutionException | ChannelException e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		
		logger.info("Stop acquisition");
		
		try {
			// Detach listener from channel - i.e. stop data acquisition
			adcData.removePropertyChangeListener(listener);
			adcCmd.setValue(AdcCmd.READY.ordinal());
	
			listener.terminate();
		
			adcCmd.destroy();
			adcData.destroy();
		} catch (ChannelException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get sampling rate int value based on the passed rate string.
	 * If the string does not match any of the strings specified in the rates variable
	 * this function will set the rate to 1Hz
	 * 
	 * 0  = 1Hz 
	 * 1  = 2Hz
	 * 2  = 5Hz
	 * 3  = 10Hz
	 * 4  = 20Hz
	 * 5  = 50Hz
	 * 6  = 100Hz
	 * 7  = 200Hz
	 * 8  = 500Hz
	 * 9  = 1000Hz
	 * 10 = 2000Hz
	 * 11 = 5000Hz
	 * 12 = 10000Hz
	 * 13 = 20000Hz
	 * 14 = 50000Hz
	 * 15 = 100000Hz
	 *  
	 * @param rate
	 */
	private int getIntSamplingRate(String rate){
		
		for(int i=0;i<SAMPLING_RATES.length; i++){
			if(rate.equals(SAMPLING_RATES[i])){
				return i;
			}
		}
		
		// Default sampling rate 10kHz
		logger.info("Using default sampling rate 12");
		return 12;
	}
}
