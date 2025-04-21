## Core Concepts

1.  **`spring-mcp-server`**:
    *   Acts as an MCP Tool Provider.
    *   It uses `spring-ai-starter-mcp-server-webmvc` to expose Java functions as tools over HTTP via the MCP protocol.
    *   In this POC, it exposes a [`ToolCallbackProvider`](spring-mcp-server/src/main/java/com/sngular/poc/spring_mcp/server/McpServerConfiguration.java) containing tools to interact with files located in [`src/main/resources/user-context`](spring-mcp-server/src/main/resources/user-context). Specifically, it can list and read files in that directory.

2.  **`spring-mcp-client`**:
    *   Acts as an MCP Tool Consumer and integrates with Spring AI.
    *   It uses `spring-ai-starter-mcp-client` to configure an [`McpSyncClient`](spring-mcp-client/src/main/java/com/sngular/poc/spring_mcp/client/McpClientConfiguration.java) pointing to the `spring-mcp-server`.
    *   This `McpSyncClient` is registered as a `ToolCallback` source within the Spring AI `ChatClient`.
    *   The client application connects to a local Ollama instance (running a `mistral-small3.1` model) to process prompts.
    *   A specific prompt is sent to the AI model, instructing it to:
        *   Use the tool provided by the MCP server to find the file containing a Chinese greeting within the `user-context` directory (expected to be `ni_hao`).
        *   Retrieve the content of that file ("ni hao").
        *   Translate the content into Chinese characters.
    *   The final result (the Chinese characters, e.g., 你好) is mapped to the [`ChineseWord`](spring-mcp-client/src/main/java/com/sngular/poc/spring_mcp/dto/ChineseWord.java) DTO.

## Prerequisites

*   **JDK 21** or later.
*   **Apache Maven**.
*   **Ollama**: Install and run Ollama locally. Download from [https://ollama.com/](https://ollama.com/).
*   **Ollama Model**: Pull a suitable model. The client is configured by default to use `mistral-small3.1`, but this can be changed in the client's [`application.properties`](spring-mcp-client/src/main/resources/application.properties). Ensure the model you want to use is available locally:
    ```bash
    ollama run mistral-small3.1 # Or your preferred model
    ```

## Setup & Running

1.  **Clone the Repository:**
    ```bash
    git clone <your-repository-url>
    cd <repository-directory>
    ```

2.  **Ensure Ollama is Running:**
    Start the Ollama application/service. You can verify it's running by opening `http://localhost:11434` in your browser or using `ollama list` in your terminal.

3.  **Configure Client (Optional):**
    *   Verify the Ollama configuration in [`spring-mcp-client/src/main/resources/application.properties`](spring-mcp-client/src/main/resources/application.properties). Ensure the `spring.ai.openai.chat.options.model` points to a model you have pulled.
    *   Verify the Ollama server URL (`spring.ai.openai.chat.base-url`) matches where the server is running (default is `http://localhost:11434`).

4.  **Run the MCP Server:**
    *   Open a terminal in the project's root directory.
    *   Navigate to the server module: `cd spring-mcp-server`
    *   Start the server:
        ```bash
        ./mvnw spring-boot:run
        # On Windows: mvnw.cmd spring-boot:run
        ```
    *   The server will start, listening on port `8081` (check [`spring-mcp-server/src/main/resources/application.properties`](spring-mcp-server/src/main/resources/application.properties) for the `server.port` if needed).

5.  **Run the MCP Client:**
    *   Open a **new** terminal in the project's root directory.
    *   Navigate to the client module: `cd spring-mcp-client`
    *   Start the client:
        ```bash
        ./mvnw spring-boot:run
        # On Windows: mvnw.cmd spring-boot:run
        ```

6.  **Observe Output:**
    *   Watch the console output of the `spring-mcp-client` application.
    *   You will see Spring Boot initialization logs, followed by information about the MCP client connecting to the server and potentially listing available tools.
    *   Look for logs indicating the `ChatClient` is processing the request. The AI model should decide to use the remote MCP tool. You might see DEBUG logs related to `DefaultToolCallingManager` executing a tool call like `JavaSDKMCPClient_listFiles` or `JavaSDKMCPClient_readFileFromUserContext` (depending on the exact function names exposed and used).
    *   The most important line indicating successful execution and the correct result will be printed by the `McpClientRunner`:
        ```
        clientRunner: ChineseWord[file=ni_hao, word=你好]
        ```
    *   This confirms that the client communicated with the server via MCP, the server's tool accessed the `ni_hao` file, the AI processed the content ("ni hao"), translated it to Chinese characters ("你好"), and the result was correctly mapped to the `ChineseWord` DTO.
    *   You can also check the `spring-mcp-server` console logs to see the incoming request for the tool execution.

## Key Dependencies & Versions

*   **Java:** 21
*   **Spring Boot:** 3.4.4
*   **Spring AI:** 1.0.0-M7
    *   `spring-ai-starter-mcp-client`: For MCP client functionality.
    *   `spring-ai-starter-mcp-server-webmvc`: For exposing tools via MCP over WebMVC.
    *   `spring-ai-starter-model-openai`: Included in the client's POM (Note: Even though this starter is present, the application is configured via properties to use the Ollama integration).
*   **Ollama:** (External runtime dependency)

## License

This project is a personal Proof of Concept (POC) and is not intended for production use. No specific license is granted.