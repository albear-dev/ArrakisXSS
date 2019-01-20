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
public class Filters implements Serializable  {
	
	private static final long serialVersionUID = -5957135289796833518L;
	
    @XmlElement(name="filter")
	private List<Filter> filterList;
    @XmlAttribute(name="default")
    private String defaultFilter;
    
	
	public Filters() {
		super();
		this.filterList = new ArrayList<Filter>();
	}
	public List<Filter> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
	}

	public Filter getFilter(int index) {
		return this.filterList.get(index);
	}
	
	public Filter getFilter(String filterName) {
		for(Filter filter: this.filterList) {
			if(filterName.equals(filter.getName())){
				return filter;
			}
		}
		return null;
	}

	public String getDefaultFilter() {
		return defaultFilter;
	}
	public void setDefaultFilter(String defaultFilter) {
		this.defaultFilter = defaultFilter;
	}
	public void addFilter(Filter filter) {
		this.filterList.add(filter);
	}
	
	public void removeFilter(int index) {
		this.filterList.remove(index);
	}
	
	public int size() {
		return this.filterList.size();
	}
}
