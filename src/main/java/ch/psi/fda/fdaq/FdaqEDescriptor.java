package ch.psi.fda.fdaq;

import javax.xml.bind.annotation.XmlRootElement;

import ch.psi.fda.edescriptor.EDescriptor;

@XmlRootElement(name="fdaq")
public class FdaqEDescriptor implements EDescriptor {

	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
