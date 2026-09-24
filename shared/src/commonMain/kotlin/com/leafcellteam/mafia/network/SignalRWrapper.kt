//package com.leafcellteam.mafia.network
//
//import kotlin.reflect.KType
//import kotlin.reflect.typeOf
//import eu.lepicekmichal.signalrkore.HttpHubConnectionBuilder
//import eu.lepicekmichal.signalrkore.HubConnection
//import eu.lepicekmichal.signalrkore.HubConnectionBuilder
//import eu.lepicekmichal.signalrkore.HubConnectionState
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.CoroutineStart
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//import kotlinx.serialization.InternalSerializationApi
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.serializer
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.JsonArray
//import kotlinx.serialization.json.JsonElement
//import kotlinx.serialization.json.JsonNull
//import kotlinx.serialization.json.JsonObject
//import kotlin.reflect.KClass
//
//
//interface SignalRHubConnection {
//    fun start()
//    fun stop()
//    fun send(method: String, vararg args: Any?)
//    fun <T> on(method: String, callback: (T) -> Unit, targetClass: KClass<*>)
//    fun <T1, T2> on(method: String, callback: (T1, T2) -> Unit, targetClass1: KClass<*>, targetClass2: KClass<*>)
//    fun <T1, T2, T3> on(method: String, callback: (T1, T2, T3) -> Unit, targetClass1: KClass<*>, targetClass2: KClass<*>, targetClass3: KClass<*>)
//    fun <T1, T2, T3, T4, T5, T6> on(
//        method: String,
//        callback: (T1, T2, T3, T4, T5, T6) -> Unit,
//        targetClass1: KClass<*>,
//        targetClass2: KClass<*>,
//        targetClass3: KClass<*>,
//        targetClass4: KClass<*>,
//        targetClass5: KClass<*>,
//        targetClass6: KClass<*>
//    )
//
//    fun <T1, T2, T3, T4, T5, T6, T7, T8> on(
//        method: String,
//        callback: (T1, T2, T3, T4, T5, T6, T7, T8) -> Unit,
//        targetClass1: KClass<*>,
//        targetClass2: KClass<*>,
//        targetClass3: KClass<*>,
//        targetClass4: KClass<*>,
//        targetClass5: KClass<*>,
//        targetClass6: KClass<*>,
//        targetClass7: KClass<*>,
//        targetClass8: KClass<*>
//    )
//    fun remove(method: String)
//    fun onClosed(callback: (Exception?) -> Unit)
//    val state: HubState
//}
//
//inline fun <reified T : Any> SignalRHubConnection.on(
//    method: String,
//    noinline callback: (T) -> Unit
//) {
//    // Благодаря reified, T::class успешно и легально извлечет класс Array<PlayerDto>
//    on(method, callback, T::class)
//}
//
//inline fun <reified T1 : Any, reified T2 : Any, reified T3 : Any> SignalRHubConnection.on(
//    method: String,
//    noinline callback: (T1, T2, T3) -> Unit
//) {
//    on(method, callback, T1::class, T2::class, T3::class)
//}
//
//enum class HubState {
//    CONNECTED, DISCONNECTED, CONNECTING, RECONNECTING
//}
//
//class SignalRHubBuilder(
//    private val url: String,
//    private val onError: (Throwable) -> Unit = {},
//    private val configure: (HttpHubConnectionBuilder.() -> Unit)? = null, // токен, реконнект и т.д.
//) {
//    fun build(): SignalRHubConnection {
//        val hubConnection = if (configure != null) {
//            HubConnectionBuilder.create(url, configure)
//        } else {
//            HubConnectionBuilder.create(url)
//        }
//        return KoreSignalRHubConnection(hubConnection, onError)
//    }
//}
//
////@file:OptIn(InternalSerializationApi::class)
////@file:Suppress("UNCHECKED_CAST")
////
//
//
///**
// * Реализация [SignalRHubConnection] на SignalRKore. Лежит в commonMain,
// * отдельные Android/iOS реализации и expect/actual больше не нужны.
// *
// * Не потокобезопасно только управление подписками (on/remove): вызывайте их
// * с одного потока (обычно main).
// */
//class KoreSignalRHubConnection(
//    private val hubConnection: HubConnection,
//    private val onError: (Throwable) -> Unit = {},
//    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
//) : SignalRHubConnection {
//
//    private val subscriptions = mutableMapOf<String, MutableList<Job>>()
//
//    // Очередь исходящих: сохраняет порядок send(), в отличие от launch на каждый вызов.
//    private val sendQueue = Channel<suspend () -> Unit>(Channel.UNLIMITED)
//
//    init {
//        scope.launch {
//            for (task in sendQueue) {
//                runReporting(task)
//            }
//        }
//    }
//
//    // ---------------------------------------------------------------- lifecycle
//
//    override fun start() {
//        scope.launch { runReporting { hubConnection.start() } }
//    }
//
//    override fun stop() {
//        scope.launch { runReporting { hubConnection.stop() } }
//    }
//
//    override val state: HubState
//        get() = when (hubConnection.connectionState.value) {
//            HubConnectionState.CONNECTED -> HubState.CONNECTED
//            HubConnectionState.CONNECTING,
//            HubConnectionState.RECONNECTING -> HubState.CONNECTING
//            HubConnectionState.DISCONNECTED -> HubState.DISCONNECTED
//        }
//
//    /**
//     * В SignalRKore нет колбэка закрытия, поэтому он собран из connectionState:
//     * вызывается, когда соединение, бывшее CONNECTED, оказалось DISCONNECTED
//     * (в том числе после stop() и после неудачных попыток реконнекта).
//     * Исключение недоступно — всегда null.
//     */
//    override fun onClosed(callback: (Exception?) -> Unit) {
//        scope.launch(start = CoroutineStart.UNDISPATCHED) {
//            var wasConnected = false
//            hubConnection.connectionState.collect { s ->
//                when (s) {
//                    HubConnectionState.CONNECTED -> wasConnected = true
//                    HubConnectionState.DISCONNECTED -> if (wasConnected) {
//                        wasConnected = false
//                        try { callback(null) } catch (e: Exception) { onError(e) }
//                    }
//                    else -> Unit
//                }
//            }
//        }
//    }
//
//    // --------------------------------------------------------------------- send
//
//    override fun send(method: String, vararg args: Any?) {
//        // SignalRKore принимает уже сериализованные аргументы (List<JsonElement>)
//        val elements = try {
//            args.map { it.toJsonElement() }
//        } catch (e: Exception) {
//            onError(e)
//            return
//        }
//        sendQueue.trySend { hubConnection.send(method, elements) }
//    }
//
//    // ----------------------------------------------------------------------- on
//
//    override fun <T> on(method: String, callback: (T) -> Unit, targetClass: KClass<*>) {
//        subscribe(method, hubConnection.on(method, targetClass.ser())) { (a) ->
//            callback(a as T)
//        }
//    }
//
//    override fun <T1, T2> on(
//        method: String,
//        callback: (T1, T2) -> Unit,
//        targetClass1: KClass<*>,
//        targetClass2: KClass<*>,
//    ) {
//        subscribe(
//            method,
//            hubConnection.on(method, targetClass1.ser(), targetClass2.ser()),
//        ) { (a1, a2) ->
//            callback(a1 as T1, a2 as T2)
//        }
//    }
//
//    override fun <T1, T2, T3> on(
//        method: String,
//        callback: (T1, T2, T3) -> Unit,
//        targetClass1: KClass<*>,
//        targetClass2: KClass<*>,
//        targetClass3: KClass<*>,
//    ) {
//        subscribe(
//            method,
//            hubConnection.on(method, targetClass1.ser(), targetClass2.ser(), targetClass3.ser()),
//        ) { (a1, a2, a3) ->
//            callback(a1 as T1, a2 as T2, a3 as T3)
//        }
//    }
//
//    override fun <T1, T2, T3, T4, T5, T6> on(
//        method: String,
//        callback: (T1, T2, T3, T4, T5, T6) -> Unit,
//        targetClass1: KClass<*>,
//        targetClass2: KClass<*>,
//        targetClass3: KClass<*>,
//        targetClass4: KClass<*>,
//        targetClass5: KClass<*>,
//        targetClass6: KClass<*>,
//    ) {
//        subscribe(
//            method,
//            hubConnection.on(
//                method,
//                targetClass1.ser(), targetClass2.ser(), targetClass3.ser(),
//                targetClass4.ser(), targetClass5.ser(), targetClass6.ser(),
//            ),
//        ) { (a1, a2, a3, a4, a5, a6) ->
//            callback(a1 as T1, a2 as T2, a3 as T3, a4 as T4, a5 as T5, a6 as T6)
//        }
//    }
//
//    override fun <T1, T2, T3, T4, T5, T6, T7, T8> on(
//        method: String,
//        callback: (T1, T2, T3, T4, T5, T6, T7, T8) -> Unit,
//        targetClass1: KClass<*>,
//        targetClass2: KClass<*>,
//        targetClass3: KClass<*>,
//        targetClass4: KClass<*>,
//        targetClass5: KClass<*>,
//        targetClass6: KClass<*>,
//        targetClass7: KClass<*>,
//        targetClass8: KClass<*>,
//    ) {
//        subscribe(
//            method,
//            hubConnection.on(
//                method,
//                targetClass1.ser(), targetClass2.ser(), targetClass3.ser(), targetClass4.ser(),
//                targetClass5.ser(), targetClass6.ser(), targetClass7.ser(), targetClass8.ser(),
//            ),
//        ) { (a1, a2, a3, a4, a5, a6, a7, a8) ->
//            callback(
//                a1 as T1, a2 as T2, a3 as T3, a4 as T4,
//                a5 as T5, a6 as T6, a7 as T7, a8 as T8,
//            )
//        }
//    }
//
//    override fun remove(method: String) {
//        subscriptions.remove(method)?.forEach { it.cancel() }
//    }
//
//    // ------------------------------------------------------------------ helpers
//
//    /**
//     * Подписка стартует UNDISPATCHED: collect начинается синхронно в момент вызова on(),
//     * так что on() до start() гарантированно успевает зарегистрироваться.
//     * Исключение в callback не убивает подписку, а уходит в onError.
//     */
//    private fun <R> subscribe(method: String, flow: Flow<R>, handler: (R) -> Unit) {
//        val job = scope.launch(start = CoroutineStart.UNDISPATCHED) {
//            try {
//                flow.collect { item ->
//                    try {
//                        handler(item)
//                    } catch (e: Exception) {
//                        onError(e)
//                    }
//                }
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                onError(e)
//            }
//        }
//        subscriptions.getOrPut(method) { mutableListOf() }.add(job)
//    }
//
//    private suspend fun runReporting(block: suspend () -> Unit) {
//        try {
//            block()
//        } catch (e: CancellationException) {
//            throw e
//        } catch (e: Exception) {
//            onError(e)
//        }
//    }
//
//    // Gson больше нет: KClass -> KSerializer. Работает для примитивов и @Serializable-классов.
//    @OptIn(InternalSerializationApi::class)
//    private fun KClass<*>.ser(): KSerializer<Any> = (this as KClass<Any>).serializer()
//
//    private val json = Json { encodeDefaults = true }
//
//    // Any? -> JsonElement для send(). Примитивы, @Serializable-классы, списки, массивы, Map.
//    @OptIn(InternalSerializationApi::class)
//    private fun Any?.toJsonElement(): JsonElement = when (this) {
//        null -> JsonNull
//        is JsonElement -> this
//        is Collection<*> -> JsonArray(map { it.toJsonElement() })
//        is Array<*> -> JsonArray(map { it.toJsonElement() })
//        is Map<*, *> -> JsonObject(entries.associate { (k, v) -> k.toString() to v.toJsonElement() })
//        else -> json.encodeToJsonElement((this::class as KClass<Any>).serializer(), this)
//    }
//}
//
///**
// * ОБЫЧНЫЙ класс (не actual!) вместо expect/actual. Старый `expect class SignalRHubBuilder`
// * и все его `actual` нужно удалить, иначе будет конфликт. Конструктор совместим со старым (url),
// * остальные параметры опциональны.
// */
//
//
//// Пример configure:
//// SignalRHubBuilder(
////     url = "https://api.example.com/gamehub",
////     onError = { println("SignalR error: $it") },
////     configure = {
////         automaticReconnect = AutomaticReconnect.Active
////         accessTokenProvider = { tokenStorage.getToken() }
////     },
//// ).build()

package com.leafcellteam.mafia.network

import kotlin.reflect.KType
import kotlin.reflect.typeOf
import eu.lepicekmichal.signalrkore.HttpHubConnectionBuilder
import eu.lepicekmichal.signalrkore.HubConnection
import eu.lepicekmichal.signalrkore.HubConnectionBuilder
import eu.lepicekmichal.signalrkore.HubConnectionState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.leafcellteam.mafia.DOMAIN
import com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge
import com.leafcellteam.mafia.logw
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.ChatMessageData
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.ChatModel
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.SystemMessage
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.UserColor
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.WaitingRoomDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


interface SignalRHubConnection {
    fun start()
    fun stop()
    fun send(method: String, vararg args: Any?)
    fun <T> on(method: String, serializer: KSerializer<T>, callback: (T) -> Unit)
    fun <T1, T2> on(method: String, s1: KSerializer<T1>, s2: KSerializer<T2>, callback: (T1, T2) -> Unit)
    fun <T1, T2, T3> on(method: String, s1: KSerializer<T1>, s2: KSerializer<T2>, s3: KSerializer<T3>, callback: (T1, T2, T3) -> Unit)
    fun <T1, T2, T3, T4, T5, T6> on(
        method: String,
        s1: KSerializer<T1>,
        s2: KSerializer<T2>,
        s3: KSerializer<T3>,
        s4: KSerializer<T4>,
        s5: KSerializer<T5>,
        s6: KSerializer<T6>,
        callback: (T1, T2, T3, T4, T5, T6) -> Unit
    )
    fun <T1, T2, T3, T4, T5, T6, T7, T8> on(
        method: String,
        s1: KSerializer<T1>,
        s2: KSerializer<T2>,
        s3: KSerializer<T3>,
        s4: KSerializer<T4>,
        s5: KSerializer<T5>,
        s6: KSerializer<T6>,
        s7: KSerializer<T7>,
        s8: KSerializer<T8>,
        callback: (T1, T2, T3, T4, T5, T6, T7, T8) -> Unit
    )
    fun remove(method: String)
    fun onClosed(callback: (Exception?) -> Unit)
    val state: HubState
}

inline fun <reified T> SignalRHubConnection.on(
    method: String,
    noinline callback: (T) -> Unit
) {
    on(method, serializer<T>(), callback)
}

inline fun <reified T1, reified T2> SignalRHubConnection.on(
    method: String,
    noinline callback: (T1, T2) -> Unit
) {
    on(method, serializer<T1>(), serializer<T2>(), callback)
}

inline fun <reified T1, reified T2, reified T3> SignalRHubConnection.on(
    method: String,
    noinline callback: (T1, T2, T3) -> Unit
) {
    on(method, serializer<T1>(), serializer<T2>(), serializer<T3>(), callback)
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6> SignalRHubConnection.on(
    method: String,
    noinline callback: (T1, T2, T3, T4, T5, T6) -> Unit
) {
    on(
        method,
        serializer<T1>(), serializer<T2>(), serializer<T3>(),
        serializer<T4>(), serializer<T5>(), serializer<T6>(),
        callback
    )
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8> SignalRHubConnection.on(
    method: String,
    noinline callback: (T1, T2, T3, T4, T5, T6, T7, T8) -> Unit
) {
    on(
        method,
        serializer<T1>(), serializer<T2>(), serializer<T3>(), serializer<T4>(),
        serializer<T5>(), serializer<T6>(), serializer<T7>(), serializer<T8>(),
        callback
    )
}

enum class HubState {
    CONNECTED, DISCONNECTED, CONNECTING, RECONNECTING
}

class SignalRHubBuilder(
    private val url: String,
    private val onError: (Throwable) -> Unit = {},
    private val configure: (HttpHubConnectionBuilder.() -> Unit)? = null, // токен, реконнект и т.д.
) {
    fun build(): SignalRHubConnection {
        val hubConnection = if (configure != null) {
            HubConnectionBuilder.create(url, configure)
        } else {
            HubConnectionBuilder.create(url)
        }
        return KoreSignalRHubConnection(hubConnection, onError)
    }
}

//@file:OptIn(InternalSerializationApi::class)
//@file:Suppress("UNCHECKED_CAST")
//


/**
 * Реализация [SignalRHubConnection] на SignalRKore. Лежит в commonMain,
 * отдельные Android/iOS реализации и expect/actual больше не нужны.
 *
 * Не потокобезопасно только управление подписками (on/remove): вызывайте их
 * с одного потока (обычно main).
 */
class KoreSignalRHubConnection(
    private val hubConnection: HubConnection,
    private val onError: (Throwable) -> Unit = {},
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : SignalRHubConnection {

    private val subscriptions = mutableMapOf<String, MutableList<Job>>()

    // Очередь исходящих: сохраняет порядок send(), в отличие от launch на каждый вызов.
    private val sendQueue = Channel<suspend () -> Unit>(Channel.UNLIMITED)

    init {
        scope.launch {
            for (task in sendQueue) {
                runReporting(task)
            }
        }
    }

    // ---------------------------------------------------------------- lifecycle

    override fun start() {
        scope.launch { runReporting { hubConnection.start() } }
    }

    override fun stop() {
        scope.launch { runReporting { hubConnection.stop() } }
    }

    override val state: HubState
        get() = when (hubConnection.connectionState.value) {
            HubConnectionState.CONNECTED -> HubState.CONNECTED
            HubConnectionState.CONNECTING,
            HubConnectionState.RECONNECTING -> HubState.CONNECTING
            HubConnectionState.DISCONNECTED -> HubState.DISCONNECTED
        }

    /**
     * В SignalRKore нет колбэка закрытия, поэтому он собран из connectionState:
     * вызывается, когда соединение, бывшее CONNECTED, оказалось DISCONNECTED
     * (в том числе после stop() и после неудачных попыток реконнекта).
     * Исключение недоступно — всегда null.
     */
    override fun onClosed(callback: (Exception?) -> Unit) {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            var wasConnected = false
            hubConnection.connectionState.collect { s ->
                when (s) {
                    HubConnectionState.CONNECTED -> wasConnected = true
                    HubConnectionState.DISCONNECTED -> if (wasConnected) {
                        wasConnected = false
                        try { callback(null) } catch (e: Exception) { onError(e) }
                    }
                    else -> Unit
                }
            }
        }
    }

    // --------------------------------------------------------------------- send

    override fun send(method: String, vararg args: Any?) {
        // SignalRKore принимает уже сериализованные аргументы (List<JsonElement>)
        val elements = try {
            args.map { it.toJsonElement() }
        } catch (e: Exception) {
            onError(e)
            return
        }
        sendQueue.trySend { hubConnection.send(method, elements) }
    }

    // ----------------------------------------------------------------------- on

    override fun <T> on(method: String, serializer: KSerializer<T>, callback: (T) -> Unit) {
        subscribe(method, hubConnection.on(method, serializer)) { (a) ->
            callback(a as T)
        }
    }

    override fun <T1, T2> on(
        method: String,
        s1: KSerializer<T1>,
        s2: KSerializer<T2>,
        callback: (T1, T2) -> Unit,
    ) {
        subscribe(
            method,
            hubConnection.on(method, s1, s2),
        ) { (a1, a2) ->
            callback(a1 as T1, a2 as T2)
        }
    }

    override fun <T1, T2, T3> on(
        method: String,
        s1: KSerializer<T1>,
        s2: KSerializer<T2>,
        s3: KSerializer<T3>,
        callback: (T1, T2, T3) -> Unit,
    ) {
        subscribe(
            method,
            hubConnection.on(method, s1, s2, s3),
        ) { (a1, a2, a3) ->
            callback(a1 as T1, a2 as T2, a3 as T3)
        }
    }

    override fun <T1, T2, T3, T4, T5, T6> on(
        method: String,
        s1: KSerializer<T1>,
        s2: KSerializer<T2>,
        s3: KSerializer<T3>,
        s4: KSerializer<T4>,
        s5: KSerializer<T5>,
        s6: KSerializer<T6>,
        callback: (T1, T2, T3, T4, T5, T6) -> Unit,
    ) {
        subscribe(
            method,
            hubConnection.on(
                method,
                s1, s2, s3, s4, s5, s6,
            ),
        ) { (a1, a2, a3, a4, a5, a6) ->
            callback(a1 as T1, a2 as T2, a3 as T3, a4 as T4, a5 as T5, a6 as T6)
        }
    }

    override fun <T1, T2, T3, T4, T5, T6, T7, T8> on(
        method: String,
        s1: KSerializer<T1>,
        s2: KSerializer<T2>,
        s3: KSerializer<T3>,
        s4: KSerializer<T4>,
        s5: KSerializer<T5>,
        s6: KSerializer<T6>,
        s7: KSerializer<T7>,
        s8: KSerializer<T8>,
        callback: (T1, T2, T3, T4, T5, T6, T7, T8) -> Unit,
    ) {
        subscribe(
            method,
            hubConnection.on(
                method,
                s1, s2, s3, s4, s5, s6, s7, s8,
            ),
        ) { (a1, a2, a3, a4, a5, a6, a7, a8) ->
            callback(
                a1 as T1, a2 as T2, a3 as T3, a4 as T4,
                a5 as T5, a6 as T6, a7 as T7, a8 as T8,
            )
        }
    }

    override fun remove(method: String) {
        subscriptions.remove(method)?.forEach { it.cancel() }
    }

    // ------------------------------------------------------------------ helpers

    /**
     * Подписка стартует UNDISPATCHED: collect начинается синхронно в момент вызова on(),
     * так что on() до start() гарантированно успевает зарегистрироваться.
     * Исключение в callback не убивает подписку, а уходит в onError.
     */
    private fun <R> subscribe(method: String, flow: Flow<R>, handler: (R) -> Unit) {
        val job = scope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                flow.collect { item ->
                    try {
                        handler(item)
                    } catch (e: Exception) {
                        onError(e)
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onError(e)
            }
        }
        subscriptions.getOrPut(method) { mutableListOf() }.add(job)
    }

    private suspend fun runReporting(block: suspend () -> Unit) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onError(e)
        }
    }

    private val json = Json { encodeDefaults = true }

    // Any? -> JsonElement для send(). Примитивы, @Serializable-классы, списки, массивы, Map.
    @OptIn(InternalSerializationApi::class)
    private fun Any?.toJsonElement(): JsonElement = when (this) {
        null -> JsonNull
        is JsonElement -> this
        is Collection<*> -> JsonArray(map { it.toJsonElement() })
        is Array<*> -> JsonArray(map { it.toJsonElement() })
        is Map<*, *> -> JsonObject(entries.associate { (k, v) -> k.toString() to v.toJsonElement() })
        else -> json.encodeToJsonElement((this::class as KClass<Any>).serializer(), this)
    }
}

/**
 * ОБЫЧНЫЙ класс (не actual!) вместо expect/actual. Старый `expect class SignalRHubBuilder`
 * и все его `actual` нужно удалить, иначе будет конфликт. Конструктор совместим со старым (url),
 * остальные параметры опциональны.
 */


// Пример configure:
// SignalRHubBuilder(
//     url = "https://api.example.com/gamehub",
//     onError = { println("SignalR error: $it") },
//     configure = {
//         automaticReconnect = AutomaticReconnect.Active
//         accessTokenProvider = { tokenStorage.getToken() }
//     },
// ).build()

// -----------------------------------------------------------------------------------------------
// Перенесено из com.leafcellteam.mafia.waitingSection.signalRServiceHub.SignalRServiceHub (файл
// SignalRServiceHub.kt) без изменений логики — теперь весь SignalR-код живёт в одном месте, рядом
// со своей обёрткой над SignalRKore.
// -----------------------------------------------------------------------------------------------

private const val HUB = "waitingRoomHub"
private const val SERVER_URL = "$DOMAIN$HUB"

class SignalRServiceHub(private val gameRoomServiceHub: GameRoomServiceHub) {
    private val hubConnection: SignalRHubConnection = SignalRHubBuilder(SERVER_URL).build()

    private val _waitingRoomDto = MutableStateFlow<WaitingRoomDto?>(null)
    val waitingRoomDto: StateFlow<WaitingRoomDto?> = _waitingRoomDto.asStateFlow()

    private val _playerId = MutableStateFlow(0)
    val playerId: StateFlow<Int> = _playerId.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessageData>?>(null)
    val messages: StateFlow<List<ChatMessageData>?> = _messages.asStateFlow()

    private val _systemMessages = MutableStateFlow(emptyList<SystemMessage>())
    val systemMessages: StateFlow<List<SystemMessage>> = _systemMessages.asStateFlow()

    private val _userColors = MutableStateFlow(emptyList<UserColor>())
    var userColors: StateFlow<List<UserColor>> = _userColors.asStateFlow()

    private val _counter = MutableStateFlow(25)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private var _isDataLoaded by mutableStateOf(false)

    private fun receiveWaitingRoomData() {
        logd("WaitingRoomViewModel", "Setting up ReceiveWaitingRoomData handler")
        hubConnection.on("ReceiveWaitingRoomData") { roomId: String, roomName: String, players: List<String>, chosenRoles: List<String>, minPlayers: Int, maxPlayers: Int ->
            logd("WaitingRoomViewModel", "Received room data: $roomId, $roomName, players: ${players.size}")
            _waitingRoomDto.value = WaitingRoomDto(roomId, roomName, players, minPlayers, maxPlayers, chosenRoles)
            gameRoomServiceHub.setGuid(roomId)
            _isDataLoaded = true
        }
    }

    private fun setupReceivePlayerId() {
        hubConnection.remove("ReceivePlayerId")
        hubConnection.on("ReceivePlayerId") { receivedId: Int ->
            _playerId.value = receivedId
        }
    }

    private fun setupReceivePlayers() {
        hubConnection.remove("ReceivePlayers")
        hubConnection.on("ReceivePlayers") { players: List<String> ->
            _waitingRoomDto.value = _waitingRoomDto.value?.copy(players = players) ?: _waitingRoomDto.value
        }
    }

    private fun setupReceiveMessage() {
        hubConnection.remove("ReceiveMessage")
        hubConnection.on("ReceiveMessage") { chatModels: List<ChatModel?> ->
            val updatedChatModels = chatModels.map { ChatMessageData(it?.playerId ?: 0, it?.playerName ?: "Loading...", it?.message ?: "") }
            _messages.value = updatedChatModels
        }
    }

    private fun clearMessages() {
        _messages.value = null
    }

    private fun setupBackgroundImage() {
        hubConnection.remove("GetBackgroundImage")
        hubConnection.on("GetBackgroundImage") { playerNames: List<String>, backgroundImages: List<List<Int>> ->
            this._userColors.value = playerNames.zip(backgroundImages).map { (name, color) ->
                UserColor(name, Color(color[0], color[1], color[2]))
            }
        }
    }

    private fun setupSystemMessage() {
        hubConnection.remove("SystemMessage")
        hubConnection.on("SystemMessage") { content: String ->
            val newSystemMessage = SystemMessage(content)
            _systemMessages.value += newSystemMessage
        }
    }

    private fun setupStartGame() {
        hubConnection.remove("GoToGame")
        hubConnection.on("GoToGame") { _: Unit ->
            logd("SignalR", "GoToGame")
            hubConnection.stop()
            gameRoomServiceHub.startConnection()
        }
    }

    private fun setupTimer() {
        hubConnection.remove("GetTimer")
        hubConnection.on("GetTimer") { counter: Int ->
            _counter.value = counter
        }
    }

    fun resetWaitingRoomData() {
        this._waitingRoomDto.value = null
        this._messages.value = emptyList()
        this._userColors.value = emptyList()
        this._isDataLoaded = false
    }

    fun sendPlayerMessage(guid: String, playerId: Int, playerName: String, message: String) {
        try {
            if (playerName.isNotBlank() && message.isNotBlank()) {
                hubConnection.send("SendMessage", guid, playerId, playerName, message)
            }
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "sendPlayerMessage: ${e.message}")
        }
    }

    fun startConnection() {
        logd("WaitingRoomViewModel", "Attempting to connect to hub")
        try {
            if(hubConnection.state == HubState.DISCONNECTED) {
                logd("WaitingRoomViewModel", "Starting hub connection")
                hubConnection.start()
            } else {
                logd("WaitingRoomViewModel", "Hub already connected: ${hubConnection.state}")
            }
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "Exception in waitingRoomConnection: ${e.message}")
        }
    }

    fun sendLeaveRoom(guid: String, playerId: Int) {
        try {
            hubConnection.send("LeaveRoom", guid, playerId)
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "sendLeaveRoom: ${e.message}")
        }
    }

    fun requestJoinGame(guid: String, playerName: String, playerId: Int) {
        clearMessages()
        try {
            if (guid.isBlank()) {
                loge("HUB", "Invalid GUID: Empty string")
                return
            }
            if (hubConnection.state == HubState.CONNECTED) {
                hubConnection.send("JoinGame", guid, playerName, playerId)
                logd("HUB", "JoinGame request sent")
                setupReceiveMessage()
            } else {
                logw("SignalR", "Error: hub not connected")
            }
        } catch (e: Exception) {
            loge("SignalR", "Send error JoinGame: ${e.message}")
        }
    }

    fun requestStartGame(guid: String) {
        try {
            if(guid.isNotBlank()) {
                hubConnection.send("StartGame", guid)
            } else {
                logw("SignalR", "Cannot requestStartGame now (guid='$guid')")
            }
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "requestStartGame: ${e.message}")
        }
    }

    fun changePlayerID(playerId: Int) {
        _playerId.value = playerId
    }

    fun stopConnection() {
        try {
            hubConnection.stop()
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "stopConnection: ${e.message}")
        }
    }

    fun reset() {
        _waitingRoomDto.value = _waitingRoomDto.value?.copy(
            roomName = "",
            players = emptyList(),
            maxPlayers = 0,
            chosenRoles = emptyList()
        )
        _isDataLoaded = false
        _messages.value = null
        _userColors.value = emptyList()
        _systemMessages.value = emptyList()
    }

    fun resetCounter() {
        _counter.value = 25
    }

    fun setupHub() {
        receiveWaitingRoomData()
        setupReceivePlayerId()
        setupBackgroundImage()
        setupReceivePlayers()
        setupReceiveMessage()
        setupSystemMessage()
        setupStartGame()
        setupTimer()
    }

    fun getHubConnection(): SignalRHubConnection = hubConnection
}