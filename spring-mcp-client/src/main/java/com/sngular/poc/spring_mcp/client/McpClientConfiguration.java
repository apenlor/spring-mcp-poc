package com.sngular.poc.spring_mcp.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpClientConfiguration {

    @Bean
    McpSyncClient mcpSyncClient() {
        var mcp = McpClient.sync(HttpClientSseClientTransport.builder("http://localhost:8081").build()).build();
        mcp.initialize();
        return mcp;
    }

    @Bean
    McpClientRunner clientRunner(ChatClient.Builder builder, McpSyncClient mcpSyncClient) {
        SyncMcpToolCallbackProvider syncMcpToolCallbackProvider = new SyncMcpToolCallbackProvider(mcpSyncClient);
        return new McpClientRunner(builder.defaultTools(syncMcpToolCallbackProvider));
    }
}
