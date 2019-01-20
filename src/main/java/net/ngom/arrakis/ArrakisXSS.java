package net.ngom.arrakis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.ArrakisXSSConfigHandler;
import net.ngom.arrakis.config.elements.Config;
import net.ngom.arrakis.datastructure.ConditionMatchValueCollection;
import net.ngom.arrakis.filter.FilteringHelper;

public class ArrakisXSS {

	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");

	private FilteringHelper filteringHelper;
	private Config config;

	public ArrakisXSS() throws Exception {
		this(null);
	}

	public ArrakisXSS(String configPath) throws Exception {
		super();

		try {
			initEnviroment(configPath);
		} catch (Exception e) {
			logger.error("ArrakisXSS Initialize fail!");
			throw e;
		}

		logger.info("ArrakisXSS is Initialize complete.");
	}
	
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	private void initEnviroment(String configPath) throws Exception {
		this.config = loadConfig(configPath);
		if(this.config == null) {
			logger.info("ArrakisXSS config load fail!");
		}else {
			logger.info("ArrakisXSS config is loaded!");
		}

		this.filteringHelper = new FilteringHelper(this.config);
		logger.info("FilteringHelper is created!");

	}

	private Config loadConfig(String configPath) throws Exception {
		// load config
		if (configPath == null) {
			return new ArrakisXSSConfigHandler().loadConfig();
		} else {
			return new ArrakisXSSConfigHandler().loadConfig(configPath);
		}
		
	}

	public String filtering(ConditionMatchValueCollection cmvc, String paramKey, String value) {
		return this.filteringHelper.filtering(cmvc, paramKey, value);
	}

}
