package com.sngular.poc.spring_mcp.client;

import com.sngular.poc.spring_mcp.dto.ChineseWord;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.concurrent.atomic.AtomicReference;

public class McpClientRunner implements ApplicationRunner, BeanNameAware {
    private final AtomicReference<String> beanName = new AtomicReference<>();

    private final ChatClient.Builder chatClient;

    McpClientRunner(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName.set(name);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var prompt = """
                Read the filenames present in the user-context folder,
                and select the one which name is a chinese greeting.
                Return the original fileName and the word in its original language.
                """;

        var response = this.chatClient.build().prompt(prompt).call().entity(ChineseWord.class);

        System.out.println(this.beanName.get() + ": " + response);
    }
}
