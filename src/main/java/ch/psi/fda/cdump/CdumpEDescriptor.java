package ch.psi.fda.cdump;

import javax.xml.bind.annotation.XmlRootElement;

import ch.psi.fda.edescriptor.EDescriptor;

@XmlRootElement(name="cdump")
public class CdumpEDescriptor implements EDescriptor {

	private String samplingRate;
	private String fileName;
	
	public String getSamplingRate() {
		return samplingRate;
	}
	public void setSamplingRate(String samplingRate) {
		this.samplingRate = samplingRate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
