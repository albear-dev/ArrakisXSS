package net.ngom.arrakis.datastructure;

import net.ngom.arrakis.config.elements.Match;

public interface ConditionMatchValue {
	public abstract String getStructureId();
	public abstract String getValue(String key);
	public abstract String[] getValues(String key);
	public abstract Boolean isMatch(Match match);
	
}
