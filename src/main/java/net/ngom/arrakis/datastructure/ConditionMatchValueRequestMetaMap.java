package net.ngom.arrakis.datastructure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.ngom.arrakis.config.elements.Match;

public class ConditionMatchValueRequestMetaMap implements ConditionMatchValue, Serializable {
	private static final long serialVersionUID = -6246777629771266144L;
	private final String structureId = "RequestMetaMap";
	
	private Map<String, String> requestMetaMap;
	
	public ConditionMatchValueRequestMetaMap(HttpServletRequest httpServletRequest) {
		if(this.requestMetaMap == null) {
			this.requestMetaMap = new HashMap<String, String>();
		}
		this.requestMetaMap.put("URI", httpServletRequest.getRequestURI());
	}

	public Map<String, String> getRequestMetaMap() {
		return requestMetaMap;
	}

	public void setRequestMetaMap(Map<String, String> requestMetaMap) {
		this.requestMetaMap = requestMetaMap;
	}

	public String getStructureId() {
		return structureId;
	}

	public Boolean isMatch(Match match) {
		return match.compare(requestMetaMap.get(match.getKeyName()));
	}

	public String getValue(String key) {
		return requestMetaMap.get(key);
	}

	public String[] getValues(String key) {
		return new String[] { requestMetaMap.get(key)};
	}

	
}
