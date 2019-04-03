package ch.psi.fda;

import ch.psi.fda.edescriptor.EDescriptor;

import com.google.common.eventbus.EventBus;

public interface EContainerFactory {

	/**
	 * Check whether the factory supports 
	 * @param descriptor
	 * @return
	 */
	public boolean supportsEDescriptor(EDescriptor descriptor);
	
	/**
	 * Create the execution container based on the passed descriptor
	 * @param descriptor
	 * @return
	 */
	public EContainer getEContainer(EDescriptor descriptor, EventBus bus);
}
