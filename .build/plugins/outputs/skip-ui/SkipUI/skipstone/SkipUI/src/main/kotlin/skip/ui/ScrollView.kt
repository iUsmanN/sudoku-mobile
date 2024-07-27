// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Array
import skip.lib.Set

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

class ScrollView: View {
    internal val content: ComposeBuilder
    internal val axes: Axis.Set

    constructor(axes: Axis.Set = Axis.Set.vertical, content: () -> View) {
        this.axes = axes.sref()
        this.content = ComposeBuilder.from(content)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        // Some components in Compose have their own scrolling built in, so we'll look for them
        // below before adding our scrolling modifiers
        val firstView = content.collectViews(context = context).first?.strippingModifiers { it -> it }

        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        var scrollModifier: Modifier = Modifier
        if (axes.contains(Axis.Set.vertical) && !(firstView is LazyVStack) && !(firstView is LazyVGrid)) {
            scrollModifier = scrollModifier.verticalScroll(scrollState)
            if (!axes.contains(Axis.Set.horizontal)) {
                // Integrate with our scroll-to-top navigation bar taps
                PreferenceValues.shared.contribute(context = context, key = ScrollToTopPreferenceKey::class, value = { ->
                    coroutineScope.launch { -> scrollState.animateScrollTo(0) }
                })
            }
        }
        if (axes.contains(Axis.Set.horizontal) && !(firstView is LazyHStack) && !(firstView is LazyHGrid)) {
            scrollModifier = scrollModifier.horizontalScroll(scrollState)
        }
        val contentContext = context.content()
        ComposeContainer(scrollAxes = axes, modifier = context.modifier, fillWidth = axes.contains(Axis.Set.horizontal), fillHeight = axes.contains(Axis.Set.vertical), then = scrollModifier) { modifier ->
            val containerModifier: Modifier
            val refreshing = remember { -> mutableStateOf(false) }
            val refreshAction = EnvironmentValues.shared.refresh
            val refreshState: PullRefreshState?
            if (refreshAction != null) {
                val updatedAction = rememberUpdatedState(refreshAction)
                refreshState = rememberPullRefreshState(refreshing.value, { ->
                    coroutineScope.launch { ->
                        refreshing.value = true
                        updatedAction.value()
                        refreshing.value = false
                    }
                })
                containerModifier = modifier.pullRefresh(refreshState!!)
            } else {
                refreshState = null
                containerModifier = modifier
            }
            Box(modifier = containerModifier) { ->
                content.Compose(context = contentContext)
                if (refreshState != null) {
                    PullRefreshIndicator(refreshing.value, refreshState, Modifier.align(androidx.compose.ui.Alignment.TopCenter))
                }
            }
        }
    }

    companion object {
    }
}

class ScrollViewProxy {
    internal val scrollToID: (Any) -> Unit

    fun scrollTo(id: Any, anchor: UnitPoint? = null) {
        // Warning: anchor is currently ignored
        scrollToID(id)
    }

    internal constructor(scrollToID: (Any) -> Unit) {
        this.scrollToID = scrollToID
    }

    companion object {
    }
}

class ScrollViewReader<Content>: View where Content: View {
    val content: (ScrollViewProxy) -> Content

    constructor(content: (ScrollViewProxy) -> Content) {
        this.content = content
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val scrollToID = rememberSaveable(stateSaver = context.stateSaver as Saver<Preference<(Any) -> Unit>, Any>) { -> mutableStateOf(Preference<(Any) -> Unit>(key = ScrollToIDPreferenceKey::class)) }
        val scrollToIDCollector = PreferenceCollector<(Any) -> Unit>(key = ScrollToIDPreferenceKey::class, state = scrollToID)
        val scrollProxy = ScrollViewProxy(scrollToID = scrollToID.value.reduced)
        PreferenceValues.shared.collectPreferences(arrayOf(scrollToIDCollector)) { -> content(scrollProxy).Compose(context) }
    }

    companion object {
    }
}

internal class ScrollToTopPreferenceKey: PreferenceKey<() -> Unit> {

    companion object: PreferenceKeyCompanion<() -> Unit> {
        override val defaultValue: () -> Unit = { ->  }
        override fun reduce(value: InOut<() -> Unit>, nextValue: () -> () -> Unit) {
            value.value = nextValue()
        }
    }
}

internal class ScrollToIDPreferenceKey: PreferenceKey<(Any) -> Unit> {

    companion object: PreferenceKeyCompanion<(Any) -> Unit> {
        override val defaultValue: (Any) -> Unit = { _ ->  }
        override fun reduce(value: InOut<(Any) -> Unit>, nextValue: () -> (Any) -> Unit) {
            value.value = nextValue()
        }
    }
}

enum class ScrollBounceBehavior: Sendable {
    automatic,
    always,
    basedOnSize;

    companion object {
    }
}

enum class ScrollDismissesKeyboardMode: Sendable {
    automatic,
    immediately,
    interactively,
    never;

    companion object {
    }
}

enum class ScrollIndicatorVisibility {
    automatic,
    visible,
    hidden,
    never;

    companion object {
    }
}

@Suppress("MUST_BE_INITIALIZED")
class ScrollTarget: MutableStruct {
    var rect: CGRect
        get() = field.sref({ this.rect = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    var anchor: UnitPoint? = null
        get() = field.sref({ this.anchor = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(rect: CGRect, anchor: UnitPoint? = null) {
        this.rect = rect
        this.anchor = anchor
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as ScrollTarget
        this.rect = copy.rect
        this.anchor = copy.anchor
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = ScrollTarget(this as MutableStruct)

    companion object {
    }
}

class PinnedScrollableViews: OptionSet<PinnedScrollableViews, Int>, Sendable, MutableStruct {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): PinnedScrollableViews = PinnedScrollableViews(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: PinnedScrollableViews) {
        willmutate()
        try {
            assignfrom(target)
        } finally {
            didmutate()
        }
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as PinnedScrollableViews
        this.rawValue = copy.rawValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = PinnedScrollableViews(this as MutableStruct)

    private fun assignfrom(target: PinnedScrollableViews) {
        this.rawValue = target.rawValue
    }

    companion object {

        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val sectionHeaders = PinnedScrollableViews(rawValue = 1 shl 0)
        @Deprecated("This API is not yet available in Skip. Consider placing it within a #if !SKIP block. You can file an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val sectionFooters = PinnedScrollableViews(rawValue = 1 shl 1)

        fun of(vararg options: PinnedScrollableViews): PinnedScrollableViews {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return PinnedScrollableViews(rawValue = value)
        }
    }
}

