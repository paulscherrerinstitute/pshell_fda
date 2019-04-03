package ch.psi.fda.cdump;

import javax.inject.Inject;

import com.google.common.eventbus.EventBus;

import ch.psi.fda.EContainer;
import ch.psi.fda.EContainerFactory;
import ch.psi.fda.edescriptor.EDescriptor;
import ch.psi.jcae.ChannelService;

public class CdumpEContainerFactory implements EContainerFactory {

	@Inject
	private ChannelService cservice;
	
	@Override
	public boolean supportsEDescriptor(EDescriptor descriptor) {
		return descriptor instanceof CdumpEDescriptor;
	}

	@Override
	public EContainer getEContainer(EDescriptor descriptor, EventBus bus) {
		return new CdumpEContainer(cservice, bus, (CdumpEDescriptor) descriptor);
	}
}
