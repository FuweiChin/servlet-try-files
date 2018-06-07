# servlet-try-files
This project provides a Servlet filter 'TryFilesFilter' which is equivelant to Nginx directive 'try_files', in order to make web server support HTML5 history API fallback.

'TryFilesFilter' can be configured to try files only for HTML request. such we won't suffer errors like
> index.js:1 Uncaught SyntaxError: Unexpected token &lt;

> Resource interpreted as Stylesheet but transferred with MIME type text/html

> Broken image

## Configure
Choose your preferred approach from the following to register/enable `TryFilesFilter`:  

### Register TryFilesFilter in web.xml with `<filter>` and `<filter-mapping>`.  
```
<filter>
	<filter-name>try-files</filter-name>
	<filter-class>net.bldgos.commons.web.filter.TryFilesFilter</filter-class>
	<init-param>
		<param-name>tryFiles</param-name>
		<param-value>$uri /index.html</param-value>
	</init-param>
	<init-param>
		<param-name>onlyForHtml</param-name>
		<param-value>true</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>try-files</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```
### Register TryFilesFilter by annotate itself with @WebFilter.  
```
@WebFilter(filterName="try-files",urlPatterns={"/*"},initParams= {
	@WebInitParam(name="tryFiles",value="$uri /index.html"),
	@WebInitParam(name="onlyForHtml",value="true")
})
```

## Compile
1. Run `mvn package` to build the project.
2. Change to directory "target", you'll see "servlet-try-files.jar"

## Usage
Assuming you have a react app `myapp` which is created by command `create-react-app myapp`.  
1. Run `npm build`, when built, there may leave a directory "build".
2. Change to directory "build", create a folder "WEB-INF" and put "servlet-tryfiles-filter.jar" into it.
3. Register servlet filter 'TryFilesFilter' if not registered yet.
