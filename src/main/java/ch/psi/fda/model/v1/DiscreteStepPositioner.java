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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DiscreteStepPositioner complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DiscreteStepPositioner">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psi.ch/~ebner/models/scan/1.0}Positioner">
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="readback" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="settlingTime" default="0">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;minInclusive value="0"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="done" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="doneValue" type="{http://www.w3.org/2001/XMLSchema}string" default="1" />
 *       &lt;attribute name="type" default="Integer">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="String"/>
 *             &lt;enumeration value="Integer"/>
 *             &lt;enumeration value="Double"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="doneDelay" default="0">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;minInclusive value="0"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="asynchronous" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiscreteStepPositioner")
@XmlSeeAlso({
    FunctionPositioner.class,
    PseudoPositioner.class,
    LinearPositioner.class,
    ArrayPositioner.class,
    RegionPositioner.class
})
public abstract class DiscreteStepPositioner
    extends Positioner
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "readback")
    protected String readback;
    @XmlAttribute(name = "settlingTime")
    protected Double settlingTime;
    @XmlAttribute(name = "done")
    protected String done;
    @XmlAttribute(name = "doneValue")
    protected String doneValue;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "doneDelay")
    protected Double doneDelay;
    @XmlAttribute(name = "asynchronous")
    protected Boolean asynchronous;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the readback property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReadback() {
        return readback;
    }

    /**
     * Sets the value of the readback property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReadback(String value) {
        this.readback = value;
    }

    /**
     * Gets the value of the settlingTime property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getSettlingTime() {
        if (settlingTime == null) {
            return  0.0D;
        } else {
            return settlingTime;
        }
    }

    /**
     * Sets the value of the settlingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSettlingTime(Double value) {
        this.settlingTime = value;
    }

    /**
     * Gets the value of the done property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDone() {
        return done;
    }

    /**
     * Sets the value of the done property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDone(String value) {
        this.done = value;
    }

    /**
     * Gets the value of the doneValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoneValue() {
        if (doneValue == null) {
            return "1";
        } else {
            return doneValue;
        }
    }

    /**
     * Sets the value of the doneValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoneValue(String value) {
        this.doneValue = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "Integer";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the doneDelay property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getDoneDelay() {
        if (doneDelay == null) {
            return  0.0D;
        } else {
            return doneDelay;
        }
    }

    /**
     * Sets the value of the doneDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDoneDelay(Double value) {
        this.doneDelay = value;
    }

    /**
     * Gets the value of the asynchronous property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAsynchronous() {
        if (asynchronous == null) {
            return false;
        } else {
            return asynchronous;
        }
    }

    /**
     * Sets the value of the asynchronous property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAsynchronous(Boolean value) {
        this.asynchronous = value;
    }

}
