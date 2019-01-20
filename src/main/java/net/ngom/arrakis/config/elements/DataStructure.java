package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataStructure implements Serializable {

	private static final long serialVersionUID = -3568754380552844788L;

	@XmlElement(name="structureType")
	private List<StructureType> structureTypeList = null;

	public DataStructure() {
		super();
		this.structureTypeList = new ArrayList<StructureType>();
	}
	
	
	
	public List<StructureType> getStructureTypeList() {
		return structureTypeList;
	}



	public void setStructureTypeList(List<StructureType> structureTypeList) {
		this.structureTypeList = structureTypeList;
	}



	public void addStructureType(StructureType structureType) {
		this.structureTypeList.add(structureType);
	}
	public void removeStructureType(int index) {
		this.structureTypeList.remove(index);
	}
	
}