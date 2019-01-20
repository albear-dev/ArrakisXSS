package net.ngom.arrakis.config.elements;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlAccessorType(XmlAccessType.FIELD)
public class Rule {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	
	@XmlElement(name="pattern")
	private String pattern;
	@XmlElement(name="replace-to")
	private String replaceTo;
	@XmlAttribute(name = "pattern")
	private String patternAttr;
	@XmlAttribute(name = "replace-to")
	private String replaceToAttr;
	@XmlAttribute(name = "name")
	private String name;
	
	public String getPattern() {
		if(pattern == null) return patternAttr;
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getReplaceTo() {
		if(replaceTo == null) return replaceToAttr;
		return replaceTo;
	}
	public void setReplaceTo(String replaceTo) {
		this.replaceTo = replaceTo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getPatternAttr() {
		return patternAttr;
	}
	public void setPatternAttr(String patternAttr) {
		this.patternAttr = patternAttr;
	}
	public String getReplaceToAttr() {
		return replaceToAttr;
	}
	public void setReplaceToAttr(String replaceToAttr) {
		this.replaceToAttr = replaceToAttr;
	}
	// It is called after all the properties are unmarshalled for this object,
    // but before this object is set to the parent object.
    // xml -> object
    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
    	logger.debug("afterUnmarshal pattern : " + pattern);
    	logger.debug("afterUnmarshal replaceTo : " + replaceTo);
    }
}
