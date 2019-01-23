package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlAccessorType(XmlAccessType.FIELD)
public class Match implements Serializable {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");

	private static final long serialVersionUID = 2177753778809321521L;

	@XmlAttribute(name = "ref-type")
	private String refType;
	@XmlAttribute(name = "key-name")
	private String keyName;
	@XmlAttribute(name = "operator")
	private String operator;
	@XmlAttribute(name = "exclude")
	private Boolean exclude;
	@XmlAttribute(name = "filter")
	private String filter;
	@XmlAttribute(name = "pattern")
	private String pattern;
	@XmlAttribute(name = "replace-to")
	private String replaceTo;
	@XmlAttribute(name = "match-type")
	private String matchType;
	@XmlAttribute(name = "name")
	private String name;
	
	@XmlValue
	private String value;

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Boolean getExclude() {
		return this.exclude==null?false:this.exclude;
	}

	public void setExclude(Boolean exclude) {
		this.exclude = exclude;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getReplaceTo() {
		return replaceTo;
	}

	public void setReplaceTo(String replaceTo) {
		this.replaceTo = replaceTo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMatchType() {
		if(this.matchType == null) return "exactly";
		return this.matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private boolean _compare(String srcValue) {
		logger.debug("[_compare] start!");
		logger.debug("[_compare] srcValue [" +srcValue+"] matchSetvalue [" +this.value+"] matchType["+this.matchType+"]");
		
		if(srcValue == null) return false;
		
		if (this.matchType == null || "exactly".equals(this.matchType)) {
			return srcValue.equals(this.value);
			
		} else if ("exactly".equals(this.matchType)) {
			return srcValue.equals(this.value);
			
		} else if ("startWith".equals(this.matchType)) {
			return srcValue.startsWith(this.value);
			
		} else if ("endWith".equals(this.matchType)) {
			return srcValue.endsWith(this.value);
			
		} else if ("contains".equals(this.matchType)) {
			return srcValue.indexOf(this.value)==-1?false:true;
			
		} else if ("regex".equals(this.matchType)) {
			Pattern p = null;
			if(this.pattern != null) {
				p = Pattern.compile(this.pattern);
			}else {
				p = Pattern.compile(this.value);
			}
			return p.matcher(srcValue).find();
		} else {
			return false;
		}
	}
	
	public boolean compare(String srcValue) {
		boolean result = _compare(srcValue);
		if("not".equals(this.operator)) {
			return !result;
		}
		
		logger.debug("[compare] result["+result+"]");
		return result;
	}
	
	private boolean _arrayContain(String[] srcValues) {
		logger.debug("[_arrayContain] start!");
		logger.debug("[_arrayContain] srcValues [" +Arrays.toString(srcValues)+"] matchSetvalue [" +this.value+"] matchType["+this.matchType+"]");
		
		if(srcValues == null || srcValues.length == 0) return false;
		
		boolean isContain = false;
		for(int i=0; i<srcValues.length; i++) {
			if (this.matchType == null || "exactly".equals(this.matchType)) {
				isContain = srcValues[i].equals(this.value);
				
			} else if ("exactly".equals(this.matchType)) {
				isContain = srcValues[i].equals(this.value);
				
			} else if ("startWith".equals(this.matchType)) {
				isContain = srcValues[i].startsWith(this.value);
				
			} else if ("endWith".equals(this.matchType)) {
				isContain = srcValues[i].endsWith(this.value);
				
			} else if ("contains".equals(this.matchType)) {
				isContain = srcValues[i].indexOf(this.value)==-1?false:true;
				
			} else if ("regex".equals(this.matchType)) {
				Pattern p = null;
				if(this.pattern != null) {
					p = Pattern.compile(this.pattern);
				}else {
					p = Pattern.compile(this.value);
				}
				isContain = p.matcher(srcValues[i]).find();
			} else {
				isContain = false;
			}
			
			if(isContain) {
				break;
			}
		}
		
		return isContain;
	}
	
	public boolean arrayContain(String[] srcValues) {
		boolean result = _arrayContain(srcValues);
		if("not".equals(this.operator)) {
			return !result;
		}
		
		logger.debug("[arrayContain] result["+result+"]");
		return result;
	}

}