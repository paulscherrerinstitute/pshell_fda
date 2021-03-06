package ch.psi.fda.aq;

import com.google.common.eventbus.EventBus;

import ch.psi.fda.EContainer;
import ch.psi.fda.model.v1.Configuration;
import ch.psi.jcae.ChannelService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XScanContainer implements EContainer {

	private final Acquisition acquisition;
	
	private EventBus bus;
	private Configuration xscanConfiguration;
	
	public XScanContainer(ChannelService cservice, AcquisitionConfiguration config, EventBus bus, Configuration xscanConfiguration){
		acquisition = new Acquisition(cservice, config, null);
		this.bus = bus;
		this.xscanConfiguration = xscanConfiguration;
	}
	
	@Override
	public void initialize() {
            acquisition.initalize(bus, xscanConfiguration);
	}

	@Override
	public void execute() {
		try {
			acquisition.execute();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void abort() {
            try {
                acquisition.abort();
            } catch (Exception e) {
                Logger.getLogger(XScanContainer.class.getName()).log(Level.WARNING,null,e);
            }            	
	}

	@Override
	public void destroy() {
		acquisition.destroy();
	}

	@Override
	public boolean isActive() {
		return acquisition.isActive();
	}
}
