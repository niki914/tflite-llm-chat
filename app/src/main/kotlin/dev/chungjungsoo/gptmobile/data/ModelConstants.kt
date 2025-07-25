package dev.chungjungsoo.gptmobile.data

import dev.chungjungsoo.gptmobile.data.model.ApiType

object ModelConstants {
    // LinkedHashSet should be used to guarantee item order
    val ollamaModels = linkedSetOf<String>()

    val tfLiteModels = linkedSetOf<String>()
    const val ANTHROPIC_API_URL = "https://api.anthropic.com/"

    fun getDefaultAPIUrl(apiType: ApiType) = when (apiType) {
        ApiType.OLLAMA -> ""
        ApiType.TENSOR_FLOW_LITE -> ""
    }

    const val DEFAULT_PROMPT = "Your task is to answer my questions precisely."
}
