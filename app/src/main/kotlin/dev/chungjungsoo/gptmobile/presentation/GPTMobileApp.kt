package dev.chungjungsoo.gptmobile.presentation

import android.app.Application
import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.chungjungsoo.gptmobile.presentation.GPTMobileApp.Companion.app
import javax.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object TFLite {
    private var _currentTFModel: String? = null
    private var _llmInference: LlmInference? = null
    private var initializationDeferred: CompletableDeferred<LlmInference>? = null
    private val mutex = Mutex() // 用于同步访问共享资源

    // 提供一个公共的访问器，但其设置是私有的
    val currentTFModel: String?
        get() = _currentTFModel

    val llmInference: LlmInference?
        get() = _llmInference

    // 私有状态，外部不需要关心内部是否正在加载
    // val isLoading = false // 不再需要单独的 isLoading 标志，Mutex 和 CompletableDeferred 会处理状态

    /**
     * 构建或获取 LlmInference 实例。
     * 这是一个挂起函数，因为它内部会进行耗时操作。
     * 确保在协程作用域内调用。
     */
    suspend fun buildLLMInference(modelName: String): LlmInference = mutex.withLock {
        // 1. 检查是否已经存在且模型匹配
        if (_llmInference != null && modelName == _currentTFModel) {
            return _llmInference!!
        }

        // 2. 检查是否有正在进行的初始化任务
        val currentDeferred = initializationDeferred
        if (currentDeferred != null && !currentDeferred.isCompleted) {
            // 如果有正在进行的任务，并且不是为当前模型初始化的，等待它完成并重新评估
            // 或者你可以选择抛出异常或直接返回 currentDeferred.await()
            // 这里我们选择等待并重新评估，以支持不同模型的并行请求处理（虽然实际是串行等待）
            currentDeferred.await() // 等待上一个初始化完成
            // 再次检查，因为上一个初始化可能已经为我们设置了正确模型
            if (_llmInference != null && modelName == _currentTFModel) {
                return _llmInference!!
            }
        }

        // 3. 如果没有，或者需要新的初始化，则开始新任务
        // 创建一个新的 CompletableDeferred 来表示这个初始化任务
        val newDeferred = CompletableDeferred<LlmInference>()
        initializationDeferred = newDeferred // 设置为当前正在进行的任务

        try {
            val taskOptions = LlmInference.LlmInferenceOptions
                .builder()
                .setModelPath("/data/local/tmp/llm/$modelName.task")
                .setMaxTopK(64)
                .build()

            // 这是一个耗时操作，假定 LlmInference.createFromOptions 是协程友好的
            // 如果它不是，你需要用 withContext(Dispatchers.IO) 包裹它
            val inference = LlmInference.createFromOptions(
                app, // 使用注入的上下文
                taskOptions
            )

            _llmInference = inference // 更新实例
            _currentTFModel = modelName // 更新当前模型
            newDeferred.complete(inference) // 标记任务完成，并提供结果
            return inference
        } catch (e: Exception) {
            newDeferred.completeExceptionally(e) // 如果发生错误，标记任务失败
            _llmInference = null // 清除可能的部分初始化
            _currentTFModel = null
            throw e // 重新抛出异常
        } finally {
            // 清理 deferred，确保下一次调用不会重用已完成的 deferred
            // 只有当这个 deferred 是我们当前设置的 deferred 时才清除
            if (initializationDeferred === newDeferred) {
                initializationDeferred = null
            }
        }
    }
}

@HiltAndroidApp
class GPTMobileApp : Application() {

    companion object {
        lateinit var app: Application
    }

    // TODO Delete when https://github.com/google/dagger/issues/3601 is resolved.
    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}
