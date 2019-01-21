package net.ngom.arrakis.filter;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.elements.Config;
import net.ngom.arrakis.filter.DefaultFilter;

public class SQLInjectionFilter extends DefaultFilter{
	static final Logger logger = LoggerFactory.getLogger("enXSS");
	
	public SQLInjectionFilter(Config config) {
		super(config);
		logger.debug("init SQLInjectionFilter. config is null? " + (config==null?true:false));
	}
	
	@Override
	public String filter(String userInput) {
		logger.debug("SQLInjectionFilter. userInput["+userInput+"]");
		/* ?��?��문자 공백 처리 */
		final Pattern pattern1 = Pattern.compile("['\"\\-#()@;=*/+]");
		userInput = pattern1.matcher(userInput).replaceAll("");
		 
		final Pattern pattern2 = Pattern.compile("(union|select|from|where)", Pattern.CASE_INSENSITIVE);
		userInput = pattern2.matcher(userInput).replaceAll("");
		
		logger.debug("SQLInjectionFilter. userInput result ["+userInput+"]");
		return userInput;
	}
}
