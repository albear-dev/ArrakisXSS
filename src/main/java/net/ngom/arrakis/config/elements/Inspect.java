package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Inspect implements Serializable {

	private static final long serialVersionUID = -719853208272290485L;

	@XmlElement(name="rule")
	private List<InspectRule> ruleList;
	@XmlAttribute(name="filter")
	private String filter;
	@XmlAttribute(name="exclude")
	private Boolean exclude;
	@XmlAttribute(name="name")
	private String name;
	
	public Inspect() {
		super();
		this.ruleList = new ArrayList<InspectRule>();
	}
	public List<InspectRule> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<InspectRule> ruleList) {
		this.ruleList = ruleList;
	}

	public void addRule(InspectRule match) {
		this.ruleList.add(match);
	}
	public void removeRule(int index) {
		this.ruleList.remove(index);
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	public Boolean getExclude() {
		return this.exclude==null?false:this.exclude;
	}
	public void setExclude(Boolean exclude) {
		this.exclude = exclude;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) throws Exception {
		if(ruleList != null) {
			for(int i=0; i<ruleList.size(); i++) {
				InspectRule match = ruleList.get(i);
				String name = match.getName();
				match.setName("name=["+name+"]["+i+"]");
			}
		}
	}
	
	
}