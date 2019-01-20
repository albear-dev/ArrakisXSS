package net.ngom.arrakis.datastructure;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionMatchValueCollection {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	
	private Map<String, ConditionMatchValue> conditionMatchValueMap;
	
	public ConditionMatchValueCollection(ConditionMatchValue... conditionMatchValue) {
		
		if(this.conditionMatchValueMap == null) {
			this.conditionMatchValueMap = new HashMap<String, ConditionMatchValue>();
		}
		
		for(ConditionMatchValue obj: conditionMatchValue) {
			this.conditionMatchValueMap.put(obj.getStructureId(), obj);
		}
		
		logger.debug("ConditionMatchValueCollection created!");
	}
	
	public ConditionMatchValue getConditionMatchValue(String structureId) {
		return this.conditionMatchValueMap.get(structureId);
	}

	public void setConditionMatchValue(ConditionMatchValue conditionMatchValue) {
		if(this.conditionMatchValueMap == null) {
			this.conditionMatchValueMap = new HashMap<String, ConditionMatchValue>();
		}
		this.conditionMatchValueMap.put(conditionMatchValue.getStructureId(), conditionMatchValue);
	}
	
	public void setConditionMatchValue(ConditionMatchValue... conditionMatchValue) {
		if(conditionMatchValue != null) {
			for(ConditionMatchValue obj: conditionMatchValue) {
				this.conditionMatchValueMap.put(obj.getStructureId(), obj);
			}
		}
	}
	
	public Boolean IsConditionMatchValueExists() {
		if(this.conditionMatchValueMap == null || conditionMatchValueMap.size() == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public void clearConditionMatchValueMap() {
		if(this.conditionMatchValueMap != null) {
			this.conditionMatchValueMap.clear();
		}
	}
	
	
}
