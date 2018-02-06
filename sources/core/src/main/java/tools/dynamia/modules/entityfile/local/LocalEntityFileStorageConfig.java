package tools.dynamia.modules.entityfile.local;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Configuration
class LocalEntityFileStorageConfig {

	@Bean
	public SimpleUrlHandlerMapping localHandler() {

		ResourceHttpRequestHandler handler = localEntityFileStorageHandler();

		Map<String, Object> map = new HashMap<>();

		map.put("storage/**", handler);

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(map);

		return mapping;
	}

	@Bean
	public LocalEntityFileStorageHandler localEntityFileStorageHandler() {
		return new LocalEntityFileStorageHandler();
	}
}
