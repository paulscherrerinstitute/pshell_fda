/*
 * Copyright (c) 2014 Paul Scherrer Institute. All rights reserved.
 */

package ch.psi.fda;

import ch.psi.fda.aq.AcquisitionConfiguration;
import ch.psi.fda.serializer.SerializerTXT;
import ch.psi.fda.ui.visualizer.Visualizer;
import ch.psi.fda.vdescriptor.LinePlot;
import ch.psi.fda.vdescriptor.VDescriptor;
import ch.psi.fda.vdescriptor.XYSeries;
import ch.psi.fda.fdaq.FdaqConfiguration;
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
public class Fdaq {
    
    ch.psi.fda.fdaq.Fdaq fdaqService;
    
    private String hostname = "localhost";
    private int port = 2233;
    private int killPort = 2234;        
    
    public String getHostname() {
            return hostname;
    }
    public void setHostname(String hostname) {
            this.hostname = hostname;
    }
    public int getPort() {
            return port;
    }
    public void setPort(int port) {
            this.port = port;
    }
    public int getKillPort() {
            return killPort;
    }
    public void setKillPort(int killPort) {
            this.killPort = killPort;
    }
    
    private String fileName = "";
    private boolean showPlot = true;
    private int itemCount = 1000;
    private int subsamplingFactor = 100;
    
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
    public int getItemCount() {
            return itemCount;
    }
    public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
    }
    public int getSubsamplingFactor() {
            return subsamplingFactor;
    }
    public void setSubsamplingFactor(int killPort) {
            this.subsamplingFactor = subsamplingFactor;
    }
    
    
    public void startAcquisition() {        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    acquire();
                }
                catch (Exception ex){
                    Logger.getLogger(Fdaq.class.getName()).log(Level.WARNING,null,ex);
                }
            }
        }).start();	
    }
    
    
    public void acquire() {
        FdaqConfiguration configuration = new FdaqConfiguration();
        configuration.setHostname(hostname);
        configuration.setPort(port);
        configuration.setKillPort(killPort);        
		
	EventBus bus = new AsyncEventBus(Executors.newSingleThreadExecutor());
	fdaqService = new ch.psi.fda.fdaq.Fdaq(bus, configuration);                
        
        File f = new File(Context.getInstance().getSetup().expandPath(getFileName()) + ".txt");
        f.getParentFile().mkdirs(); // Create data base directory
        
        SerializerTXT serializer = new SerializerTXT(f, false);
	serializer.setShowDimensionHeader(false);
	bus.register(serializer);
		
	// This stop ensures that the data server is in a good shape (i.e. gets restarted)
	// We need to wait a certain amount of time to have the server restarted.
	fdaqService.stop();
	try {
		Thread.sleep(1000); // TODO check whether this sleep is really necessary
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
        
        Logger.getLogger(Fdaq.class.getName()).log(Level.INFO, "Start fdaq acquisition");

        if (showPlot) {
            VDescriptor vd = new VDescriptor();				
                //LinePlot lineplot = new LinePlot();
            for (String plot: new String[]{"ain1","ain2","ain3","ain4","enc1"}){
                LinePlot lineplot = new LinePlot(plot);
                XYSeries series = new XYSeries("counter", plot);
                series.setMaxItemCount(itemCount);
                lineplot.getData().add(series);            
                vd.getPlots().add(lineplot);
            }            
            Visualizer visualizer = new Visualizer(vd);            
            visualizer.setSubsampling(subsamplingFactor);
            bus.register(visualizer);
            ProcessorFDA.setPlots(visualizer.getPlotPanels(), null);            
        }

       fdaqService.acquire();
    }

    public void stopAcquisition() {

        if(fdaqService != null){
            Logger.getLogger(Fdaq.class.getName()).log(Level.INFO, "Stop fdaq acquisition");
            fdaqService.stop();
        }
    }

}
