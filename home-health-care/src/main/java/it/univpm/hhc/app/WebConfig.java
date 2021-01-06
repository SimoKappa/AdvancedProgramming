package it.univpm.hhc.app;

import java.time.LocalDate;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
//import org.springframework.validation.Validator;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import it.univpm.hhc.test.DataServiceConfigTest;
import it.univpm.hhc.utils.LocalDateToDateConverter;
import it.univpm.hhc.utils.StringToLocalDateConverter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "it.univpm.hhc" },
	excludeFilters  = {@ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE, classes = {DataServiceConfigTest.class, })})
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public String appName() {
		return "Home Health Care App";
	}
		
	//Declare our static resources. I added cache to the java config but it's not required.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/")
				.setCachePeriod(31556926);
		registry.addResourceHandler("/media/**").addResourceLocations("/WEB-INF/media/")
		.setCachePeriod(31556926);
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/")
		.setCachePeriod(31556926);
	}

	@Bean 
	StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
	/*
	 * Esempio di funzionamento:
	 * 
	 * (footer) 		-> (/WEB-INF/views/footer.jsp)
	 * (singers/list) 	-> (/WEB-INF/views/singers/list.jsp)
	 * 
	@Bean
	//InternalResourceViewResolver viewResolver(){
	UrlBasedViewResolver viewResolver() {
	  //InternalResourceViewResolver resolver =  new InternalResourceViewResolver();
      UrlBasedViewResolver resolver = new UrlBasedViewResolver();
	  resolver.setPrefix("/WEB-INF/views/"); 
	  resolver.setSuffix(".jsp" );
	  resolver.setRequestContextAttribute("requestContext"); 
	  resolver.setViewClass(JstlView.class);
	  return resolver;
	}
	*/

	@Bean
	UrlBasedViewResolver tilesViewResolver() {
		UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
		tilesViewResolver.setViewClass(TilesView.class);
		return tilesViewResolver;
	}

	@Bean
	TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions(
				"/WEB-INF/layouts/layouts.xml",
				"/WEB-INF/views/views.xml",
				"/WEB-INF/views/**/views.xml"
		);
		tilesConfigurer.setCheckRefresh(true);
		return tilesConfigurer;
	}
	
	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {
		
		formatterRegistry.addFormatterForFieldType(LocalDate.class, this.dateFormatter());
		formatterRegistry.addConverter(new StringToLocalDateConverter());
		formatterRegistry.addConverter(new LocalDateToDateConverter());
	}

	@Bean
	public DateFormatter dateFormatter() {
		return new DateFormatter("dd/MM/YYYY");
	}

	@Bean
	ReloadableResourceBundleMessageSource messageSource() {
		// TODO implementare messaggi in message source
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("WEB-INF/i18n/messages", "WEB-INF/i18n/application");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(false);
		return messageSource;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(themeChangeInterceptor());
		registry.addInterceptor(webChangeInterceptor());
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		// TODO mostrare cambio di linguaggio
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Bean ResourceBundleThemeSource themeSource() {
		return new ResourceBundleThemeSource();
	}

	@Bean
	ThemeChangeInterceptor themeChangeInterceptor() {
		return new ThemeChangeInterceptor();
	}

	@Bean
	CookieLocaleResolver localeResolver() {
		// handle the choice of locale for showing messages
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(Locale.ITALIAN);
		cookieLocaleResolver.setCookieMaxAge(3600);
		cookieLocaleResolver.setCookieName("locale");
		return cookieLocaleResolver;
	}


	@Bean
	CookieThemeResolver themeResolver() {
		// allow to select the theme, in case the site has more than one
		CookieThemeResolver cookieThemeResolver = new CookieThemeResolver();
		cookieThemeResolver.setDefaultThemeName("standard");
		cookieThemeResolver.setCookieMaxAge(3600);
		cookieThemeResolver.setCookieName("theme");
		return cookieThemeResolver;
	}

	
	@Bean
	WebContentInterceptor webChangeInterceptor() {
		// allow/disallow handling of http methods; prepare the request
		WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
		webContentInterceptor.setCacheSeconds(0);
		webContentInterceptor.setSupportedMethods("GET", "POST", "PUT", "DELETE");
		return webContentInterceptor;
	}


	// <=> <mvc:view-controller .../>
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//mappa la URL /X nella vista "Y" (funziona senza un controller, ma cosi` la vista non riceve un contesto)
		//registry.addViewController("X").setViewName("Y");
		
		//configura delle redirezioni
        //registry.addViewController("/login").setViewName("login");

        //registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		//registry.addRedirectViewController("/", "/singers/list");
		registry.addRedirectViewController("/", "/");

	}
}