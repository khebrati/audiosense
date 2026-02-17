package ir.khebrati.audiosense.domain.useCase.ai

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.deepseek.DeepSeekLLMClient
import ai.koog.prompt.executor.clients.deepseek.DeepSeekModels
import ai.koog.prompt.executor.ollama.client.OllamaClient
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.params.LLMParams
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun testKoog(){
    // Create an OpenAI client
    val client = DeepSeekLLMClient(apiKey = "sk-02fc7eec5b114c1ca6afdb60a83bc323")

    // Create a prompt
    val prompt = prompt("audiometry_result", LLMParams()) {
        // Add a system message to set the context
        system("You are a helpful assistant.")

        // Add a user message
        user("Tell me about Kotlin")

        // You can also add assistant messages for few-shot examples
        assistant("Kotlin is a modern programming language...")

        // Add another user message
        user("What are its key features?")
    }

    // Run the prompt
    CoroutineScope(Dispatchers.Default).launch {
        val response = client.execute(prompt, DeepSeekModels.DeepSeekChat)
        // Print the response
        Logger.withTag("Ollama").a (response.toString())
    }
}