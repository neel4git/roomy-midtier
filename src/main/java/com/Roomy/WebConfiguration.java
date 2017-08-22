package com.Roomy;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfiguration extends WebMvcConfigurerAdapter {
	/*
	 * @Bean ServletRegistrationBean h2servletRegistration() {
	 * ServletRegistrationBean registrationBean = new ServletRegistrationBean(new
	 * WebServlet()); registrationBean.addUrlMappings("/console/*"); return
	 * registrationBean; }
	 */

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("https://dashboard.pobyt.co").allowedMethods("GET","PUT", "DELETE")
				.allowedHeaders("header1", "header2", "header3").exposedHeaders("header1", "header2")
				.allowCredentials(false).maxAge(3600);
	}
}
