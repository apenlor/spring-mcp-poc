package com.sngular.poc.spring_mcp.server;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileTools {

    @Value("classpath:user-context")
    private Resource folderResource;

    @Tool(description = "Returns all the files in the folder resources/user-context" )
    String[] listFiles() throws IOException {
        File folder = folderResource.getFile();
        return folder.list();
    }
}
