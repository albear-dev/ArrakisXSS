package net.ngom.arrakis.filter;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.elements.Applier;
import net.ngom.arrakis.config.elements.Condition;
import net.ngom.arrakis.config.elements.Config;
import net.ngom.arrakis.config.elements.Filter;
import net.ngom.arrakis.config.elements.Inspect;
import net.ngom.arrakis.config.elements.InspectRule;
import net.ngom.arrakis.config.elements.Match;
import net.ngom.arrakis.datastructure.ConditionMatchValue;
import net.ngom.arrakis.datastructure.ConditionMatchValueCollection;

public class FilteringHelper {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	
	private Config config;
	public FilteringHelper(Config config) {
		this.config = config;
	}
	
	public String filtering(ConditionMatchValueCollection cmvc, String paramKey, String value) {
		if(value == null || "".contentEquals(value.trim())) {
			logger.debug("current value is empty(null or blank or only spaces.. key["+paramKey+"] value["+value+"]");
			return value;
		}
		if(cmvc == null || !cmvc.IsConditionMatchValueExists()) {
			logger.error("ConditionMatchValueCollection is null or not have a matchValues. filtering will return original value.");
			return value;
		}
		
		List<Applier> applierList 	= this.config.getAppliers().getApplierList();
		String tempFilteredValue = value;
		for(Applier applier: applierList) {
			logger.info("======[StartFiltering]======");
			logger.debug("[FilteringHelper] applierName : " + applier.getName());
			if(applier.getConditions() == null) {
				logger.debug("[FilteringHelper] <contions> is not exists. apply all filtering values.");
				//return doInspect(applier, paramKey, tempFilteredValue);
				tempFilteredValue = doInspect(applier, paramKey, tempFilteredValue);
				
				// condition이 없으면 바로 inspect를 실행하고 종료.
				// 먼저 applier가 실행되면 뒤쪽은 무시된다.
				break;
			}
			
			List<Condition> conditionList = applier.getConditions().getConditionList();
			String conditionsRefType 	= applier.getConditions().getRefType();
			
			boolean isConditionMatched = false;
			for(Condition condition: conditionList) {
				logger.debug("[FilteringHelper][condition] Loop Condition["+condition.getName()+"]");
				isConditionMatched = doConditionMatch(cmvc, condition, conditionsRefType, paramKey, value);
				if(isConditionMatched) {
					logger.debug("[FilteringHelper][condition] Condition Matched!");
					
					logger.debug("[inspect] doConditionMatched (true) !!!!!");
					logger.debug("[inspect] paramKey["+paramKey+"] value["+tempFilteredValue+"]");
					String inspectResultValue = doInspect(applier, paramKey, tempFilteredValue);
					logger.debug("[inspect] doConditionMatched (true) return value ["+inspectResultValue+"]");
					//return inspectResultValue;
					tempFilteredValue = inspectResultValue;
					
					// 각 condition 끼리는 or 조건으로 한개가 맞으면 inspect를 실행하고 종료.
					break;
				}
			}
			
			// 특정 appliers 에서 컨디션이 맞아서 처리가 되면 이후는 실행하지 않는다.
			if(isConditionMatched && applier.getMatchBreak()) {
				logger.debug("[inspect] applier.getMatchBreak is ("+applier.getMatchBreak()+") next applier not execute.");
				break;
			}
		}
		
		logger.debug("[inspect] doConditionMatched (false) return value ["+tempFilteredValue+"]");
		
		return tempFilteredValue;
	}
	
	
	// 현재 filtering 에서 key/value가 들어오므로 이 값으로 비교해야 한다.
	// ConditionMatchValueCollection으로 비교하는건 이상함...
	private boolean doConditionMatch(ConditionMatchValueCollection cmvc, Condition condition, String conditionsRefType, String paramKey, String value) {
		logger.debug("[FilteringHelper][condition[doConditionMatch]] doConditionMatch Start!");
		
		String conditionRefType = condition.getRefType();
		String operator = condition.getOperator();
		ConditionMatchValue conditionMatchValue = null;
		boolean isConditionMatched = false;
		
		logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch]] conditionName["+condition.getName()+"] conditionRefType["+conditionRefType+"] operator["+operator+"]");
		
		for(Match match: condition.getMatchList()) {
			logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] matchName["+match.getName()+"]");
			String matchRefType = match.getRefType();
			conditionMatchValue = cmvc.getConditionMatchValue(pickRefType(conditionsRefType, conditionRefType, matchRefType));
			
			logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] conditionMatchValue is null["+(conditionMatchValue==null?true:false)+"]");
			
			if(conditionMatchValue == null) {
				
				// 등록된 컨디션이 없으면 기본 key/value 비교를 한다.
				if("or".equals(operator)) {
					logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] (operator or)call 'isMatch' from [Simple Key/Value].");
					
					if("SimpleKeyMatch".equals(matchRefType)) {
						isConditionMatched = isConditionMatched||match.compare(paramKey);
					}else if("SimpleValueMatch".equals(matchRefType)) {
						isConditionMatched = isConditionMatched||match.compare(value);
					}
					
					if(isConditionMatched) break;
					
				}else if("and".equals(operator)) {
					logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] (operator and) call 'isMatch' from [Simple Key/Value].");
					
					if("SimpleKeyMatch".equals(matchRefType)) {
						isConditionMatched = match.compare(paramKey);
					}else if("SimpleValueMatch".equals(matchRefType)) {
						isConditionMatched = match.compare(value);
					}
					
					if(!isConditionMatched) break;
				}
				
			}else {
				logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] conditionMatchValueStructureId["+conditionMatchValue.getStructureId()+"]");
				logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] before isConditionMatched["+isConditionMatched+"]");
				
				if("or".equals(operator)) {
					logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] (operator or)call 'isMatch' from ["+conditionMatchValue.getStructureId()+"] class.");
					
					isConditionMatched = isConditionMatched||conditionMatchValue.isMatch(match);
					if(isConditionMatched) break;
					
				}else if("and".equals(operator)) {
					logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch[v:match]]] (operator and) call 'isMatch' from ["+conditionMatchValue.getStructureId()+"] class.");
					isConditionMatched = conditionMatchValue.isMatch(match);
					
					if(!isConditionMatched) break;
				}
			}
		}
		
		logger.debug("[C:FilteringHelper][v:condition[M:doConditionMatch]] isConditionMatched="+isConditionMatched);
		return isConditionMatched;
	}
	
	private String doInspect(Applier applier, String paramKey, String paramValue) {
		Inspect inspect = applier.getInspect();
		
		logger.debug("[C:FilteringHelper][v:condition[M:doInspect]] exclude["+inspect.getExclude()+"] inspectName["+inspect.getName()+"] paramKey["+paramKey+"] paramValue["+paramValue+"]");
		
		if(inspect == null || paramKey == null || inspect.getExclude()) {
			logger.debug("[C:FilteringHelper][v:condition[M:doInspect]] inspect is null or param key is null or exclude is true");
			return paramValue;
		}
		
		String applyFilter = "";
		String defaultFilter = this.config.getFilters().getDefaultFilter();
		if(inspect != null) {
			applyFilter = inspect.getFilter();
			if(applyFilter == null || applyFilter.trim().length() == 0) {
				applyFilter = defaultFilter;
			}
		}
		
		Pattern p;
		for(InspectRule rule: inspect.getRuleList()) {
			String matchKeyName = rule.getKeyName();
			String matchType = rule.getMatchType();
			logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] matchProcess -> matchKeyName["+matchKeyName+"] matchType["+matchType+"]");
			
			boolean keyMatched = false;
			if (matchType == null || "exactly".equals(matchType)) {
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] matchType is exactly");
				if(paramKey.equals(matchKeyName)) {
					keyMatched = true;
				}
			} else if ("startWith".equals(matchType)) {
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] matchType is startWith");
				if(paramKey.startsWith(matchKeyName)) {
					keyMatched = true;
				}
			} else if ("endWith".equals(matchType)) {
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] matchType is endWith");
				if(paramKey.endsWith(matchKeyName)) {
					keyMatched = true;
				}
			} else if ("contains".equals(matchType)) {
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] matchType is contains");
				if((paramKey.indexOf(matchKeyName)==-1?false:true)) {
					keyMatched = true;
				}
			} else if ("regex".equals(matchType)) {
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] matchType is regex");
				p = Pattern.compile(matchKeyName);
				if(p.matcher(paramKey).find()) {
					keyMatched = true;
				}
			} else {
				continue;
			}
			
			if(keyMatched) {
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] keyMatched is true");
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] match.getExclude() " + rule.getExclude());
				logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] paramValue " + paramValue);
				if(rule.getExclude()) {
					logger.debug("[C:FilteringHelper][v:condition[M:doInspect[v:match]]] Match exclude is TRUE.");
					return paramValue;
				}
				
				String filter =  rule.getFilter();
				if(filter != null && filter.trim().length() > 0) {
					applyFilter =  filter;
				}
				
				break;
			}
		}
		
		return passFilter(applyFilter, paramValue);
	}
	
	private String passFilter(String applyFilter, String paramValue) {
		logger.debug("[C:FilteringHelper][v:condition[M:doInspect[M:passFilter]]] applyFilter is "+applyFilter);
		logger.debug("[C:FilteringHelper][v:condition[M:doInspect[M:passFilter]]] applyFilter["+applyFilter+"] paramValue["+paramValue+"]");
		
		FilterInterface filterInstance 	= null;
		String filteredValue 			= paramValue;
		
		if(applyFilter == null || "".contentEquals(applyFilter.trim())) {
			logger.debug("[C:FilteringHelper][v:condition[M:doInspect[M:passFilter]]] applyFilter is null! return value");
			return paramValue;
		}
		
		String[] applyFilters 	= applyFilter.split(",");
		
		for(String applyFilterName: applyFilters) {
			applyFilterName = applyFilterName.trim();
			
			Filter filterElement = this.config.getFilters().getFilter(applyFilterName);
			if(filterElement != null) {
				filterInstance 		= filterElement.getFilterInstance();
				logger.error("[C:FilteringHelper][v:condition[M:doInspect[M:passFilter]]] start filtering. applyFilter("+applyFilterName+")");
				filteredValue 	= filterInstance.filter(filteredValue);
			}else {
				logger.error("[C:FilteringHelper][v:condition[M:doInspect[M:passFilter]]] applyFilter("+applyFilterName+") instance is null. please check config file.");
			}
		}
		
		logger.debug("[C:FilteringHelper][v:condition[M:doInspect[M:passFilter]]] filteredValue["+filteredValue+"]");
		return filteredValue;
	}
	
	private String pickRefType(String conditionsRefType, String conditionRefType, String matchRefType) {
		String currentRefType = null;
		if(matchRefType != null && matchRefType.length() > 0) {
			currentRefType = matchRefType;
		}else {
			if(conditionRefType != null && conditionRefType.length() > 0) {
				currentRefType = conditionRefType;
			}else {
				if(conditionsRefType != null && conditionsRefType.length() > 0) {
					currentRefType = conditionsRefType;
				}
			}
		}
		
		return currentRefType;
	}
}
