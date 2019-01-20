package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Rules implements Serializable {

	private static final long serialVersionUID = -6055132043601034273L;

	@XmlElement(name = "rule")
	private List<Rule> ruleList;

	public List<Rule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Rule> ruleList) {
		this.ruleList = ruleList;
	}

	public void addRule(Rule rule) {
		this.ruleList.add(rule);
	}

	public Rule getRule(int index) {
		return ruleList.get(index);
	}

	public void removeRule(int index) {
		ruleList.remove(index);
	}

	public int size() {
		return ruleList.size();
	}
	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) throws Exception {
		if(ruleList != null) {
			for(int i=0; i<ruleList.size(); i++) {
				Rule rule = ruleList.get(i);
				String name = rule.getName();
				rule.setName("name=["+name+"]["+i+"]");
			}
		}
	}
}
