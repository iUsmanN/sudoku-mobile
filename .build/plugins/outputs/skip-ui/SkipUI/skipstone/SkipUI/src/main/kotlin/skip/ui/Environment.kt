// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

// Model Environment as a class rather than struct to mutate by reference and avoid copy overhead
class Environment<Value> where Value: Any {
    constructor() {
    }

    constructor(wrappedValue: Value) {
        this.wrappedValue = wrappedValue
    }

    var wrappedValue: Value
        get() = wrappedValuestorage.sref({ this.wrappedValue = it })
        set(newValue) {
            wrappedValuestorage = newValue.sref()
        }
    private lateinit var wrappedValuestorage: Value

    val projectedValue: Binding<Value>
        get() {
            return Binding(get = { -> this.wrappedValue }, set = { it -> this.wrappedValue = it })
        }

    companion object {
    }
}
