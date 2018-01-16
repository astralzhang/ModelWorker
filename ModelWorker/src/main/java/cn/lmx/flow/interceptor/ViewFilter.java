package cn.lmx.flow.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

public class ViewFilter extends SiteMeshFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest)request;
System.out.println(req.getParameter("NO_SITE_MESH"));		
		if (req.getParameter("NO_SITE_MESH") != null) {
			chain.doFilter(request, response);
		} else {
			super.doFilter(request, response, chain);
		}
	}
	
}
