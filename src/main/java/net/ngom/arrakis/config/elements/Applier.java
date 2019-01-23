package net.ngom.arrakis.config.elements;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Applier implements Serializable {
	
	private static final long serialVersionUID = -8851595072322289289L;
	private Conditions conditions;
	private Inspect inspect;
	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "break")
	private Boolean matchBreak;
	
	
	public Conditions getConditions() {
		return conditions;
	}
	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}
	public Inspect getInspect() {
		return inspect;
	}
	public void setInspect(Inspect inspect) {
		this.inspect = inspect;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getMatchBreak() {
		if(this.matchBreak == null) return false;
		return matchBreak;
	}
	public void setMatchBreak(Boolean matchBreak) {
		this.matchBreak = matchBreak;
	}
	
}
