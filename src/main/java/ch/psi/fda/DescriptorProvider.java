package ch.psi.fda;

import java.io.File;

import ch.psi.fda.edescriptor.EDescriptor;
import ch.psi.fda.vdescriptor.VDescriptor;

public interface DescriptorProvider {

	public void load(File ... files );
	
	public EDescriptor getEDescriptor();
	public VDescriptor getVDescriptor();
	
	public Class<?> getEDescriptorClass();
	
}
