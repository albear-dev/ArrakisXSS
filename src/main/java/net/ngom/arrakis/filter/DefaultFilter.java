package net.ngom.arrakis.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.elements.Config;
import net.ngom.arrakis.config.elements.Rule;
import net.ngom.arrakis.config.elements.Rules;

public abstract class DefaultFilter implements FilterInterface {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	
	private Config config;
	
	public DefaultFilter(Config config) {
		logger.debug("Init DefaultFilter. config is null? " + (config==null?true:false));
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public String filter(String srcValue) {
		Rules rules = this.getConfig().getRules();
		String tempValue = srcValue;
		String pattern;
		String replaceTo;
		logger.debug("beforeString is  " + srcValue);
		logger.debug("Rule is null? " + (rules==null?true:false));
		for(Rule rule: rules.getRuleList()) {
			pattern 	= rule.getPattern();
			if(pattern == null || "".equals(pattern.trim())) {
				logger.debug("Rule > pattern is not defined. skip rule match.");
				continue;
			}
			replaceTo 	= rule.getReplaceTo();
			if(replaceTo == null || "".equals(replaceTo.trim())) {
				logger.debug("Rule > replace-to is not defined. skip rule match.");
				continue;
			}
			logger.debug("[C:FilteringHelper][v:condition[M:doInspect[M:passFilter[C:DefaultFilter[M:filter]]]]] ruleName["+rule.getName()+"] pattern["+pattern+"] replaceTo["+replaceTo+"]");
			tempValue 	= tempValue.replaceAll(pattern, replaceTo);
		}
		
		return tempValue;
	}
	
}
