package ch.psi.fda.fdaq;

import com.google.common.eventbus.EventBus;

import ch.psi.fda.EContainer;
import ch.psi.fda.EContainerFactory;
import ch.psi.fda.edescriptor.EDescriptor;

public class FdaqEContainerFactory implements EContainerFactory {

	@Override
	public boolean supportsEDescriptor(EDescriptor descriptor) {
		return descriptor instanceof FdaqEDescriptor;
	}

	@Override
	public EContainer getEContainer(EDescriptor descriptor, EventBus bus) {
		return new FdaqEContainer((FdaqEDescriptor) descriptor, bus);
	}
}
