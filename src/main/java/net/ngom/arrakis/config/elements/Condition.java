package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

@XmlAccessorType(XmlAccessType.FIELD)
public class Condition implements Serializable {
	
	private static final long serialVersionUID = -3356546504062607775L;
	
	@XmlAttribute(name="ref-type")
	private String refType;
	@XmlAttribute(name="operator")
	private String operator;
	@XmlAttribute(name="name")
	private String name;
	@XmlElement(name="match")
	private List<Match> matchList = null;
	
	public Condition() {
		super();
		this.matchList = new ArrayList<Match>();
	}

	public String getRefType() {
		return refType;
	}


	public void setRefType(String refType) {
		this.refType = refType;
	}


	public String getOperator() {
		if(operator == null) return "and";
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	
	public List<Match> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<Match> matchList) {
		this.matchList = matchList;
	}

	public void addMatch(Match match) {
		this.matchList.add(match);
	}
	public void removeMatch(int index) {
		this.matchList.remove(index);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) throws Exception {
		if(matchList != null) {
			for(int i=0; i<matchList.size(); i++) {
				Match match = matchList.get(i);
				String name = match.getName();
				match.setName("name=["+name+"]["+i+"]");
			}
		}
	}
}
