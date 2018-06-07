package net.bldgos.commons.web.filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*@WebFilter(filterName="try-files",urlPatterns={"/*"},initParams= {
	@WebInitParam(name="tryFiles",value="$uri /index.html"),
	@WebInitParam(name="onlyForHtml",value="true")
})*/
public class TryFilesFilter implements Filter {

	private List<String> tryFiles=new ArrayList<>();
	private boolean onlyForHtml;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		for(StringTokenizer st=new StringTokenizer(filterConfig.getInitParameter("tryFiles"), " "); st.hasMoreTokens(); ){
			String f=st.nextToken();
			if(!f.equals("$uri") && !f.startsWith("/")){
				f="/"+f;
			}
			tryFiles.add(f);
		}
		onlyForHtml="true".equals(filterConfig.getInitParameter("onlyForHtml"));
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)res;

		if(onlyForHtml&&request.getHeader("Accept").indexOf("text/html")==-1) {
			chain.doFilter(req, res);
			return;
		}

		ServletContext application=request.getServletContext();
		for(String f:tryFiles){
			boolean isURI=f.equals("$uri");
			if(isURI){
				f=request.getRequestURI();
			}
			if(new File(application.getRealPath(f)).exists()){
				if(isURI){
					chain.doFilter(req, res);
				}else{
					request.getRequestDispatcher(f).forward(request, response);
				}
				return;
			}
		}
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	@Override
	public void destroy() {
		
	}

}
