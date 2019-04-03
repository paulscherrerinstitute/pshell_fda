/*
 * Copyright (c) 2014 Paul Scherrer Institute. All rights reserved.
 */

package ch.psi.fda;

import ch.psi.fda.aq.AcquisitionConfiguration;
import ch.psi.fda.cdump.CdumpConfiguration;
import ch.psi.fda.serializer.SerializerTXT;
import ch.psi.jcae.ChannelService;
import ch.psi.jcae.impl.DefaultChannelService;
import ch.psi.pshell.core.Context;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Cdump {
    final ChannelService cservice = new DefaultChannelService();
    
    private String dataChannel = "CDUMP:WAVE";
    private String controlChannel = "CDUMP:CONTROL";
    private String samplingRateChannel = "CDUMP:SAMPLING";
    private int nelements = 65536;            
        
    public String getDataChannel() {
            return dataChannel;
    }
    public void setDataChannel(String dataChannel) {
            this.dataChannel = dataChannel;
    }
    public String getControlChannel() {
            return controlChannel;
    }
    public void setControlChannel(String controlChannel) {
            this.controlChannel = controlChannel;
    }
    public String getSamplingRateChannel() {
            return samplingRateChannel;
    }
    public void setSamplingRateChannel(String samplingRateChannel) {
            this.samplingRateChannel = samplingRateChannel;
    }
    public int getNelements() {
            return nelements;
    }
    
    private String fileName = "";
    private boolean showPlot = false;
    private int samplingRate = 10000; //"10kHz"
    
    public String getFileName() {
            if((fileName == null) || (fileName.trim().length()==0)){
                return AcquisitionConfiguration.getDataFileNameDefault();
            }
            return fileName;
    }
    public void setFileName(String fileName) {
            this.fileName = fileName;
    }
    public boolean getShowPlot() {
            return showPlot;
    }
    public void setShowPlot(boolean showPlot) {
            this.showPlot = showPlot;
    }
    public int getSamplingRate() {
            return samplingRate;
    }
    public void setSamplingRate(int samplingRate) {
            this.samplingRate = samplingRate;
    }
    
    private ch.psi.fda.cdump.Cdump service;    
    
    
    public void startAcquisition() {        
        CdumpConfiguration configuration = new CdumpConfiguration();
        configuration.setControlChannel(controlChannel);
        configuration.setDataChannel(dataChannel);
        configuration.setSamplingRateChannel(samplingRateChannel);
        configuration.setNelements(nelements);
        
        Logger.getLogger(Cdump.class.getName()).log(Level.INFO, "Start acquisition");

        File f = new File(Context.getInstance().getSetup().expandPath(getFileName())+ ".txt");
        f.getParentFile().mkdirs(); // Create data base directory
		
	// Create execution service
	EventBus eventbus = new AsyncEventBus(Executors.newSingleThreadExecutor());
	service = new ch.psi.fda.cdump.Cdump(cservice, eventbus, configuration);
		
	SerializerTXT serializer = new SerializerTXT(f);
	serializer.setShowDimensionHeader(false);
		
	eventbus.register(serializer);
                
        service.acquire(samplingRate >= 1000 ? (samplingRate/1000) + "kHz" : samplingRate + "Hz");
    }
        
    public void stopAcquisition() {
        if(service != null){
            Logger.getLogger(Cdump.class.getName()).log(Level.INFO, "Stop acquisition");
            service.stop();
        }
    }

}
