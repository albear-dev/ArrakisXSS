package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlAccessorType(XmlAccessType.FIELD)
public class InspectRule implements Serializable {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");

	private static final long serialVersionUID = 2177753778809321521L;

	@XmlAttribute(name = "ref-type")
	private String refType;
	@XmlAttribute(name = "key-name")
	private String keyName;
	@XmlAttribute(name = "operator")
	private String operator;
	@XmlAttribute(name = "exclude")
	private Boolean exclude;
	@XmlAttribute(name = "filter")
	private String filter;
	@XmlElement(name="pattern")
	private String pattern;
	@XmlElement(name="replace-to")
	private String replaceTo;
	@XmlAttribute(name = "pattern")
	private String patternAttr;
	@XmlAttribute(name = "replace-to")
	private String replaceToAttr;
	@XmlAttribute(name = "match-type")
	private String matchType;
	@XmlAttribute(name = "name")
	private String name;

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Boolean getExclude() {
		return this.exclude==null?false:this.exclude;
	}

	public void setExclude(Boolean exclude) {
		this.exclude = exclude;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

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
	
	public String getMatchType() {
		if(this.matchType == null) return "exactly";
		return this.matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}