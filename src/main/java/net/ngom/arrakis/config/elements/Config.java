package net.ngom.arrakis.config.elements;

import java.io.Serializable;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "config")
public class Config implements Serializable{
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	private static final long serialVersionUID = 8709582427864655264L;
	private Filters filters;
	private Appliers appliers;
	private Rules rules;
	private String characterEncoding;
	
	public Config() {
        super();
    }
	
	public Filters getFilters() {
		return filters;
	}

	public void setFilters(Filters filters) {
		this.filters = filters;
	}

	public Appliers getAppliers() {
		return appliers;
	}

	public void setAppliers(Appliers appliers) {
		this.appliers = appliers;
	}
	
	public Rules getRules() {
		return rules;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}
	

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	// It is called immediately after the object is created and before the unmarshalling begins.
    // The callback provides an opportunity to initialize JavaBean properties prior to unmarshalling.
    void beforeUnmarshal(Unmarshaller unmarshaller, Object parent) {
    	logger.debug("Before Unmarshaller Callback");
    }
 
    // It is called after all the properties are unmarshalled for this object,
    // but before this object is set to the parent object.
    // xml -> object
    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
    	logger.debug("After Unmarshaller Callback");
        
    }
}
