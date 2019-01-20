package net.ngom.arrakis.webhelper;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.ngom.arrakis.ArrakisXSS;

public class ArrakisXSSServletFilter implements Filter {
	private ArrakisXSS xss 	= null;

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			if(xss == null) {
				xss = new ArrakisXSS();
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new ArrakisXSSRequestWrapper(xss, request), response);
	}

	public void destroy() {
	}
}
