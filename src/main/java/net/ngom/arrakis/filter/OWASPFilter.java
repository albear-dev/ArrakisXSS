package net.ngom.arrakis.filter;

import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ngom.arrakis.config.elements.Config;
import net.ngom.arrakis.filter.DefaultFilter;

public class OWASPFilter extends DefaultFilter{
	static final Logger logger = LoggerFactory.getLogger("enXSS");
	
	public OWASPFilter(Config config) {
		super(config);
		logger.debug("init ESAPIFilter. config is null? " + (config==null?true:false));
	}
	
	@Override
	public String filter(String srcValue) {
		// refer to https://www.programcreek.com/java-api-examples/?api=org.owasp.esapi.Encoder
		return Encode.forHtml(srcValue);
	}
	
	public static void main(String[] args) {
	 String s1 = "keep-alive";
	    String s2 = "text/html";
	    System.out.println("forJavaScriptSource = " + Encode.forJavaScriptSource(s1));
	    System.out.println("forJavaScriptSource = " + Encode.forJavaScriptSource(s2));
	    //Assert.assertEquals(Encode.forJavaScriptBlock(s1), s1);
	    //Assert.assertEquals(Encode.forJavaScriptBlock(s2), s2);

	    String s3 = "<script>alert('test')</script>";
	    String e3 = Encode.forJavaScriptSource(s3);
	    System.out.println("forJavaScriptSource = " + e3);
	    String e4 = Encode.forJavaScriptAttribute(s3);
	    System.out.println("forJavaScriptAttribute = " + e4);
	    String e5 = Encode.forJavaScriptBlock(s3);
	    System.out.println("forJavaScriptBlock = " + e5);
	    String e6 = Encode.forJavaScript(e3);
	    System.out.println("forJavaScript = " + e6);
	    //Assert.assertNotEquals(e3, s3);

	    String s7 = "<script>location.href=\"respources.html\"</script>";
	    String e7 = Encode.forJavaScriptSource(s7);
	    System.out.println("e7 = " + e7);
	}
    
}
