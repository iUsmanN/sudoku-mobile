// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

enum class UserInterfaceSizeClass {
    compact,
    regular;


    companion object {
        fun fromWindowHeightSizeClass(sizeClass: WindowHeightSizeClass): UserInterfaceSizeClass = if (sizeClass == WindowHeightSizeClass.COMPACT) UserInterfaceSizeClass.compact else UserInterfaceSizeClass.regular

        fun fromWindowWidthSizeClass(sizeClass: WindowWidthSizeClass): UserInterfaceSizeClass = if (sizeClass == WindowWidthSizeClass.COMPACT) UserInterfaceSizeClass.compact else UserInterfaceSizeClass.regular
    }
}
