package dev.chungjungsoo.gptmobile.presentation.common

object Route {

    const val GET_STARTED = "get_started"

    const val SETUP_ROUTE = "setup_route"
    const val SELECT_PLATFORM = "select_platform"
    const val TOKEN_INPUT = "token_input"

    const val OLLAMA_MODEL_SELECT = "ollama_model_select"
    const val OLLAMA_API_ADDRESS = "ollama_api_address"

    const val TF_LITE_MODEL_SELECT = "tf_lite_model_select"

    const val SETUP_COMPLETE = "setup_complete"

    const val CHAT_LIST = "chat_list"
    const val CHAT_ROOM = "chat_room/{chatRoomId}?enabled={enabledPlatforms}"

    const val SETTING_ROUTE = "setting_route"
    const val SETTINGS = "settings"
    const val OLLAMA_SETTINGS = "ollama_settings"
    const val TF_LITE_SETTINGS = "tf_lite_settings"
    const val ABOUT_PAGE = "about"
    const val LICENSE = "license"
}
