// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Spacer: View {
    private val minLength: Double?

    constructor(minLength: Double? = null) {
        this.minLength = minLength
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        // We haven't found a way that works to get a minimum size and expanding behavior on a spacer, so use two spacers: the
        // first to enforce the minimum, and the second to expand. Note that this will cause some modifiers to behave incorrectly

        val axis = EnvironmentValues.shared._layoutAxis
        if ((minLength != null) && (minLength > 0.0)) {
            val minModifier: Modifier
            when (axis) {
                Axis.horizontal -> minModifier = Modifier.width(minLength.dp)
                Axis.vertical -> minModifier = Modifier.height(minLength.dp)
                null -> minModifier = Modifier
            }
            androidx.compose.foundation.layout.Spacer(modifier = minModifier.then(context.modifier))
        }

        val fillModifier: Modifier
        when (axis) {
            Axis.horizontal -> fillModifier = EnvironmentValues.shared._fillWidth?.invoke(true) ?: Modifier
            Axis.vertical -> fillModifier = EnvironmentValues.shared._fillHeight?.invoke(true) ?: Modifier
            null -> fillModifier = Modifier
        }
        androidx.compose.foundation.layout.Spacer(modifier = fillModifier.then(context.modifier))
    }

    companion object {
    }
}
