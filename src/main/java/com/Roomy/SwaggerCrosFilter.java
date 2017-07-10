package com.Roomy;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerCrosFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse reponse = (HttpServletResponse) res;
		reponse.setHeader("Acess-Control-Allow-Origin", "*");
		reponse.setHeader("Acess-Control-Allow-Methods", "GET,POST,PUT,DELETE");
		reponse.setHeader("Acess-Control-Max-Age", "3600");
		reponse.setHeader("Acess-Control-Allow-Headers",
				"x-requested-with,Content-Type");
		reponse.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		reponse.setHeader("Pragma", "np-cache");
		reponse.setHeader("Expires", "0");
		chain.doFilter(req, res);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Do Nothing

	}

	@Override
	public void destroy() {
		// Do Nothing

	}
}