package net.ngom.arrakis.config;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.elements.Config;
import net.ngom.arrakis.exception.ConfigLoadException;

public class ArrakisXSSConfigHandler {
	static final Logger logger = LoggerFactory.getLogger("ArrakisXSS");
	
	private Config config;
	private String configPath;
	
	private final String defaultConfigPath = "/ArrakisXSSConfig.xml";

	public Config loadConfig() throws Exception{
		return loadConfig(defaultConfigPath);
	}

	public Config loadConfig(String configPath) throws Exception {
		return unmarshal(configPath);
	}
	
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}

	public String getConfigPath() {
		return configPath;
	}
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	private Config unmarshal() throws Exception{
		return unmarshal(null);
	}
	
	private Config unmarshal(String configPath) throws Exception{
		Config config = null;
		if(configPath == null) {
			configPath = defaultConfigPath;
		}
		
		try {
			logger.debug("ArrakisXSS config xml file to object unmarshalling start...");
			logger.debug("ArrakisXSS config file path" + configPath);
			
			URL fileUrl = getClass().getResource(configPath);
			
			File file = new File(fileUrl.getFile());
			if(file == null || !file.exists()) {
				throw new ConfigLoadException("File not found : "+configPath);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			config = (Config) jaxbUnmarshaller.unmarshal(file);

			logger.debug("ArrakisXSS config xml file to object unmarshalling end...");
		} catch (JAXBException e) {
			logger.error("JAXB unmarshal Error!!!!");
			e.printStackTrace();
		}

		return config;
	}
	
	private Config marshal(Config config) throws Exception{
		try {
			logger.debug("ArrakisXSS config object to xml file marshalling start...");
			logger.debug("ArrakisXSS config file path" + defaultConfigPath);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			// Overloaded methods to marshal to different outputs
			DOMResult domResult = new DOMResult();
			jaxbMarshaller.marshal(config, domResult);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new DOMSource(domResult.getNode()), new StreamResult(System.out));

			logger.debug("ArrakisXSS config object to xml file marshalling end...");
		} catch (JAXBException e) {
			logger.error("JAXB unmarshal Error!!!!");
			e.printStackTrace();
		}

		return config;
	}

}
