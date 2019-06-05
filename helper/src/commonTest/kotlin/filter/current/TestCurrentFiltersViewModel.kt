package filter.current

import com.algolia.instantsearch.helper.filter.current.CurrentFiltersViewModel
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestCurrentFiltersViewModel {

    private val green = "green"
    private val red = "red"
    private val color = Attribute("color")
    private val filterRed = Filter.Facet(color, red)
    private val filterGreen = Filter.Facet(color, green)
    private val filters = mapOf<String, Filter>(red to filterRed, green to filterGreen)

    @Test
    fun clearFilterHitsThenClearsIt() {
        val viewModel = CurrentFiltersViewModel(filters)

        viewModel.clearFilter(red)
        viewModel.item shouldEqual mapOf(green to filterGreen)
    }

    @Test
    fun clearFilterMissesThenNoClear() {
        val viewModel = CurrentFiltersViewModel(mapOf(green to filterGreen))

        viewModel.clearFilter(red)
        viewModel.item shouldEqual mapOf(green to filterGreen)
    }

    @Test
    fun clearFilterByFilterClearsIt() {
        val viewModel = CurrentFiltersViewModel(mapOf(green to filterGreen))

        viewModel.clearFilter(filterRed)
        viewModel.item shouldEqual mapOf(green to filterGreen)
    }

    @Test
    fun clearFilterTriggersListeners() {
        val viewModel = CurrentFiltersViewModel(mapOf(green to filterGreen))
        var triggered = false
        viewModel.onTriggered += {
            triggered = true
        }

        viewModel.clearFilter(filterGreen)
        triggered.shouldBeTrue()
    }

    @Test
    fun getFiltersReturnsFilterSet() {
        val viewModel = CurrentFiltersViewModel(mapOf(green to filterGreen))

        viewModel.getFilters() shouldEqual setOf(filterGreen)
    }
}