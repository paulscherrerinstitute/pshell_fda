//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.27 at 11:18:50 AM CEST 
//


package ch.psi.fda.model.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContinuousDimension complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContinuousDimension">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psi.ch/~ebner/models/scan/1.0}Dimension">
 *       &lt;sequence>
 *         &lt;element name="preAction" type="{http://www.psi.ch/~ebner/models/scan/1.0}Action" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="positioner" type="{http://www.psi.ch/~ebner/models/scan/1.0}ContinuousPositioner"/>
 *         &lt;element name="detector" type="{http://www.psi.ch/~ebner/models/scan/1.0}SimpleScalarDetector" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="scaler" type="{http://www.psi.ch/~ebner/models/scan/1.0}ScalerChannel" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="timestamp" type="{http://www.psi.ch/~ebner/models/scan/1.0}Timestamp" minOccurs="0"/>
 *         &lt;element name="postAction" type="{http://www.psi.ch/~ebner/models/scan/1.0}Action" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="rasterize" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContinuousDimension", propOrder = {
    "preAction",
    "positioner",
    "detector",
    "scaler",
    "timestamp",
    "postAction"
})
public class ContinuousDimension
    extends Dimension
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<Action> preAction;
    @XmlElement(required = true)
    protected ContinuousPositioner positioner;
    protected List<SimpleScalarDetector> detector;
    protected List<ScalerChannel> scaler;
    protected Timestamp timestamp;
    protected List<Action> postAction;
    @XmlAttribute(name = "rasterize")
    protected Boolean rasterize;

    /**
     * Gets the value of the preAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the preAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Action }
     * 
     * 
     */
    public List<Action> getPreAction() {
        if (preAction == null) {
            preAction = new ArrayList<Action>();
        }
        return this.preAction;
    }

    /**
     * Gets the value of the positioner property.
     * 
     * @return
     *     possible object is
     *     {@link ContinuousPositioner }
     *     
     */
    public ContinuousPositioner getPositioner() {
        return positioner;
    }

    /**
     * Sets the value of the positioner property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContinuousPositioner }
     *     
     */
    public void setPositioner(ContinuousPositioner value) {
        this.positioner = value;
    }

    /**
     * Gets the value of the detector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SimpleScalarDetector }
     * 
     * 
     */
    public List<SimpleScalarDetector> getDetector() {
        if (detector == null) {
            detector = new ArrayList<SimpleScalarDetector>();
        }
        return this.detector;
    }

    /**
     * Gets the value of the scaler property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scaler property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScaler().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScalerChannel }
     * 
     * 
     */
    public List<ScalerChannel> getScaler() {
        if (scaler == null) {
            scaler = new ArrayList<ScalerChannel>();
        }
        return this.scaler;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link Timestamp }
     *     
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Timestamp }
     *     
     */
    public void setTimestamp(Timestamp value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the postAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the postAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPostAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Action }
     * 
     * 
     */
    public List<Action> getPostAction() {
        if (postAction == null) {
            postAction = new ArrayList<Action>();
        }
        return this.postAction;
    }

    /**
     * Gets the value of the rasterize property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRasterize() {
        if (rasterize == null) {
            return false;
        } else {
            return rasterize;
        }
    }

    /**
     * Sets the value of the rasterize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRasterize(Boolean value) {
        this.rasterize = value;
    }

}
