package ch.psi.fda.aq;

import javax.inject.Inject;

import com.google.common.eventbus.EventBus;

import ch.psi.fda.EContainer;
import ch.psi.fda.EContainerFactory;
import ch.psi.fda.edescriptor.EDescriptor;
import ch.psi.jcae.ChannelService;

public class XScanFactory implements EContainerFactory {

	@Inject
	private ChannelService cservice;
	
	private AcquisitionConfiguration config = new AcquisitionConfiguration();
	
	@Override
	public boolean supportsEDescriptor(EDescriptor descriptor) {
		return (descriptor instanceof XScanDescriptor);
	}

	@Override
	public EContainer getEContainer(EDescriptor descriptor, EventBus bus) {
		
		if(! (descriptor instanceof XScanDescriptor)){
			throw new IllegalArgumentException("Descriptor of type "+descriptor.getClass().getName()+" is not supported - descriptor need to be of type "+XScanDescriptor.class);
		}
		
		XScanDescriptor xdescriptor = (XScanDescriptor) descriptor;
		
		return new XScanContainer(cservice, config, bus, xdescriptor.getConfiguration());
	}

}
