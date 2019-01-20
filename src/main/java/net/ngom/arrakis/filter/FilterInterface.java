package net.ngom.arrakis.filter;

import net.ngom.arrakis.config.elements.Config;

public interface FilterInterface {
	public abstract Config getConfig();
	public abstract String filter(String srcValue);
}
