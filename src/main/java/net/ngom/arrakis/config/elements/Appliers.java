package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Appliers implements Serializable {

	private static final long serialVersionUID = -6247242598896935078L;

	@XmlElement(name = "applier")
	private List<Applier> applierList;

	public Appliers() {
		this.applierList = new ArrayList<Applier>();
	}

	public List<Applier> getApplierList() {
		return applierList;
	}

	public void setApplierList(List<Applier> applierList) {
		this.applierList = applierList;
	}

	public void addApplier(Applier applier) {
		this.applierList.add(applier);
	}

	public Applier getApplier(int index) {
		return applierList.get(index);
	}

	public void removeApplier(int index) {
		applierList.remove(index);
	}

	public int size() {
		return applierList.size();
	}

	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) throws Exception {
		if(applierList != null) {
			for(int i=0; i<applierList.size(); i++) {
				Applier applier = applierList.get(i);
				String name = applier.getName();
				applier.setName("name=["+name+"]["+i+"]");
			}
		}
	}
}
