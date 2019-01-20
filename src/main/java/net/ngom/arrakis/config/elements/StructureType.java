package net.ngom.arrakis.config.elements;

import java.io.Serializable;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.datastructure.ConditionMatchValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class StructureType implements Serializable {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	
	private static final long serialVersionUID = 4716317736911847468L;
	
	@XmlAttribute(name="id")
	private String id;
	private String structureClass;
	private ConditionMatchValue structureClazz;
	private String retriveClass;
	private ConditionMatchValue retriveClazz;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStructureClass() {
		return structureClass;
	}

	public void setStructureClass(String structureClass) {
		this.structureClass = structureClass;
	}

	public String getRetriveClass() {
		return retriveClass;
	}

	public void setRetriveClass(String retriveClass) {
		this.retriveClass = retriveClass;
	}
	
	public ConditionMatchValue getStructureInstance() {
		return structureClazz;
	}

	public void setStructureInstance(ConditionMatchValue structureClazz) {
		this.structureClazz = structureClazz;
	}

	public ConditionMatchValue getRetriveInstance() {
		return retriveClazz;
	}

	public void setRetriveInstance(ConditionMatchValue retriveClazz) {
		this.retriveClazz = retriveClazz;
	}

	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) throws Exception {
		this.structureClazz = (ConditionMatchValue) Class.forName(structureClass).newInstance();
		//this.retriveClazz = (ConditionMatchValue)Class.forName("��Ÿ�� �� �����Ǵ� Ŭ������ �̸�").newInstance();
		
		logger.debug("After Unmarshaller Callback");
    }
	
}