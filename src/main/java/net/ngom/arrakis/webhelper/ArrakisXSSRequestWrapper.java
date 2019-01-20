package net.ngom.arrakis.webhelper;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.ArrakisXSS;
import net.ngom.arrakis.datastructure.ConditionMatchValueCollection;
import net.ngom.arrakis.datastructure.ConditionMatchValueRequestMap;
import net.ngom.arrakis.datastructure.ConditionMatchValueRequestMetaMap;

public class ArrakisXSSRequestWrapper extends HttpServletRequestWrapper {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	
	private ArrakisXSS xss;
	private ConditionMatchValueCollection conditionMatchValueCollection;
	
	public ArrakisXSSRequestWrapper(ArrakisXSS xss, ServletRequest request) throws ServletException {
		super((HttpServletRequest)request);
		try {
			logger.debug("ArrakisXSS RequestWrapper constructor create");
			this.xss = xss;
			this.conditionMatchValueCollection = new ConditionMatchValueCollection(
				new ConditionMatchValueRequestMap((HttpServletRequest)this.getRequest()),
				new ConditionMatchValueRequestMetaMap((HttpServletRequest)this.getRequest())
			);
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}
	
	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		
		return xss.filtering(this.conditionMatchValueCollection, parameter, value);
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		
		for(int i=0; i<values.length; i++) {
			values[i] = xss.filtering(this.conditionMatchValueCollection, parameter, values[i]);
		}
		
		return values;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramMap = super.getParameterMap();
		
		for( String key : paramMap.keySet() ){
			String[] values = paramMap.get(key);
			if(values != null && values.length > 0) {
				for(int i=0; i<values.length; i++) {
					values[i] = xss.filtering(this.conditionMatchValueCollection, key, values[i]);
				}
				paramMap.put(key, values);
			}
        }
		
		return paramMap;
	}
	

	@Override
	public String getHeader(String name) {
		return super.getHeader(name);
	}

}