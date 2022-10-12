package com.yuthads.springdatajdbi3crud.configuration;

import static com.google.common.collect.Lists.newArrayList;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

@Configuration
public class SwaggerConfig {

	private ApiInfo apiInfo() {
		return new ApiInfo("SpringFox Demo APIs", "Demo APIs with spring boot rest api by usgin jdbi3", "1.0",
				"Terms of service", new Contact("YUTHADS", "https://yuthads.blogspot.com", "yuthads@mail.com"),
				"License of API", "API license URL", Collections.emptyList());
	}

	@Bean
	Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey())).select()
				.apis(RequestHandlerSelectors.basePackage("com.yuthads.springdatajdbi3crud.controller"))
				.paths(PathSelectors.ant("/api/product/**")).build().useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET,
						newArrayList(new ResponseBuilder().code("500").description("500 message").build(),
								new ResponseBuilder().code("403").description("Forbidden!!!!!").build()))
				.globalRequestParameters(Arrays.asList(
						new RequestParameterBuilder().name("x-global-header-1").description("Remote User")
								.in(ParameterType.HEADER).required(true)
								.query(simpleParameterSpecificationBuilder -> simpleParameterSpecificationBuilder
										.allowEmptyValue(false).model(
												modelSpecificationBuilder -> modelSpecificationBuilder
														.scalarModel(ScalarType.STRING)))
								.build(),
						new RequestParameterBuilder().name("x-global-header-2").description("Impersonate User")
								.in(ParameterType.HEADER).required(false)
								.query(simpleParameterSpecificationBuilder -> simpleParameterSpecificationBuilder
										.allowEmptyValue(false)
										.model(modelSpecificationBuilder -> modelSpecificationBuilder
												.scalarModel(ScalarType.STRING)))
								.build()));
	}

	@Bean
	UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().operationsSorter(OperationsSorter.METHOD).build();
	}
	private ApiKey apiKey() {
		return new ApiKey("apiKey", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		return Arrays.asList(new SecurityReference("apiKey", new AuthorizationScope[0]));
	}

	@Bean
	static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof WebMvcRequestHandlerProvider) {
					customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
				}
				return bean;
			}

			private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
					List<T> mappings) {
				List<T> copy = mappings.stream().filter(mapping -> mapping.getPatternParser() == null)
						.collect(Collectors.toList());
				mappings.clear();
				mappings.addAll(copy);
			}

			@SuppressWarnings("unchecked")
			private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
				try {
					Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
					field.setAccessible(true);
					return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		};
	}
}