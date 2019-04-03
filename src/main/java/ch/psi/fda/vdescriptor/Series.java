package ch.psi.fda.vdescriptor;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;


@XmlSeeAlso({XYSeries.class, XYZSeries.class})
@XmlTransient
public abstract class Series {
	
	
}
