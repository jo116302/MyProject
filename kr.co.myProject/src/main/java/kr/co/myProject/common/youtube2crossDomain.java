package kr.co.myProject.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class youtube2crossDomain implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", "*"); //허용대상 도메인
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT"); 
		res.setHeader("Access-Control-Max-Age", "3600"); 
		res.setHeader("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept"); 
		chain.doFilter(request, res); 
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}