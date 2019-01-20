package net.ngom.arrakis.config.elements;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.ArrakisXSSConfigHandler;
import net.ngom.arrakis.exception.ConfigLoadException;
import net.ngom.arrakis.filter.DefaultFilter;

@XmlType(propOrder = { "name", "classPath", "configPath", "config" })
public class Filter implements Serializable  {
	static final Logger logger = LoggerFactory.getLogger("Arrakis");
	
	private static final long serialVersionUID = 7210665813398138177L;
	
	private String name;
	private String classPath;
	private String configPath;
	private Config config;
	
	@XmlTransient
	private DefaultFilter filterInstance;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String getConfigPath() {
		return configPath;
	}
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}
	
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	void beforeUnmarshal(Unmarshaller unmarshaller, Object parent)  throws Exception {
        logger.debug("Before Unmarshaller Callback");
    }
	
	@XmlTransient
    public DefaultFilter getFilterInstance() {
		return filterInstance;
	}
	public void setFilterInstance(DefaultFilter filterInstance) {
		this.filterInstance = filterInstance;
	}
	// xml -> object
    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) throws Exception {
        logger.debug("After Unmarshaller Callback");
                
        if(this.config == null) {
        	if(this.configPath != null && configPath.length() > 0) {
        		// configPath
        		this.config = new ArrakisXSSConfigHandler().loadConfig(this.configPath);
        	}
        	// config not found
        }
        
        this.filterInstance = createFilterInstance();
    }
    
    private DefaultFilter createFilterInstance() throws Exception{
    	Class clazz = Class.forName(this.classPath);
    	Constructor constructor = clazz.getConstructor(Config.class);
    	
        return (DefaultFilter)constructor.newInstance(config);
        
    }
	
}
