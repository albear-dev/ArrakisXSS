# ArrakisXSS
Rule based message filter (Using servlet filter for XSS,  You want to filter for other purpose.)

## 1. 개요
- 이 모듈은 룰 기반의 필터링을 수행할 수 있는것이 장점이며, 서블릿 XSS방지 뿐만이 아니라 범용적으로 특정 값이 특정 조건에 만족하면 특정 문자열 필터링을 수행할수 있는 모듈로 동작하게 하도록 만든것이 모토입니다. 그래서 사용자는 URL및 키 비교 등 여러 조건을 설정파일에 추가함으로써 쉽게 동작하도록 설정할 수 있으며 개발자는 원하면 XSS 필터링 외에 특정 동작을 수행하는 필터를 확장하여 만들어 적용할 수 도 있습니다. 좀 특별한 예를 들자면 /process.do 에서 key1의 값을 암호화 해서 던지면 복호화하는 filter를 구현하여 서블릿에서 사용 가능하게도 이용이 가능합니다.

- 마지막으로 ArrakisXSS라는 이름은 제가 어릴적 좋아하던 프랭크 허버트의 Dune이라는 소설에서 나오는 사막행성의 이름에서 따 왔습니다. 이 행성에서는 거대한 모래벌레가 땅속을 돌아다니며 땅 위의 불순분자들을 잡아먹고 다니지요.

## 2. 사용법
- 웹 어플리케이션의 WEB-INF/web.xml 에 아래 정보를 추가한다.
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

- 웹 어플리케이션의 WEB-INF/lib 내에 라이브러리 파일을 복사한다. 기존에 동일 라이브러리가 있다면 복사하지 않아도 되며 버전이 다를 경우 최신버전으로 시도해 본다.

|JAR 경로                              |설명|
|:----                                 |:----|
|WEB-INF/lib/XSSfilter-x.x.x.jar       |XSS 필터 core 라이브러리. 뒤에 버전을 붙임|
|WEB-INF/lib/encoder-1.2.2.jar         |의존성 라이브러리 (owasp encoder 라이브러리)|
|WEB-INF/lib/encoder-jsp-1.2.2.jar     |의존성 라이브러리 (owasp encoder 라이브러리)|
|WEB-INF/lib/logback-classic-1.2.3.jar |의존성 라이브러리 (logging 관련 라이브러리)|
|WEB-INF/lib/logback-core-1.2.3.jar    |의존성 라이브러리 (logging 관련 라이브러리)|
|WEB-INF/lib/slf4j-api-1.7.25.jar      |의존성 라이브러리 (logging 관련 라이브러리)|

- 웹 어플리케이션의 WEB-INF/classes 내에 설정파일을 위치시킨다.

|XML 설정파일 경로                      |설명|
|:----                                 |:----|
|WEB-INF/classes/logback.xml           |logging 관련 설정 (logback 설정 참고)|
|WEB-INF/classes/ArrakisXSSConfig.xml  |XSS 필터 설정|

- WAS 서버를 기동한다.

## 3. 필더 모듈의 임시 해제
- 모듈 사용을 중단하려면 web.xml 의 추가하였던 filter 설정과 filer-mapping 설정을 주석처리한다.

## 4. 설정 예제
- 기본 설정 (전체적용) - ArrakisXSSConfig.xml
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

> 기본적으로 ArrakisXSS 의 설정은 크게 필터링 로직들을 기술하는 &lt;filters&gt; 와 적용 범위 및 조건을 정의하는 &lt;appliers&gt; 로 나누어 진다.

> filter 는 여러개 정의가 가능하며 필요할때 &lt;name&gt;에 고유 이름을 기술해 놓은 후 이후 &lt;appliers&gt;하위 조건부에서 기술해 사용이 가능하다. 그리고 실제 필터링 로직이 구현된 class의 경로는 &lt;filter&gt; 하위의 &lt;classPath&gt; 에 기술한다.

> &lt;appliers&gt; 내에는 여러개의 &lt;applier&gt; 가 선언될 수 있으며 순차적으로 조건 판단을 수행한다.
먼저 조건에 부합하는 건이 있으면 해당 &lt;applier&gt;내 업무를 처리하고 다음 applier 부분을 수행한다.

> &lt;appliers&gt; 내에서는 filter를 수행할 “조건” 을 입력하는 &lt;conditions&gt;와 필터 수행을 위해 적용 범위를 지정하는 &lt;inspect&gt; 로 나누어진다. 세부 내용은 바로 아래 설정 예제에서 확인 가능하다.

- 특정 URL만 적용 / 제외, 특정 키값만 적용 / 제외 - ArrakisXSSConfig.xml
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

> 위 &lt;condition&gt;내 &lt;match&gt;에 해당하는 조건일 경우 실제 실행 설정인 &lt;inspect&gt; 의 속성이 exclude=”true”이면 해당 요청은 별도 처리 없이 통과시킨다.

> 기본적으로는 여러개의 &lt;applier&gt;가 선언되어 있으면 순서대로 처리를 수행한다. 다만 applier의 옵션 중 break=”true” 로 설정된 부분이 있으면 선언된 &lt;applier&gt;를 수행하면 이후 &lt;applier&gt;는 수행하지 않는다.
	
> 그 외 의 경우 두번째 &lt;applier&gt;를 비교하게 되고 해당 condition으로 특정 URI가 /process 로 시작하면서 key3라는 이름을 가진 키(파라메터) 의 경우에는 아래 &lt;inspect&gt; 를 통해 제외시킨다..

> 세번째 applier에는 condition조건이 없으므로 요청 전체를 inspect 에 적용한 로직으로 수행한다. 예제에서는 OWASP-Filter와 SQLInjectionFiler가 선언되어 있으므로 두개를 순서대로 필터링 하게 된다.


## 5. 설정 옵션 설명
- 각 세부 Element 와 Attribute 설명 (추후 보완 예정)

## 6. 기본 제공 Filter 설명
|CLASS 경로                                 |설명|
|:----                                      |:----|
|net.ngom.arrakis.filter.DefaultFilter	    |설정파일의 Rule설정에 따라 단순 정규식에 따른 Replace를 수행하는 Filter. 내부적으로 java의 replaceAll 함수를 이용한다.|
|net.ngom.arrakis.filter.OWASPFilter	        |OWASP라는 비영리 오픈소스 웹 애플리케이션 보안 프로젝트에서 배포하는 보안취약점 개선 라이브러리를 사용한다. https://www.owasp.org/index.php/OWASP_Java_Encoder_Project 참고.|
|net.ngom.arrakis.filter.SQLInjectionFilter	|SQL Injection 공격시 자주 사용하는 특수문자 패턴을 제거하는 Filter이다.|

## 7. 유의사항
- 일부 인코딩 관련하여 web.xml에 설정을 추가한 경우 먼저 인코딩 옵션을 기술하고 다음에 해당 XSS 필터를 사용해야 한다. (순서가 중요함) 아래 예시 참고
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
