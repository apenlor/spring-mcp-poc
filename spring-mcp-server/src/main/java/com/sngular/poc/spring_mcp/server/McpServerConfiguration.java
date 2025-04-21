package com.sngular.poc.spring_mcp.server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfiguration {

    @Bean
    ToolCallbackProvider fileSystemToolProvider(FileTools tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools).build();
    }
}
