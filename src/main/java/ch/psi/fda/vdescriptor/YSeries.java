package ch.psi.fda.vdescriptor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * In a YSeries the Y components hold an data array in which the index i the x and the value the y value.
 */
@XmlRootElement(name="yseries")
public class YSeries extends Series {

	private String y = null;
	
	public YSeries(){
	}
	
	public YSeries(String y){
		this.y = y;
	}
	
	@XmlAttribute
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
}
