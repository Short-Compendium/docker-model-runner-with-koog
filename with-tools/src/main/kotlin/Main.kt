package with.tools

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import ai.koog.prompt.executor.clients.openai.OpenAIClientSettings
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel

class MyToolSet : ToolSet {
    @Tool(customName = "say_hello")
    fun sayHello(name: String): String {
        println("🟢 calling say_hello")
        return "👋 Hello $name 🙂"
    }
    @Tool(customName = "add")
    fun add(a: Float, b: Float): String {
        println("🟣 calling add")
        val r = a + b
        return "THE RESULT IS: $r"
    }
}


suspend fun main() {
    val myToolSet = MyToolSet()
    val toolRegistry = ToolRegistry { myToolSet }

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

    println(recipe)


}