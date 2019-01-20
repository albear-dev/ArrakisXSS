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
public class Conditions implements Serializable {

	private static final long serialVersionUID = -7323257293067851953L;
	
	@XmlElement(name="condition")
	private List<Condition> conditionList = null;
	@XmlAttribute(name="ref-type")
	private String refType;
	
	public Conditions() {
		super();
		conditionList = new ArrayList<Condition>();
	}
	
	public Conditions(Condition[] args) {
		if(args != null && args.length > 0) {
			for(int i=0; i<args.length; i++) {
				conditionList.add(args[i]);
			}
		}
	}

	
	public List<Condition> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<Condition> conditionList) {
		this.conditionList = conditionList;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public void addCondition(Condition cond) {
		conditionList.add(cond);
	}
	
	public Condition getCondition(int i) {
		return conditionList.get(i);
	}
	
	public void clearCondition() {
		conditionList.clear();
	}
	
	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) throws Exception {
		if(conditionList != null) {
			for(int i=0; i<conditionList.size(); i++) {
				Condition condition = conditionList.get(i);
				String name = condition.getName();
				condition.setName("name=["+name+"]["+i+"]");
			}
		}
	}
}
