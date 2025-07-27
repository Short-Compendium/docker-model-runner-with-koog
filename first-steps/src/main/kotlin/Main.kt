package first.steps

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.mcp.McpToolRegistryProvider
import ai.koog.prompt.executor.clients.openai.OpenAIClientSettings
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel

suspend fun main() {


    val apiKey = "nothing"
    val customEndpoint = System.getenv("MODEL_RUNNER_BASE_URL").removeSuffix("/")
    val model = System.getenv("MODEL_RUNNER_CHAT_MODEL")?:
    "hf.co/menlo/lucy-128k-gguf:q4_k_m"

    val client = OpenAILLMClient(
        apiKey=apiKey,
        settings = OpenAIClientSettings(customEndpoint)
    )

    val promptExecutor = SingleLLMPromptExecutor(client)

    val llmModel = LLModel(
        provider = LLMProvider.OpenAI,
        id = model,
        capabilities = listOf(LLMCapability.Completion)
    )

    val agent = AIAgent(
        executor = promptExecutor,
        systemPrompt = System.getenv("SYSTEM_PROMPT"),
        llmModel = llmModel,
        temperature = 0.0,

    )

    val recipe = agent.run(System.getenv("AGENT_INPUT"))

    println("Recipe:\n $recipe")


}