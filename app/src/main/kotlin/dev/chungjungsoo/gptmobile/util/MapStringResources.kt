package dev.chungjungsoo.gptmobile.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.chungjungsoo.gptmobile.R
import dev.chungjungsoo.gptmobile.data.model.ApiType
import dev.chungjungsoo.gptmobile.data.model.DynamicTheme
import dev.chungjungsoo.gptmobile.data.model.ThemeMode

@Composable
fun getPlatformTitleResources(): Map<ApiType, String> = mapOf(
    ApiType.OLLAMA to stringResource(R.string.ollama),
    ApiType.TENSOR_FLOW_LITE to stringResource(R.string.tflite)
)

@Composable
fun getPlatformDescriptionResources(): Map<ApiType, String> = mapOf(
    ApiType.OLLAMA to stringResource(R.string.ollama_description),
    ApiType.TENSOR_FLOW_LITE to stringResource(R.string.tflite_description)
)

@Composable
fun getPlatformAPILabelResources(): Map<ApiType, String> = mapOf(
    ApiType.OLLAMA to stringResource(R.string.ollama_api_key),
    ApiType.TENSOR_FLOW_LITE to stringResource(R.string.tflite_api_key)
)

@Composable
fun getPlatformHelpLinkResources(): Map<ApiType, String> = mapOf(
    ApiType.OLLAMA to stringResource(R.string.ollama_api_help),
    ApiType.TENSOR_FLOW_LITE to stringResource(R.string.tflite_api_help)
)

@Composable
fun getAPIModelSelectTitle(apiType: ApiType) = when (apiType) {
    ApiType.OLLAMA -> stringResource(R.string.select_ollama_model)
    ApiType.TENSOR_FLOW_LITE -> stringResource(R.string.select_tflite_model)
}

@Composable
fun getAPIModelSelectDescription(apiType: ApiType) = when (apiType) {
    ApiType.OLLAMA -> stringResource(id = R.string.select_ollama_model_description)
    ApiType.TENSOR_FLOW_LITE -> stringResource(R.string.select_tflite_model_description)
}

@Composable
fun getDynamicThemeTitle(theme: DynamicTheme) = when (theme) {
    DynamicTheme.ON -> stringResource(R.string.on)
    DynamicTheme.OFF -> stringResource(R.string.off)
}

@Composable
fun getThemeModeTitle(theme: ThemeMode) = when (theme) {
    ThemeMode.SYSTEM -> stringResource(R.string.system_default)
    ThemeMode.DARK -> stringResource(R.string.on)
    ThemeMode.LIGHT -> stringResource(R.string.off)
}

@Composable
fun getPlatformSettingTitle(apiType: ApiType) = when (apiType) {
    ApiType.OLLAMA -> stringResource(R.string.ollama_setting)
    ApiType.TENSOR_FLOW_LITE -> stringResource(R.string.tflite_setting)
}

@Composable
fun getPlatformSettingDescription(apiType: ApiType) = when (apiType) {
    ApiType.OLLAMA -> stringResource(R.string.platform_setting_description)
    ApiType.TENSOR_FLOW_LITE -> stringResource(R.string.platform_setting_description)
}

@Composable
fun getPlatformAPIBrandText(apiType: ApiType) = when (apiType) {
    ApiType.OLLAMA -> stringResource(R.string.ollama_brand_text)
    ApiType.TENSOR_FLOW_LITE -> stringResource(R.string.tflite_brand_text)
}
