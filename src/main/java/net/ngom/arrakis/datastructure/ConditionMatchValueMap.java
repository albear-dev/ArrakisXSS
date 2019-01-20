package net.ngom.arrakis.datastructure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.ngom.arrakis.config.elements.Match;

public class ConditionMatchValueMap implements ConditionMatchValue, Serializable {

	private static final long serialVersionUID = 3261376209446910612L;

	private final String structureId = "Map";
	
	private Map<String, String> map;
	
	public ConditionMatchValueMap(final Map<String, String> map) throws Exception {
		if(this.map == null) {
			this.map = new HashMap<String, String>();
		}else {
			this.map = map.getClass().newInstance();
			this.map.putAll(map);
		}
	}

	public Map<String, String> getmap() {
		return map;
	}

	public void setmap(Map<String, String> map) {
		this.map = map;
	}
	
	public String getStructureId() {
		return structureId;
	}
	
	public Boolean isMatch(Match match) {
		return match.compare(map.get(match.getKeyName()));
	}

	public String getValue(String key) {
		return map.get(key);
	}

	public String[] getValues(String key) {
		return new String[] { map.get(key)};
	}

	
}
