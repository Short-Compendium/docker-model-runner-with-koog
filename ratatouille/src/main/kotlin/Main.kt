package kitchen.ratatouille

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.mcp.McpToolRegistryProvider
import ai.koog.prompt.executor.clients.openai.OpenAIClientSettings
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel

suspend fun main() {

    val transport = McpToolRegistryProvider.defaultSseTransport(System.getenv("MCP_HOST"))
    // Create a tool registry with tools from the MCP server
    val toolRegistry = McpToolRegistryProvider.fromTransport(
        transport = transport,
        name = "sse-client",
        version = "1.0.0"
    )
    println(toolRegistry.tools)

    val apiKey = "nothing"
    val customEndpoint = System.getenv("MODEL_RUNNER_BASE_URL").removeSuffix("/")
    val model = System.getenv("MODEL_RUNNER_CHAT_MODEL")

    val client = OpenAILLMClient(
        apiKey=apiKey,
        settings = OpenAIClientSettings(customEndpoint)
    )

    val promptExecutor = SingleLLMPromptExecutor(client)

    val llmModel = LLModel(
        provider = LLMProvider.OpenAI,
        id = model,
        capabilities = listOf(LLMCapability.Completion, LLMCapability.Tools)
    )

    val agent = AIAgent(
        executor = promptExecutor,
        systemPrompt = System.getenv("SYSTEM_PROMPT"),
        llmModel = llmModel,
        temperature = 0.0,
        toolRegistry = toolRegistry
    )

    val recipe = agent.run(System.getenv("AGENT_INPUT"))

    println("Recipe:\n $recipe")



}