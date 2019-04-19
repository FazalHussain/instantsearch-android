package selection

import selection.SelectionMode.Multiple
import selection.SelectionMode.Single
import kotlin.properties.Delegates


public class SelectionListViewModel<K, V>(
    val selectionMode: SelectionMode = Single
) {

    public val onValuesChange: MutableList<(List<V>) -> Unit> = mutableListOf()
    public val onSelectionsChange: MutableList<(Set<K>) -> Unit> = mutableListOf()
    public val onSelectedChange: MutableList<(Set<K>) -> Unit> = mutableListOf()

    public var values: List<V> by Delegates.observable(listOf()) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onValuesChange.forEach { it(newValue) }
        }
    }
    public var selections by Delegates.observable(setOf<K>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectionsChange.forEach { it(newValue) }
        }
    }

    public fun select(key: K) {
        val selections = when (selectionMode) {
            Single -> if (key in selections) setOf() else setOf(key)
            Multiple -> if (key in selections) selections - key else selections + key
        }

        onSelectedChange.forEach { it(selections) }
    }
}