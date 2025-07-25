package dev.chungjungsoo.gptmobile.presentation.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.chungjungsoo.gptmobile.data.ModelConstants.ollamaModels
import dev.chungjungsoo.gptmobile.data.ModelConstants.tfLiteModels
import dev.chungjungsoo.gptmobile.data.dto.Platform
import dev.chungjungsoo.gptmobile.data.model.ApiType
import dev.chungjungsoo.gptmobile.data.repository.SettingRepository
import dev.chungjungsoo.gptmobile.presentation.common.Route
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SetupViewModel @Inject constructor(private val settingRepository: SettingRepository) : ViewModel() {

    private val _platformState = MutableStateFlow(
        listOf(
//            Platform(ApiType.OPENAI),
//            Platform(ApiType.ANTHROPIC),
//            Platform(ApiType.GOOGLE),
//            Platform(ApiType.GROQ),
            Platform(ApiType.OLLAMA),
            Platform(ApiType.TENSOR_FLOW_LITE)
        )
    )
    val platformState: StateFlow<List<Platform>> = _platformState.asStateFlow()

    fun updateAPIAddress(platform: Platform, address: String) {
        val index = _platformState.value.indexOf(platform)

        if (index >= 0) {
            _platformState.update {
                it.mapIndexed { i, p ->
                    if (index == i) {
                        p.copy(apiUrl = address.trim())
                    } else {
                        p
                    }
                }
            }
        }
    }

    fun updateCheckedState(platform: Platform) {
        val index = _platformState.value.indexOf(platform)

        if (index >= 0) {
            _platformState.update {
                it.mapIndexed { i, p ->
                    if (index == i) {
                        p.copy(selected = p.selected.not())
                    } else {
                        p
                    }
                }
            }
        }
    }

    fun updateToken(platform: Platform, token: String) {
        val index = _platformState.value.indexOf(platform)

        if (index >= 0) {
            _platformState.update {
                it.mapIndexed { i, p ->
                    if (index == i) {
                        p.copy(token = token.ifBlank { null })
                    } else {
                        p
                    }
                }
            }
        }
    }

    fun updateModel(apiType: ApiType, model: String) {
        val index = _platformState.value.indexOfFirst { it.name == apiType }

        if (index >= 0) {
            _platformState.update {
                it.mapIndexed { i, p ->
                    if (index == i) {
                        p.copy(model = model)
                    } else {
                        p
                    }
                }
            }
        }
    }

    fun savePlatformState() {
        _platformState.update { platforms ->
            // Update to platform enabled value
            platforms.map { p ->
                p.copy(enabled = p.selected, selected = false)
            }
        }
        viewModelScope.launch {
            settingRepository.updatePlatforms(_platformState.value)
        }
    }

    fun getNextSetupRoute(currentRoute: String?): String {
        val steps = listOf(
            Route.SELECT_PLATFORM,
            Route.TOKEN_INPUT,
//            Route.OPENAI_MODEL_SELECT,
//            Route.ANTHROPIC_MODEL_SELECT,
//            Route.GOOGLE_MODEL_SELECT,
//            Route.GROQ_MODEL_SELECT,
            Route.OLLAMA_MODEL_SELECT,
            Route.OLLAMA_API_ADDRESS,
            Route.TF_LITE_MODEL_SELECT, // 添加 TF_LITE 模型选择
            Route.TF_LITE_API_ADDRESS,  // 如果 TF_LITE 也有 API 地址，添加此项
            Route.SETUP_COMPLETE
        )
        val commonSteps = mutableSetOf(Route.SELECT_PLATFORM, Route.TOKEN_INPUT, Route.SETUP_COMPLETE)
        val platformStep = mapOf(
//            Route.OPENAI_MODEL_SELECT to ApiType.OPENAI,
//            Route.ANTHROPIC_MODEL_SELECT to ApiType.ANTHROPIC,
//            Route.GOOGLE_MODEL_SELECT to ApiType.GOOGLE,
//            Route.GROQ_MODEL_SELECT to ApiType.GROQ,
            Route.OLLAMA_MODEL_SELECT to ApiType.OLLAMA,
            Route.OLLAMA_API_ADDRESS to ApiType.OLLAMA, // Ollama API 地址应该映射到 Ollama
            Route.TF_LITE_MODEL_SELECT to ApiType.TENSOR_FLOW_LITE, // 修正：TF Lite 模型选择映射到 TF_LITE
            Route.TF_LITE_API_ADDRESS to ApiType.TENSOR_FLOW_LITE // 如果 TF_LITE 也有 API 地址，添加此项
        )

        val currentIndex = steps.indexOfFirst { it == currentRoute }
        val enabledPlatform = platformState.value.filter { it.selected }.map { it.name }.toSet()

        // 仅在Ollama是唯一选择时跳过API Token输入页面
        // 如果TensorFlow Lite也无需Token，你可能需要调整这个逻辑
        if (enabledPlatform.size == 1 && ApiType.OLLAMA in enabledPlatform) {
            commonSteps.remove(Route.TOKEN_INPUT)
        }

        val remainingSteps = steps.filterIndexed { index, setupStep ->
            index > currentIndex &&
                (setupStep in commonSteps || platformStep[setupStep] in enabledPlatform)
        }

        if (remainingSteps.isEmpty()) {
            // Setup Complete
            return Route.CHAT_LIST
        }

        return remainingSteps.first()
    }

    fun setDefaultModel(apiType: ApiType, defaultModelIndex: Int): String {
        val modelList = when (apiType) {
//            ApiType.OPENAI -> openaiModels
//            ApiType.ANTHROPIC -> anthropicModels
//            ApiType.GOOGLE -> googleModels
//            ApiType.GROQ -> groqModels
            ApiType.OLLAMA -> ollamaModels
            ApiType.TENSOR_FLOW_LITE -> tfLiteModels
        }.toList()

        if (modelList.size <= defaultModelIndex) {
            return ""
        }

        val model = modelList[defaultModelIndex]
        updateModel(apiType, model)

        return model
    }
}
