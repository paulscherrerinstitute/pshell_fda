//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.27 at 11:18:50 AM CEST 
//


package ch.psi.fda.model.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PseudoPositioner complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PseudoPositioner">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psi.ch/~ebner/models/scan/1.0}DiscreteStepPositioner">
 *       &lt;sequence>
 *         &lt;element name="counts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PseudoPositioner", propOrder = {
    "counts"
})
public class PseudoPositioner
    extends DiscreteStepPositioner
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected int counts;

    /**
     * Gets the value of the counts property.
     * 
     */
    public int getCounts() {
        return counts;
    }

    /**
     * Sets the value of the counts property.
     * 
     */
    public void setCounts(int value) {
        this.counts = value;
    }

}
