# ArrakisXSS
Rule based message filter (Using servlet filter for XSS,  You want to filter for other purpose.)

*Read this in other languages: [English](README.md), [한국어](README.ko.md).*

## 1. Summary
- This module has an advantage of being able to perform rule-based filtering. It is a motto that not only prevents Servlet XSS, but also makes it possible to perform a specific string filtering when a specific value satisfies a specific condition. So, users can easily set various conditions such as URL and key comparison to be added to the configuration file, and developers can also create and apply filters that perform specific actions in addition to XSS filtering if desired. As a special example, if you encrypt the value of key1 in /process.do and implement a filter to decrypt it, you can use it in a servlet.

- Finally, the name ArrakisXSS comes from the name of a desert planet from a novel by Frank Herbert, Dune, which I liked as a child. On this planet, giant sand worms roam the earth and eat the moving things on the desert.

## 2. Usable environment
- java 1.7 or higher (some libraries require 1.7 or higher)

## 3. How to use
- Add the following information to the WEB-INF / web.xml of your web application.
```xml
<filter>
<filter-name>ArrakisXSSServletFilter</filter-name>
    <filter-class>net.ngom.arrakis.webhelper.ArrakisXSSServletFilter</filter-class>
</filter>
    
<filter-mapping>
    <filter-name>ArrakisXSSServletFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

- Copy the library file into WEB-INF / lib of the web application. If you already have the same library, you do not need to copy it. If the version is different, try the latest version.

|JAR path                              |Description|
|:----                                 |:----|
|WEB-INF/lib/XSSfilter-x.x.x.jar       |XSS filter core library. Subsequent versions|
|WEB-INF/lib/encoder-1.2.2.jar         |Dependency library (owasp encoder library)|
|WEB-INF/lib/encoder-jsp-1.2.2.jar     |Dependency library (owasp encoder library)|
|WEB-INF/lib/logback-classic-1.2.3.jar |Dependency library (logging library)|
|WEB-INF/lib/logback-core-1.2.3.jar    |Dependency library (logging library)|
|WEB-INF/lib/slf4j-api-1.7.25.jar      |Dependency library (logging library)|

- Locate the configuration file in WEB-INF / classes of the web application.

|XML Configuration file path           |Description|
|:----                                 |:----|
|WEB-INF/classes/logback.xml           |logging related settings (see logback setting)|
|WEB-INF/classes/ArrakisXSSConfig.xml  |XSS Filter settings|

- WAS Start the server.

## 4. Temporarily release the filter module
- To stop using the module, comment out the filter and filer-mapping settings you added in web.xml.

## 5. Setting example
- Basic setting (all applied) - ArrakisXSSConfig.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
	<filters default="OWASP-Filter">
		<filter>
			<name>OWASP-Filter</name>
			<classPath>net.ngom.arrakis.filter.OWASPFilter</classPath>
		</filter>
		<filter>
			<name>SQLInjectionFilter</name>
			<classPath>net.ngom.arrakis.filter.SQLInjectionFilter</classPath>
		</filter>
	</filters>
		
	<appliers>
		<applier>
			<inspect filter="OWASP-Filter, SQLInjectionFilter"  />
		</applier>
	</appliers>
</config>
```

> By default, the configuration of ArrakisXSS is largely defined by the &lt;filters&gt; And &lt;appliers&gt; that define coverage and conditions. Respectively.

> The filter can be defined multiple times and can be used in the &lt;appliers&gt; subcondition after describing the distinguished name &lt;name&gt; The path of the class in which the actual filtering logic is implemented is &lt;filter&gt; &lt;classPath&gt; .

> &lt;appliers&gt; There are multiple &lt;applier&gt; Can be declared, and condition judgment is performed sequentially.
  First, if there is anything that meets the condition, process the &lt;applier&gt; my work and do the next applier part.

> &lt;appliers&gt; &lt;conditions&gt; for entering a &quot;condition&quot; to perform a filter and &lt;inspect&gt; for specifying a scope for performing a filter. Respectively. Details can be found in the example below.

- Apply / exclude specific URLs, apply / exclude specific key values only - ArrakisXSSConfig.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
	<filters default="OWASP-Filter">
		<filter>
			<name>OWASP-Filter</name>
			<classPath>net.ngom.arrakis.filter.OWASPFilter</classPath>
		</filter>
		<filter>
			<name>SQLInjectionFilter</name>
			<classPath>net.ngom.arrakis.filter.SQLInjectionFilter</classPath>
		</filter>
		<filter>
			<name>CustomFilter</name>
			<classPath>net.ngom.arrakis.filter.DefaultFilter</classPath>
			<configPath>/DefaultXSSFilterConfig.xml</configPath>
		</filter>
		<filter>
			<name>CustomFilter2</name>
			<classPath>net.ngom.arrakis.filter.DefaultFilter</classPath>
			<rules>
				<rule pattern="wrongValue" replace-to="correctValue" />
				<rule pattern="[0-9]{3}-([0-9]{3})-[0-9]{3}" replace-to="$1" />
				<rule>
					<pattern><![CDATA[\'([a-zA-Z]+)\']]></pattern>
					<replace-to><![CDATA[<$1>]]></replace-to>
				</rule>
			</rules>
		</filter>
	</filters>

<appliers>
	<applier break="true">
		<conditions ref-type="RequestMetaMap">
			<condition>
				<match key-name="URI" match-type="startWith">/process2</match>
			</condition>
		</conditions>
		<inspect exclude="true">
	</applier>
		
	<applier break="true">
		<conditions>
			<condition>
				<match ref-type="RequestMetaMap" key-name="URI"  match-type="startWith" >/process</match>
				<match ref-type="SimpleKeyMatch" match-type="exactly" >key3</match>
			</condition>
		</conditions>
		<inspect exclude="true" />
	</applier>
		
	<applier>
		<inspect filter="OWASP-Filter, SQLInjectionFilter" />
	</applier>
</appliers>

</config>
```

> In the case of the above condition &lt;condition&gt; within &lt;match&gt;, the actual execution setting &lt;inspect&gt; If the attribute of &quot;exclude&quot; is &quot;true&quot;, the request is passed without any processing.

> Basically, if several &lt;applier&gt; are declared, processing is performed in order. However, if there is a portion set to break ="true" among the options of applier, &lt;applier&gt; is not performed after the &lt;applier&gt;
	
> Otherwise, it compares the second &lt;applier&gt;. In the case of a key (parameter) whose name starts with a particular URI / process and whose name is key3, .

> The third applier does not have a condition condition, so it executes the entire request with logic applied to inspect. In the example, OWASP-Filter and SQLInjectionFiler are declared, so they are filtered in order.


## 6. Setting Option Description
- Note the docx file in the Documents folder of the repository

|언어(language)   |파일명(filename)|
|:----            |:----|
|영문(english)    |ArrakisXSS Option Guide.en.docx|
|한글(korean)     |ArrakisXSS Option Guide.ko.docx|

## 7. Built-in Filter Description
|CLASS Path                                 |Decription|
|:----                                      |:----|
|net.ngom.arrakis.filter.DefaultFilter	    |Filter that performs replacement according to simple regular expression according to rule setting of setting file. Internally, we use the replaceAll function of java.|
|net.ngom.arrakis.filter.OWASPFilter	        |It uses a vulnerability remover library distributed by OWASP, a nonprofit open source Web application security project. https://www.owasp.org/index.php/OWASP_Java_Encoder_Project|
|net.ngom.arrakis.filter.SQLInjectionFilter	|It is a filter that removes the special character patterns frequently used in SQL Injection attack.|

## 8. Precautions
- If you add settings to web.xml for some encodings, you must first describe the encoding options and then use the appropriate XSS filter. (Order is important) See example below
```xml
<?xml version="1.0" encoding="utf-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	version="2.5">
	
	<servlet>
		<servlet-name>MyHttpServlet</servlet-name>
		<servlet-class>net.ngom.web.servlet.MyHttpServlet</servlet-class>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>MyHttpServlet</servlet-name>
		<url-pattern>/process</url-pattern>
	</servlet-mapping>
	
	 <filter>
        <filter-name>EncodingFilter</filter-name>
        <filter-class>net.ngom.web.filter.CommonEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    
	<filter>
        <filter-name>XSSServletFilter</filter-name>
        <filter-class>net.ngom.arrakis.webhelper.ArrakisXSSServletFilter</filter-class>
    </filter>
    
    <filter-mapping>
        <filter-name>EncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
    <filter-mapping>
        <filter-name>XSSServletFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```
