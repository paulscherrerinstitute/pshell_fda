package ch.psi.fda.cdump;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ch.psi.fda.DescriptorProvider;
import ch.psi.fda.edescriptor.EDescriptor;
import ch.psi.fda.vdescriptor.VDescriptor;

public class CdumpEDescriptorProvider implements DescriptorProvider {

	private EDescriptor edescriptor;
	
	@Override
	public void load(File... files) {
		try {
			JAXBContext context = JAXBContext.newInstance(CdumpEDescriptor.class);
			Unmarshaller u = context.createUnmarshaller();
			edescriptor = (EDescriptor) u.unmarshal(files[0]);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public EDescriptor getEDescriptor() {
		return edescriptor;
	}

	@Override
	public VDescriptor getVDescriptor() {
		return null;
	}

	@Override
	public Class<?> getEDescriptorClass() {
		return CdumpEDescriptor.class;
	}

}
