// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Array
import skip.lib.Set

import skip.foundation.*

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

internal val logger: SkipLogger = SkipLogger(subsystem = "skip.ui", category = "SkipUI") // adb logcat '*:S' 'skip.ui.SkipUI:V'

open class UIApplication {

    private constructor() {
        val lifecycle = ProcessLifecycleOwner.get().lifecycle.sref()
        lifecycle.addObserver(UIApplicationLifecycleEventObserver(application = this))
    }

    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open var delegate: Any?
        get() {
            fatalError()
        }
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open var isIdleTimerDisabled: Boolean
        get() {
            fatalError()
        }
        set(newValue) {
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun canOpenURL(url: URL): Boolean {
        fatalError()
    }

    open suspend fun open(url: URL, options: Dictionary<UIApplication.OpenExternalURLOptionsKey, Any> = dictionaryOf()): Boolean = MainActor.run l@{
        val context = ProcessInfo.processInfo.androidContext.sref()
        try {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url.absoluteString))
            // needed or else: android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return@l true
        } catch (error: Throwable) {
            @Suppress("NAME_SHADOWING") val error = error.aserror()
            logger.warning("UIApplication.launch error: ${error}")
            return@l false
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun sendEvent(event: Any) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun sendAction(action: Any, to: Any?, from: Any?, for_: Any?): Boolean {
        val target = to
        val sender = from
        val event = for_
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun supportedInterfaceOrientations(for_: Any?): Any {
        val window = for_
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open var applicationSupportsShakeToEdit: Boolean
        get() {
            fatalError()
        }
        set(newValue) {
        }

    open var applicationState: UIApplication.State
        get() = _applicationState.value
        internal set(newValue) {
            _applicationState.value = newValue
        }
    private val _applicationState: MutableState<UIApplication.State> = mutableStateOf(UIApplication.State.active)

    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val backgroundTimeRemaining: Double
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun beginBackgroundTask(expirationHandler: (() -> Unit)? = null): Any {
        val handler = expirationHandler
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun beginBackgroundTask(withName: String?, expirationHandler: (() -> Unit)? = null): Any {
        val taskName = withName
        val handler = expirationHandler
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun endBackgroundTask(identifier: Any) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val backgroundRefreshStatus: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val isProtectedDataAvailable: Boolean
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val userInterfaceLayoutDirection: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val preferredContentSizeCategory: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val connectedScenes: Set<AnyHashable>
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val openSessions: Set<AnyHashable>
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val supportsMultipleScenes: Boolean
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun requestSceneSessionDestruction(sceneSession: Any, options: Any?, errorHandler: ((Error) -> Unit)? = null) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun requestSceneSessionRefresh(sceneSession: Any) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun activateSceneSession(for_: Any, errorHandler: ((Error) -> Unit)? = null) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun registerForRemoteNotifications() = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun unregisterForRemoteNotifications() = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val isRegisteredForRemoteNotifications: Boolean
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun beginReceivingRemoteControlEvents() = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun endReceivingRemoteControlEvents() = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val shortcutItems: Any?
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val supportsAlternateIcons: Boolean
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun setAlternateIconName(alternateIconName: String?, completionHandler: ((Error?) -> Unit)? = null) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open suspend fun setAlternateIconName(alternateIconName: String?): Unit = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val alternateIconName: String?
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun extendStateRestoration() = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun completeStateRestoration() = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun ignoreSnapshotOnNextApplicationLaunch() = Unit

    enum class State(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): RawRepresentable<Int> {
        active(0),
        inactive(1),
        background(2);

        companion object {
        }
    }

    class OpenExternalURLOptionsKey: RawRepresentable<String> {
        override val rawValue: String
        constructor(rawValue: String) {
            this.rawValue = rawValue
        }

        override fun equals(other: Any?): Boolean {
            if (other !is UIApplication.OpenExternalURLOptionsKey) return false
            return rawValue == other.rawValue
        }

        override fun hashCode(): Int {
            var result = 1
            result = Hasher.combine(result, rawValue)
            return result
        }

        companion object {

            val universalLinksOnly = OpenExternalURLOptionsKey(rawValue = "universalLinksOnly")
            val eventAttribution = OpenExternalURLOptionsKey(rawValue = "eventAttribution")
        }
    }

    companion object: CompanionClass() {
        override val shared = UIApplication()
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val openNotificationSettingsURLString: String
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun registerObject(forStateRestoration: Any, restorationIdentifier: String) = Unit
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val didEnterBackgroundNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val willEnterForegroundNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val didFinishLaunchingNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val didBecomeActiveNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val willResignActiveNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val didReceiveMemoryWarningNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val willTerminateNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val significantTimeChangeNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val backgroundRefreshStatusDidChangeNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val protectedDataWillBecomeUnavailableNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val protectedDataDidBecomeAvailableNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val openSettingsURLString: String
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val userDidTakeScreenshotNotification: Notification.Name
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val invalidInterfaceOrientationException: Any
            get() {
                fatalError()
            }

        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val backgroundFetchIntervalMinimum: Double
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val backgroundFetchIntervalNever: Double
            get() {
                fatalError()
            }

        override fun State(rawValue: Int): UIApplication.State? {
            return when (rawValue) {
                0 -> State.active
                1 -> State.inactive
                2 -> State.background
                else -> null
            }
        }
    }
    open class CompanionClass {
        open val shared
            get() = UIApplication.shared
        open fun State(rawValue: Int): UIApplication.State? = UIApplication.State(rawValue = rawValue)
    }
}

internal class UIApplicationLifecycleEventObserver: LifecycleEventObserver, DefaultLifecycleObserver {
    internal val application: UIApplication

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        for (unusedi in 0..0) {
            when (event) {
                Lifecycle.Event.ON_CREATE -> break
                Lifecycle.Event.ON_START -> break
                Lifecycle.Event.ON_RESUME -> application.applicationState = UIApplication.State.active
                Lifecycle.Event.ON_PAUSE -> application.applicationState = UIApplication.State.inactive
                Lifecycle.Event.ON_STOP -> application.applicationState = UIApplication.State.background
                Lifecycle.Event.ON_DESTROY -> break
                Lifecycle.Event.ON_ANY -> break
            }
        }
    }

    constructor(application: UIApplication) {
        this.application = application
    }
}

open class UIImage {
    internal val bitmap: Bitmap?

    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(named: String, in_: Bundle? = null, compatibleWith: Any? = null) {
        val bundle = in_
        val traitCollection = compatibleWith
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(named: String, in_: Bundle?, with: UIImage.Configuration?, unusedp: Unit? = null) {
        val bundle = in_
        val configuration = with
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(named: String, in_: Bundle? = null, variableValue: Double, configuration: UIImage.Configuration? = null) {
        val bundle = in_
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(imageLiteralResourceName: String, unusedp_0: Unit? = null, unusedp_1: Unit? = null) {
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(systemName: String, withConfiguration: UIImage.Configuration? = null, unusedp_0: Unit? = null, unusedp_1: Unit? = null, unusedp_2: Unit? = null) {
        val configuration = withConfiguration
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(systemName: String, variableValue: Double, configuration: UIImage.Configuration? = null, unusedp: Unit? = null) {
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(systemName: String, compatibleWith: Any?, unusedp_0: Unit? = null, unusedp_1: Unit? = null, unusedp_2: Unit? = null, unusedp_3: Unit? = null) {
        val traitCollection = compatibleWith
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(resource: Any, unusedp_0: Unit? = null, unusedp_1: Unit? = null, unusedp_2: Unit? = null, unusedp_3: Unit? = null) {
        this.bitmap = null
        this.scale = 1.0
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun preparingForDisplay(): UIImage? {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun prepareForDisplay(completionHandler: (UIImage?) -> Unit) {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun preparingThumbnail(of: CGSize): UIImage? {
        val size = of
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun prepareThumbnail(of: CGSize, completionHandler: (UIImage?) -> Unit) {
        val size = of
        fatalError()
    }

    constructor(contentsOfFile: String) {
        val path = contentsOfFile
        val bitmap_0 = BitmapFactory.decodeFile(path)
        if (bitmap_0 == null) {
            throw NullReturnException()
        }
        this.bitmap = bitmap_0.sref()
        this.scale = 1.0
    }

    constructor(data: Data, scale: Double = 1.0) {
        val bytes = data.kotlin(nocopy = true)
        val bitmap_1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.count())
        if (bitmap_1 == null) {
            throw NullReturnException()
        }
        this.bitmap = bitmap_1.sref()
        this.scale = scale
    }

    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    class UIImageReader {

        companion object {
        }
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun withConfiguration(configuration: UIImage.Configuration): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun applyingSymbolConfiguration(configuration: Any): UIImage? {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun imageFlippedForRightToLeftLayoutDirection(): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun withHorizontallyFlippedOrientation(): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun withRenderingMode(renderingMode: Any): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun withAlignmentRectInsets(insets: Any): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun resizableImage(withCapInsets: Any, resizingMode: Any? = null): UIImage {
        val insets = withCapInsets
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun imageWithoutBaseline(): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun withBaselineOffset(fromBottom: Double): UIImage {
        val offset = fromBottom
        fatalError()
    }

    val scale: Double

    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val size: CGSize
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val imageOrientation: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val flipsForRightToLeftLayoutDirection: Boolean
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val resizingMode: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val duration: Double
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val capInsets: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val alignmentRectInsets: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val isSymbolImage: Boolean
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val configuration: Any?
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val symbolConfiguration: Any?
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val traitCollection: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val isHighDynamicRange: Boolean
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun imageRestrictedToStandardDynamicRange(): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun heicData(): Data? {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val baselineOffsetFromBottom: Double?
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open val renderingMode: Any
        get() {
            fatalError()
        }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun withTintColor(color: Any, renderingMode: Any? = null): UIImage {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun draw(at: CGPoint, blendMode: Any? = null, alpha: Double? = null) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    internal open fun draw(in_: CGRect, blendMode: Any? = null, alpha: Double? = null) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun drawAsPattern(in_: CGRect) = Unit
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun jpegData(compressionQuality: Double): Data? {
        fatalError()
    }
    @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    open fun pngData(): Data? {
        fatalError()
    }

    class Configuration {

        companion object {
        }
    }

    companion object: CompanionClass() {
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun animatedImageNamed(name: String, duration: Double): UIImage? {
            fatalError()
        }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun animatedImage(with: Array<UIImage>, duration: Double): UIImage? {
            val images = with
            fatalError()
        }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        fun animatedResizableImageNamed(name: String, capInsets: Any, resizingMode: Any? = null, duration: Double): UIImage? {
            fatalError()
        }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val add: UIImage
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val remove: UIImage
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val actions: UIImage
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val checkmark: UIImage
            get() {
                fatalError()
            }
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val strokedCheckmark: UIImage
            get() {
                fatalError()
            }
    }
    open class CompanionClass {
    }
}

enum class UIKeyboardType(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): RawRepresentable<Int> {
    default(0),
    asciiCapable(1),
    numbersAndPunctuation(2),
    URL(3),
    numberPad(4),
    phonePad(5),
    namePhonePad(6),
    emailAddress(7),
    decimalPad(8),
    twitter(9),
    webSearch(10),
    asciiCapableNumberPad(11),
    alphabet(12);

    internal fun asComposeKeyboardType(): KeyboardType {
        when (this) {
            UIKeyboardType.default -> return KeyboardType.Text.sref()
            UIKeyboardType.asciiCapable -> return KeyboardType.Ascii.sref()
            UIKeyboardType.numbersAndPunctuation -> return KeyboardType.Text.sref()
            UIKeyboardType.URL -> return KeyboardType.Uri.sref()
            UIKeyboardType.numberPad -> return KeyboardType.Number.sref()
            UIKeyboardType.phonePad -> return KeyboardType.Phone.sref()
            UIKeyboardType.namePhonePad -> return KeyboardType.Text.sref()
            UIKeyboardType.emailAddress -> return KeyboardType.Email.sref()
            UIKeyboardType.decimalPad -> return KeyboardType.Decimal.sref()
            UIKeyboardType.twitter -> return KeyboardType.Text.sref()
            UIKeyboardType.webSearch -> return KeyboardType.Text.sref()
            UIKeyboardType.asciiCapableNumberPad -> return KeyboardType.Text.sref()
            UIKeyboardType.alphabet -> return KeyboardType.Text.sref()
        }
    }

    companion object {
    }
}

fun UIKeyboardType(rawValue: Int): UIKeyboardType? {
    return when (rawValue) {
        0 -> UIKeyboardType.default
        1 -> UIKeyboardType.asciiCapable
        2 -> UIKeyboardType.numbersAndPunctuation
        3 -> UIKeyboardType.URL
        4 -> UIKeyboardType.numberPad
        5 -> UIKeyboardType.phonePad
        6 -> UIKeyboardType.namePhonePad
        7 -> UIKeyboardType.emailAddress
        8 -> UIKeyboardType.decimalPad
        9 -> UIKeyboardType.twitter
        10 -> UIKeyboardType.webSearch
        11 -> UIKeyboardType.asciiCapableNumberPad
        12 -> UIKeyboardType.alphabet
        else -> null
    }
}

class UITextContentType: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is UITextContentType) return false
        return rawValue == other.rawValue
    }

    companion object {

        val name = UITextContentType(rawValue = 0) // Not allowed as a Kotlin enum case name
        val namePrefix = UITextContentType(rawValue = 1)
        val givenName = UITextContentType(rawValue = 2)
        val middleName = UITextContentType(rawValue = 3)
        val familyName = UITextContentType(rawValue = 4)
        val nameSuffix = UITextContentType(rawValue = 5)
        val nickname = UITextContentType(rawValue = 6)
        val jobTitle = UITextContentType(rawValue = 7)
        val organizationName = UITextContentType(rawValue = 8)
        val location = UITextContentType(rawValue = 9)
        val fullStreetAddress = UITextContentType(rawValue = 10)
        val streetAddressLine1 = UITextContentType(rawValue = 11)
        val streetAddressLine2 = UITextContentType(rawValue = 12)
        val addressCity = UITextContentType(rawValue = 13)
        val addressState = UITextContentType(rawValue = 14)
        val addressCityAndState = UITextContentType(rawValue = 15)
        val sublocality = UITextContentType(rawValue = 16)
        val countryName = UITextContentType(rawValue = 17)
        val postalCode = UITextContentType(rawValue = 18)
        val telephoneNumber = UITextContentType(rawValue = 19)
        val emailAddress = UITextContentType(rawValue = 20)
        val URL = UITextContentType(rawValue = 21)
        val creditCardNumber = UITextContentType(rawValue = 22)
        val username = UITextContentType(rawValue = 23)
        val password = UITextContentType(rawValue = 24)
        val newPassword = UITextContentType(rawValue = 25)
        val oneTimeCode = UITextContentType(rawValue = 26)
        val shipmentTrackingNumber = UITextContentType(rawValue = 27)
        val flightNumber = UITextContentType(rawValue = 28)
        val dateTime = UITextContentType(rawValue = 29)
        val birthdate = UITextContentType(rawValue = 30)
        val birthdateDay = UITextContentType(rawValue = 31)
        val birthdateMonth = UITextContentType(rawValue = 32)
        val birthdateYear = UITextContentType(rawValue = 33)
        val creditCardSecurityCode = UITextContentType(rawValue = 34)
        val creditCardName = UITextContentType(rawValue = 35)
        val creditCardGivenName = UITextContentType(rawValue = 36)
        val creditCardMiddleName = UITextContentType(rawValue = 37)
        val creditCardFamilyName = UITextContentType(rawValue = 38)
        val creditCardExpiration = UITextContentType(rawValue = 39)
        val creditCardExpirationMonth = UITextContentType(rawValue = 40)
        val creditCardExpirationYear = UITextContentType(rawValue = 41)
        val creditCardType = UITextContentType(rawValue = 42)
    }
}

interface UIFeedbackGenerator {

    // note that this needs AndroidManifest.xml permission:
    // <uses-permission android:name="android.permission.VIBRATE"/>
    val vibrator: android.os.Vibrator?
        get() {
            val context = ProcessInfo.processInfo.androidContext.sref() // Android-specific extension to get the global Context
            val vibratorManager_0 = (context.getSystemService(android.content.Context.VIBRATOR_MANAGER_SERVICE) as? android.os.VibratorManager).sref()
            if (vibratorManager_0 == null) {
                logger.log("vibratorManager: returned null")
                return null
            }

            logger.log("vibratorManager: ${vibratorManager_0}")

            // https://developer.android.com/reference/android/os/Vibrator
            return vibratorManager_0.getDefaultVibrator()
        }
}

/// UIImpactFeedbackGenerator is used to give user feedback when an impact between UI elements occurs
open class UIImpactFeedbackGenerator: UIFeedbackGenerator {
    private val style: UIImpactFeedbackGenerator.FeedbackStyle

    constructor() {
        this.style = UIImpactFeedbackGenerator.FeedbackStyle.medium
    }

    constructor(style: UIImpactFeedbackGenerator.FeedbackStyle) {
        this.style = style
    }

    /// call when your UI element impacts something else
    open fun impactOccurred() {
        this.vibrator?.vibrate(style.vibrationEffect)
    }

    /// call when your UI element impacts something else with a specific intensity [0.0, 1.0]
    open fun impactOccurred(intensity: Double) {
        if (intensity <= 0.0) {
            return
        }

        val effect = android.os.VibrationEffect.startComposition()
            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, Float(intensity), 0)
            .compose()
        this.vibrator?.vibrate(effect)
    }

    open fun impactOccurred(intensity: Double, at: CGPoint) {
        val location = at
        impactOccurred(intensity = intensity)
    }

    enum class FeedbackStyle(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): Sendable, RawRepresentable<Int> {
        light(0),
        medium(1),
        heavy(2),

        soft(3),
        rigid(4);

        internal val vibrationEffect: android.os.VibrationEffect
            get() {
                when (this) {
                    UIImpactFeedbackGenerator.FeedbackStyle.light -> return android.os.VibrationEffect.createPredefined(android.os.VibrationEffect.EFFECT_TICK)
                    UIImpactFeedbackGenerator.FeedbackStyle.medium -> return android.os.VibrationEffect.createPredefined(android.os.VibrationEffect.EFFECT_CLICK)
                    UIImpactFeedbackGenerator.FeedbackStyle.heavy -> return android.os.VibrationEffect.createPredefined(android.os.VibrationEffect.EFFECT_HEAVY_CLICK)
                    UIImpactFeedbackGenerator.FeedbackStyle.soft -> return android.os.VibrationEffect.createPredefined(android.os.VibrationEffect.EFFECT_TICK)
                    UIImpactFeedbackGenerator.FeedbackStyle.rigid -> return android.os.VibrationEffect.createPredefined(android.os.VibrationEffect.EFFECT_CLICK)
                }
            }

        companion object {
        }
    }

    companion object: CompanionClass() {

        override fun FeedbackStyle(rawValue: Int): UIImpactFeedbackGenerator.FeedbackStyle? {
            return when (rawValue) {
                0 -> FeedbackStyle.light
                1 -> FeedbackStyle.medium
                2 -> FeedbackStyle.heavy
                3 -> FeedbackStyle.soft
                4 -> FeedbackStyle.rigid
                else -> null
            }
        }
    }
    open class CompanionClass {
        open fun FeedbackStyle(rawValue: Int): UIImpactFeedbackGenerator.FeedbackStyle? = UIImpactFeedbackGenerator.FeedbackStyle(rawValue = rawValue)
    }
}

/// UINotificationFeedbackGenerator is used to give user feedback when an notification is displayed
open class UINotificationFeedbackGenerator: UIFeedbackGenerator {

    constructor() {
    }

    /// call when a notification is displayed, passing the corresponding type
    open fun notificationOccurred(notificationType: UINotificationFeedbackGenerator.FeedbackType) {
        // amplitude parameter: “The strength of the vibration. This must be a value between 1 and 255”
        this.vibrator?.vibrate(notificationType.vibrationEffect)
    }

    /// call when a notification is displayed, passing the corresponding type
    open fun notificationOccurred(notificationType: UINotificationFeedbackGenerator.FeedbackType, at: CGPoint) {
        val location = at
        notificationOccurred(notificationType)
    }

    enum class FeedbackType(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): Sendable, RawRepresentable<Int> {
        success(0),
        warning(1),
        error(2);

        internal val vibrationEffect: android.os.VibrationEffect
            get() {
                when (this) {
                    UINotificationFeedbackGenerator.FeedbackType.success -> {
                        return android.os.VibrationEffect.startComposition()
                            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, 0.8f, 0)
                            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, 0.8f, 150)
                            .compose()
                    }
                    UINotificationFeedbackGenerator.FeedbackType.warning -> {
                        return android.os.VibrationEffect.startComposition()
                            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, 0.8f, 0)
                            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, 0.8f, 150)
                            .compose()
                    }
                    UINotificationFeedbackGenerator.FeedbackType.error -> {
                        return android.os.VibrationEffect.startComposition()
                            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, 0.5f, 0)
                            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, 0.7f, 100)
                            .addPrimitive(android.os.VibrationEffect.Composition.PRIMITIVE_CLICK, 0.9f, 150)
                            .compose()
                    }
                }
            }

        companion object {
        }
    }

    companion object: CompanionClass() {

        override fun FeedbackType(rawValue: Int): UINotificationFeedbackGenerator.FeedbackType? {
            return when (rawValue) {
                0 -> FeedbackType.success
                1 -> FeedbackType.warning
                2 -> FeedbackType.error
                else -> null
            }
        }
    }
    open class CompanionClass {
        open fun FeedbackType(rawValue: Int): UINotificationFeedbackGenerator.FeedbackType? = UINotificationFeedbackGenerator.FeedbackType(rawValue = rawValue)
    }
}


/// UINotificationFeedbackGenerator is used to give user feedback when an notification is displayed
open class UISelectionFeedbackGenerator: UIFeedbackGenerator {

    constructor() {
    }

    /// call when a notification is displayed, passing the corresponding type
    open fun selectionChanged() {
        this.vibrator?.vibrate(android.os.VibrationEffect.createPredefined(android.os.VibrationEffect.EFFECT_TICK))
    }

    open fun selectionChanged(at: CGPoint) {
        val location = at
        selectionChanged()
    }


    companion object: CompanionClass() {
    }
    open class CompanionClass {
    }
}

