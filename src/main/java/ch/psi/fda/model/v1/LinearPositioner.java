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
 * <p>Java class for LinearPositioner complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LinearPositioner">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psi.ch/~ebner/models/scan/1.0}DiscreteStepPositioner">
 *       &lt;sequence>
 *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="stepSize" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinearPositioner", propOrder = {
    "start",
    "end",
    "stepSize"
})
public class LinearPositioner
    extends DiscreteStepPositioner
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected double start;
    protected double end;
    protected double stepSize;

    /**
     * Gets the value of the start property.
     * 
     */
    public double getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     */
    public void setStart(double value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     * 
     */
    public double getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     */
    public void setEnd(double value) {
        this.end = value;
    }

    /**
     * Gets the value of the stepSize property.
     * 
     */
    public double getStepSize() {
        return stepSize;
    }

    /**
     * Sets the value of the stepSize property.
     * 
     */
    public void setStepSize(double value) {
        this.stepSize = value;
    }

}