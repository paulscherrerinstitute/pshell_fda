package ch.psi.fda.aq;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ch.psi.fda.edescriptor.EDescriptor;
import ch.psi.fda.model.v1.Configuration;

@XmlRootElement(name="edescriptor")
@XmlType(name="edescriptor")
public class XScanDescriptor implements EDescriptor {

	private Configuration configuration;

	public XScanDescriptor(){
	}
	
	public XScanDescriptor(Configuration configuration){
		this.configuration = configuration;
	}

	
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
