package net.ngom.arrakis.datastructure;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.elements.Match;

public class ConditionMatchValueRequestMap implements ConditionMatchValue, Serializable {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	private static final long serialVersionUID = -6246777629771266144L;
	private final String structureId = "RequestMap";
	
	private Map<String, String[]> parametorMap;
	
	public ConditionMatchValueRequestMap(HttpServletRequest httpServletRequest) {
		this.parametorMap = httpServletRequest.getParameterMap();
	}
	
	public ConditionMatchValueRequestMap(Map<String, String[]> parametorMap ) {
		this.parametorMap = parametorMap;
	}

	public String getStructureId() {
		return structureId;
	}

	public Map<String, String[]> getParametorMap() {
		return parametorMap;
	}

	public void setParametorMap(Map<String, String[]> parametorMap) {
		this.parametorMap = parametorMap;
	}

	public Boolean isMatch(Match match) {
		String[] paramValues = parametorMap.get(match.getKeyName());
		if(paramValues == null || paramValues.length == 0) {
			return false;
		}else if(paramValues.length == 1) {
			return match.compare(paramValues[0]);
		}else {
			StringBuffer sb = new StringBuffer();
			for(String param: paramValues) {
				sb.append(param + ",");
			}
			sb.deleteCharAt(sb.length());
			return match.compare(sb.toString());
		}
	}
	
	/*
	public Boolean isMatch(Match match) {
		String[] paramValues = parametorMap.get(match.getKeyName());
		if(paramValues == null || paramValues.length == 0) {
			return false;
		}else if(paramValues.length == 1) {
			return match.compare(paramValues[0]);
		}else {
			StringBuffer sb = new StringBuffer();
			for(String param: paramValues) {
				sb.append(param + ",");
			}
			sb.deleteCharAt(sb.length());
			return match.compare(sb.toString());
		}
	}
	*/

	public String getValue(String key) {
		return Arrays.toString(parametorMap.get(key));
	}

	public String[] getValues(String key) {
		return parametorMap.get(key);
	}
	
}
