// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import kotlin.reflect.KClass
import skip.lib.*
import skip.lib.Array
import skip.lib.Set

import skip.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


/// Common corner radius for our overlay presentations.
internal val overlayPresentationCornerRadius = 16.0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SheetPresentation(isPresented: Binding<Boolean>, isFullScreen: Boolean, context: ComposeContext, content: () -> View, onDismiss: (() -> Unit)?) {
    if (HandlePresentationSizeClassChange(isPresented = isPresented)) {
        return
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (isPresented.get() || sheetState.isVisible) {
        val contentView = ComposeBuilder.from(content)
        val topInset = remember { -> mutableStateOf(0.dp) }
        val topCornerSize = if (isFullScreen) CornerSize(0.dp) else CornerSize(overlayPresentationCornerRadius.dp)
        val shape = with(LocalDensity.current) { -> RoundedCornerShapeWithTopOffset(offset = topInset.value.toPx(), topStart = topCornerSize, topEnd = topCornerSize) }
        val coroutineScope = rememberCoroutineScope()
        val onDismissRequest = { ->
            if (isFullScreen) {
                // Veto attempts to dismiss fullscreen modal via swipe or back button by re-showing
                if (isPresented.get()) {
                    coroutineScope.launch { -> sheetState.show() }
                }
            } else {
                isPresented.set(false)
            }
        }
        ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState, containerColor = androidx.compose.ui.graphics.Color.Unspecified, shape = shape, dragHandle = null, windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)) { ->
            val isEdgeToEdge = EnvironmentValues.shared._isEdgeToEdge == true
            val sheetDepth = EnvironmentValues.shared._sheetDepth
            val verticalSizeClass = EnvironmentValues.shared.verticalSizeClass
            var systemBarEdges: Edge.Set = Edge.Set.of(Edge.Set.top, Edge.Set.bottom)

            val detentPreferences = rememberSaveable(stateSaver = context.stateSaver as Saver<Preference<PresentationDetentPreferences>, Any>) { -> mutableStateOf(Preference<PresentationDetentPreferences>(key = PresentationDetentPreferenceKey::class)) }
            val detentPreferencesCollector = PreferenceCollector<PresentationDetentPreferences>(key = PresentationDetentPreferences::class, state = detentPreferences)
            val reducedDetentPreferences = detentPreferences.value.reduced.sref()

            if (!isFullScreen && verticalSizeClass != UserInterfaceSizeClass.compact) {
                systemBarEdges.remove(Edge.Set.top)
                // We have to delay access to WindowInsets until inside the ModalBottomSheet composable to get accurate values
                val topBarHeight = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
                var detentInset = 0.dp.sref()

                var inset = (topBarHeight + (24 * sheetDepth).dp).sref()
                if (!isEdgeToEdge) {
                    inset += 24.dp.sref()
                    systemBarEdges.remove(Edge.Set.bottom)
                }

                // TODO: add custom cases
                // Add inset depending on the presentation detent
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp.sref()
                val detent: PresentationDetent = reducedDetentPreferences.detent
                for (unusedi in 0..0) {
                    when (detent) {
                        is PresentationDetent.MediumCase -> inset += screenHeight / 2
                        is PresentationDetent.HeightCase -> {
                            val h = detent.associated0
                            inset += (screenHeight - h.dp).sref()
                        }
                        is PresentationDetent.FractionCase -> {
                            val f = detent.associated0
                            inset += screenHeight * Float(1 - f)
                        }
                        else -> break
                    }
                }

                topInset.value = inset
                // Push the presentation root content area down an equal amount
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(inset))
            } else if (!isEdgeToEdge) {
                systemBarEdges.remove(Edge.Set.top)
                systemBarEdges.remove(Edge.Set.bottom)
                val inset = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
                topInset.value = inset
                // Push the presentation root content area below the top bar
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(inset))
            }

            Box(modifier = Modifier.weight(1.0f)) { ->
                // Place outside of PresentationRoot recomposes
                val stateSaver = remember { -> ComposeStateSaver() }
                val presentationContext = context.content(stateSaver = stateSaver)
                // Place inside of ModalBottomSheet, which renders content async
                PresentationRoot(context = presentationContext, absoluteSystemBarEdges = systemBarEdges) { context ->
                    EnvironmentValues.shared.setValues({ it ->
                        if (!isFullScreen) {
                            it.set_sheetDepth(sheetDepth + 1)
                        }
                        it.setdismiss(DismissAction(action = { -> isPresented.set(false) }))
                    }, in_ = { ->
                        PreferenceValues.shared.collectPreferences(arrayOf(detentPreferencesCollector)) { -> contentView.Compose(context = context) }
                    })
                }
            }
            if (!isEdgeToEdge) {
                // Move the presentation root content area above the bottom bar
                val inset = max(0.dp, WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() - WindowInsets.ime.asPaddingValues().calculateBottomPadding())
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(inset))
            }
        }
    }
    if (!isPresented.get()) {
        LaunchedEffect(true) { ->
            if (sheetState.targetValue != SheetValue.Hidden) {
                sheetState.hide()
                onDismiss?.invoke()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConfirmationDialogPresentation(title: Text?, isPresented: Binding<Boolean>, context: ComposeContext, actions: View, message: View? = null) {
    if (HandlePresentationSizeClassChange(isPresented = isPresented)) {
        return
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (isPresented.get() || sheetState.isVisible) {
        // Collect buttons and message text
        val actionViews: Array<View>
        val matchtarget_0 = actions as? ComposeBuilder
        if (matchtarget_0 != null) {
            val composeBuilder = matchtarget_0
            actionViews = composeBuilder.collectViews(context = context)
        } else {
            actionViews = arrayOf(actions)
        }
        val buttons = actionViews.compactMap { it ->
            it.strippingModifiers { it -> it as? Button }
        }
        val messageViews: Array<View>
        val matchtarget_1 = message as? ComposeBuilder
        if (matchtarget_1 != null) {
            val composeBuilder = matchtarget_1
            messageViews = composeBuilder.collectViews(context = context)
        } else if (message != null) {
            messageViews = arrayOf(message)
        } else {
            messageViews = arrayOf()
        }
        val messageText = messageViews.compactMap { it ->
            it.strippingModifiers { it -> it as? Text }
        }.first.sref()

        ModalBottomSheet(onDismissRequest = { -> isPresented.set(false) }, sheetState = sheetState, containerColor = androidx.compose.ui.graphics.Color.Transparent, dragHandle = null, windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)) { ->
            // Add padding to always keep the sheet away from the top of the screen. It should tap to dismiss like the background
            val interactionSource = remember { -> MutableInteractionSource() }
            Box(modifier = Modifier.fillMaxWidth().height(128.dp).clickable(interactionSource = interactionSource, indication = null, onClick = { -> isPresented.set(false) }))

            val stateSaver = remember { -> ComposeStateSaver() }
            val scrollState = rememberScrollState()
            val isEdgeToEdge = EnvironmentValues.shared._isEdgeToEdge == true
            val bottomSystemBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            val modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = if (isEdgeToEdge) 0.dp else bottomSystemBarPadding)
                .clip(shape = RoundedCornerShape(topStart = overlayPresentationCornerRadius.dp, topEnd = overlayPresentationCornerRadius.dp))
                .background(Color.overlayBackground.colorImpl())
                .padding(bottom = if (isEdgeToEdge) bottomSystemBarPadding else 0.dp)
                .verticalScroll(scrollState)
            val contentContext = context.content(stateSaver = stateSaver)
            Column(modifier = modifier, horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) { -> ComposeConfirmationDialog(title = title, context = contentContext, isPresented = isPresented, buttons = buttons, message = messageText) }
        }
    }
    if (!isPresented.get()) {
        LaunchedEffect(true) { ->
            if (sheetState.targetValue != SheetValue.Hidden) {
                sheetState.hide()
            }
        }
    }
}

@Composable
internal fun ComposeConfirmationDialog(title: Text?, context: ComposeContext, isPresented: Binding<Boolean>, buttons: Array<Button>, message: Text?) {
    val padding = 16.dp.sref()
    if (title != null) {
        androidx.compose.material3.Text(modifier = Modifier.padding(horizontal = padding, vertical = 8.dp), color = Color.secondary.colorImpl(), text = title.localizedTextString(), style = Font.callout.bold().fontImpl())
    }
    if (message != null) {
        androidx.compose.material3.Text(modifier = Modifier.padding(start = padding, top = 8.dp, end = padding, bottom = padding), color = Color.secondary.colorImpl(), text = message.localizedTextString(), style = Font.callout.fontImpl())
    }
    if (title != null || message != null) {
        androidx.compose.material3.Divider()
    }

    val buttonModifier = Modifier.padding(horizontal = padding, vertical = padding)
    val buttonFont = Font.title3
    val tint = (EnvironmentValues.shared._tint ?: Color.accentColor).colorImpl()
    if (buttons.isEmpty) {
        ConfirmationDialogButton(action = { -> isPresented.set(false) }) { -> androidx.compose.material3.Text(modifier = buttonModifier, color = tint, text = stringResource(android.R.string.ok), style = buttonFont.fontImpl()) }
        return
    }

    var cancelButton: Button? = null
    for (button in buttons.sref()) {
        if (button.role == ButtonRole.cancel) {
            cancelButton = button
            continue
        }
        ConfirmationDialogButton(action = { ->
            isPresented.set(false)
            button.action()
        }) { ->
            val text = button.label.collectViews(context = context).compactMap { it ->
                it.strippingModifiers { it -> it as? Text }
            }.first.sref()
            val color = if (button.role == ButtonRole.destructive) Color.red.colorImpl() else tint
            androidx.compose.material3.Text(modifier = buttonModifier, color = color, text = text?.localizedTextString() ?: "", maxLines = 1, style = buttonFont.fontImpl())
        }
        androidx.compose.material3.Divider()
    }
    if (cancelButton != null) {
        ConfirmationDialogButton(action = { ->
            isPresented.set(false)
            cancelButton.action()
        }) { ->
            val text = cancelButton.label.collectViews(context = context).compactMap { it ->
                it.strippingModifiers { it -> it as? Text }
            }.first.sref()
            androidx.compose.material3.Text(modifier = buttonModifier, color = tint, text = text?.localizedTextString() ?: "", maxLines = 1, style = buttonFont.bold().fontImpl())
        }
    } else {
        ConfirmationDialogButton(action = { -> isPresented.set(false) }) { -> androidx.compose.material3.Text(modifier = buttonModifier, color = tint, text = stringResource(android.R.string.cancel), style = buttonFont.bold().fontImpl()) }
    }
}

@Composable
internal fun ConfirmationDialogButton(action: () -> Unit, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().requiredHeightIn(min = 60.dp).clickable(onClick = action), contentAlignment = androidx.compose.ui.Alignment.Center) { -> content() }
}

/// Handle size class changes (typically due to orientation change) in our various presentations.
///
/// Sheets deform on change, so we re-present.
@Composable
internal fun HandlePresentationSizeClassChange(isPresented: Binding<Boolean>): Boolean {
    val verticalSizeClass = rememberUpdatedState(EnvironmentValues.shared.verticalSizeClass)
    val rememberedVerticalSizeClass = remember { -> mutableStateOf(verticalSizeClass.value) }
    if (verticalSizeClass.value == rememberedVerticalSizeClass.value) {
        return false
    }
    LaunchedEffect(verticalSizeClass.value, rememberedVerticalSizeClass.value) { ->
        if (isPresented.get() && verticalSizeClass.value != rememberedVerticalSizeClass.value) {
            isPresented.set(false)
            isPresented.set(true)
        }
        rememberedVerticalSizeClass.value = verticalSizeClass.value
    }
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlertPresentation(title: Text? = null, titleResource: Int? = null, isPresented: Binding<Boolean>, context: ComposeContext, actions: View, message: View? = null) {
    if (!isPresented.get()) {
        return
    }
    // Collect buttons and message text
    val actionViews: Array<View>
    val matchtarget_2 = actions as? ComposeBuilder
    if (matchtarget_2 != null) {
        val composeBuilder = matchtarget_2
        actionViews = composeBuilder.collectViews(context = context)
    } else {
        actionViews = arrayOf(actions)
    }
    val buttons = actionViews.compactMap { it ->
        it.strippingModifiers { it -> it as? Button }
    }
    val messageViews: Array<View>
    val matchtarget_3 = message as? ComposeBuilder
    if (matchtarget_3 != null) {
        val composeBuilder = matchtarget_3
        messageViews = composeBuilder.collectViews(context = context)
    } else if (message != null) {
        messageViews = arrayOf(message)
    } else {
        messageViews = arrayOf()
    }
    val messageText = messageViews.compactMap { it ->
        it.strippingModifiers { it -> it as? Text }
    }.first.sref()

    BasicAlertDialog(onDismissRequest = { -> isPresented.set(false) }) { ->
        val modifier = Modifier.wrapContentWidth().wrapContentHeight().then(context.modifier)
        Surface(modifier = modifier, shape = MaterialTheme.shapes.large, tonalElevation = AlertDialogDefaults.TonalElevation) { ->
            val contentContext = context.content()
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) { -> ComposeAlert(title = title, titleResource = titleResource, context = contentContext, isPresented = isPresented, buttons = buttons, message = messageText) }
        }
    }
}

@Composable
internal fun ComposeAlert(title: Text?, titleResource: Int? = null, context: ComposeContext, isPresented: Binding<Boolean>, buttons: Array<Button>, message: Text?) {
    val padding = 16.dp.sref()
    if (title != null) {
        androidx.compose.material3.Text(modifier = Modifier.padding(horizontal = padding, vertical = 8.dp), color = Color.primary.colorImpl(), text = title.localizedTextString(), style = Font.title3.bold().fontImpl(), textAlign = TextAlign.Center)
    } else if (titleResource != null) {
        androidx.compose.material3.Text(modifier = Modifier.padding(horizontal = padding, vertical = 8.dp), color = Color.primary.colorImpl(), text = stringResource(titleResource), style = Font.title3.bold().fontImpl(), textAlign = TextAlign.Center)
    }
    if (message != null) {
        androidx.compose.material3.Text(modifier = Modifier.padding(start = padding, end = padding), color = Color.primary.colorImpl(), text = message.localizedTextString(), style = Font.callout.fontImpl(), textAlign = TextAlign.Center)
    }
    androidx.compose.material3.Divider(modifier = Modifier.padding(top = 16.dp))

    val buttonModifier = Modifier.padding(horizontal = padding, vertical = 12.dp)
    val buttonFont = Font.title3
    val tint = (EnvironmentValues.shared._tint ?: Color.accentColor).colorImpl()
    if (buttons.isEmpty) {
        AlertButton(modifier = Modifier.fillMaxWidth(), action = { -> isPresented.set(false) }) { -> androidx.compose.material3.Text(modifier = buttonModifier, color = tint, text = stringResource(android.R.string.ok), style = buttonFont.fontImpl()) }
        return
    }

    val buttonContent: @Composable (Button, Boolean) -> Unit = { button, isCancel ->
        val text = button.label.collectViews(context = context).compactMap { it ->
            it.strippingModifiers { it -> it as? Text }
        }.first.sref()
        val color = if (button.role == ButtonRole.destructive) Color.red.colorImpl() else tint
        val style = if (isCancel) buttonFont.bold().fontImpl() else buttonFont.fontImpl()
        androidx.compose.material3.Text(modifier = buttonModifier, color = color, text = text?.localizedTextString() ?: "", maxLines = 1, style = style)
    }

    val optionButtons = buttons.filter { it -> it.role != ButtonRole.cancel }
    val cancelButton = buttons.first { it -> it.role == ButtonRole.cancel }
    val cancelCount = if (cancelButton == null) 0 else 1
    if (optionButtons.count + cancelCount == 2) {
        // Horizontal layout for two buttons
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) { ->
            val modifier = Modifier.weight(1.0f)
            (cancelButton ?: optionButtons.first)?.let { button ->
                AlertButton(modifier = modifier, action = { ->
                    isPresented.set(false)
                    button.action()
                }) { -> buttonContent(button, button === cancelButton) }
                androidx.compose.material3.VerticalDivider()
            }
            optionButtons.last?.let { button ->
                AlertButton(modifier = modifier, action = { ->
                    isPresented.set(false)
                    button.action()
                }) { -> buttonContent(button, false) }
            }
        }
    } else {
        // Vertical layout
        val modifier = Modifier.fillMaxWidth()
        for (button in optionButtons.sref()) {
            AlertButton(modifier = modifier, action = { ->
                isPresented.set(false)
                button.action()
            }) { -> buttonContent(button, false) }
            if (button !== optionButtons.last || cancelButton != null) {
                androidx.compose.material3.Divider()
            }
        }
        if (cancelButton != null) {
            AlertButton(modifier = modifier, action = { ->
                isPresented.set(false)
                cancelButton.action()
            }) { -> buttonContent(cancelButton, true) }
        }
    }
}

@Composable
internal fun AlertButton(modifier: Modifier, action: () -> Unit, content: @Composable () -> Unit) {
    Box(modifier = modifier.clickable(onClick = action), contentAlignment = androidx.compose.ui.Alignment.Center) { -> content() }
}

enum class PresentationAdaptation: Sendable {
    automatic,
    none,
    popover,
    sheet,
    fullScreenCover;

    companion object {
    }
}

class PresentationBackgroundInteraction: Sendable {
    internal val enabled: Boolean?
    internal val upThrough: PresentationDetent?

    internal constructor(enabled: Boolean? = null, upThrough: PresentationDetent? = null) {
        this.enabled = enabled
        this.upThrough = upThrough
    }

    companion object {

        val automatic = PresentationBackgroundInteraction(enabled = null, upThrough = null)

        val enabled = PresentationBackgroundInteraction(enabled = true, upThrough = null)

        fun enabled(upThrough: PresentationDetent): PresentationBackgroundInteraction = PresentationBackgroundInteraction(enabled = true, upThrough = upThrough)

        val disabled = PresentationBackgroundInteraction(enabled = false, upThrough = null)
    }
}

enum class PresentationContentInteraction: Sendable {
    automatic,
    resizes,
    scrolls;

    companion object {
    }
}

sealed class PresentationDetent: Sendable {
    class MediumCase: PresentationDetent() {
    }
    class LargeCase: PresentationDetent() {
    }
    class FractionCase(val associated0: Double): PresentationDetent() {
    }
    class HeightCase(val associated0: Double): PresentationDetent() {
    }
    class CustomCase(val associated0: KClass<*>): PresentationDetent() {
    }

    class Context {
        val maxDetentValue: Double

        constructor(maxDetentValue: Double) {
            this.maxDetentValue = maxDetentValue
        }

        //        public subscript<T>(dynamicMember keyPath: KeyPath<EnvironmentValues, T>) -> T { get { fatalError() } }

        companion object {
        }
    }

    override fun hashCode(): Int {
        var hasher = Hasher()
        hash(into = InOut<Hasher>({ hasher }, { hasher = it }))
        return hasher.finalize()
    }
    fun hash(into: InOut<Hasher>) {
        val hasher = into
        when (this) {
            is PresentationDetent.MediumCase -> hasher.value.combine(1)
            is PresentationDetent.LargeCase -> hasher.value.combine(2)
            is PresentationDetent.FractionCase -> {
                val fraction = this.associated0
                hasher.value.combine(3)
                hasher.value.combine(fraction)
            }
            is PresentationDetent.HeightCase -> {
                val height = this.associated0
                hasher.value.combine(4)
                hasher.value.combine(height)
            }
            is PresentationDetent.CustomCase -> {
                val type = this.associated0
                hasher.value.combine(String(describing = type))
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PresentationDetent) {
            return false
        }
        val lhs = this
        val rhs = other
        when (lhs) {
            is PresentationDetent.MediumCase -> {
                if (rhs is PresentationDetent.MediumCase) {
                    return true
                } else {
                    return false
                }
            }
            is PresentationDetent.LargeCase -> {
                if (rhs is PresentationDetent.LargeCase) {
                    return true
                } else {
                    return false
                }
            }
            is PresentationDetent.FractionCase -> {
                val fraction1 = lhs.associated0
                if (rhs is PresentationDetent.FractionCase) {
                    val fraction2 = rhs.associated0
                    return fraction1 == fraction2
                } else {
                    return false
                }
            }
            is PresentationDetent.HeightCase -> {
                val height1 = lhs.associated0
                if (rhs is PresentationDetent.HeightCase) {
                    val height2 = rhs.associated0
                    return height1 == height2
                } else {
                    return false
                }
            }
            is PresentationDetent.CustomCase -> {
                val type1 = lhs.associated0
                if (rhs is PresentationDetent.CustomCase) {
                    val type2 = rhs.associated0
                    return type1 == type2
                } else {
                    return false
                }
            }
        }
    }

    companion object {
        val medium: PresentationDetent = MediumCase()
        val large: PresentationDetent = LargeCase()
        fun fraction(associated0: Double): PresentationDetent = FractionCase(associated0)
        fun height(associated0: Double): PresentationDetent = HeightCase(associated0)
        fun custom(associated0: KClass<*>): PresentationDetent = CustomCase(associated0)
    }
}

interface CustomPresentationDetent {
}

//public struct PresentedWindowContent<Data, Content> : View where Data : Decodable, Data : Encodable, Data : Hashable, Content : View {
//
//    public typealias Body = NeverView
//    public var body: Body { fatalError() }
//}

internal class PresentationDetentPreferenceKey: PreferenceKey<PresentationDetentPreferences> {

    companion object: PreferenceKeyCompanion<PresentationDetentPreferences> {
        override val defaultValue = PresentationDetentPreferences()
        override fun reduce(value: InOut<PresentationDetentPreferences>, nextValue: () -> PresentationDetentPreferences) {
            value.value = value.value.reduce(nextValue())
        }
    }
}

internal class PresentationDetentPreferences {
    internal val detent: PresentationDetent

    internal constructor(detent: PresentationDetent? = null) {
        this.detent = detent ?: PresentationDetent.large
    }

    internal fun reduce(next: PresentationDetentPreferences): PresentationDetentPreferences = next

    override fun equals(other: Any?): Boolean {
        if (other !is PresentationDetentPreferences) {
            return false
        }
        val lhs = this
        val rhs = other
        return lhs.detent == rhs.detent
    }
}

internal class PresentationModifierView: ComposeModifierView {
    private val presentation: @Composable (ComposeContext) -> Unit

    internal constructor(view: View, presentation: @Composable (ComposeContext) -> Unit): super(view = view) {
        this.presentation = presentation
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        EnvironmentValues.shared.setValues({ it ->
            // Clear environment state that should not transfer to presentations
            it.set_animation(null)
            it.set_searchableState(null)
        }, in_ = { -> presentation(context.content()) })
        view.Compose(context = context)
    }
}

/// Used to chop off the empty area Compose adds above the content of a bottom sheet modal, and to round the rop corners.
internal class RoundedCornerShapeWithTopOffset: CornerBasedShape {
    private val offset: Float

    internal constructor(offset: Float, topStart: CornerSize, topEnd: CornerSize, bottomEnd: CornerSize = CornerSize(0.dp), bottomStart: CornerSize = CornerSize(0.dp)): super(topStart = topStart, topEnd = topEnd, bottomEnd = bottomEnd, bottomStart = bottomStart) {
        this.offset = offset
    }

    override fun copy(topStart: CornerSize, topEnd: CornerSize, bottomEnd: CornerSize, bottomStart: CornerSize): RoundedCornerShapeWithTopOffset = RoundedCornerShapeWithTopOffset(offset = offset, topStart = topStart, topEnd = topEnd, bottomEnd = bottomEnd, bottomStart = bottomStart)

    override fun createOutline(size: Size, topStart: Float, topEnd: Float, bottomEnd: Float, bottomStart: Float, layoutDirection: androidx.compose.ui.unit.LayoutDirection): Outline {
        val rect = Rect(offset = Offset(x = 0.0f, y = offset), size = size)
        val topLeft = CornerRadius(if (layoutDirection == androidx.compose.ui.unit.LayoutDirection.Ltr) topStart else topEnd)
        val topRight = CornerRadius(if (layoutDirection == androidx.compose.ui.unit.LayoutDirection.Ltr) topStart else topEnd)
        val bottomRight = CornerRadius(if (layoutDirection == androidx.compose.ui.unit.LayoutDirection.Ltr) bottomEnd else bottomStart)
        val bottomLeft = CornerRadius(if (layoutDirection == androidx.compose.ui.unit.LayoutDirection.Ltr) bottomStart else bottomEnd)
        return Outline.Rounded(RoundRect(rect = rect, topLeft = topLeft, topRight = topRight, bottomRight = bottomRight, bottomLeft = bottomLeft))
    }
}
