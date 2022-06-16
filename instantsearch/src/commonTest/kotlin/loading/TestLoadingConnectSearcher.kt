package loading

import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.loading.connectSearcher
import com.algolia.search.model.IndexName
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import mockClient
import searcher.MockSearcher
import searcher.TestSearcherSingle
import shouldBeFalse
import shouldEqual

class TestLoadingConnectSearcher {

    private val client = mockClient()
    private val indexName = IndexName("A")

    @Test
    fun connectShouldSetItem() {
        val searcher = TestSearcherSingle(client, indexName)
        val viewModel = LoadingViewModel()
        val expected = true
        val debouncer = Debouncer(200)
        val connection = viewModel.connectSearcher(searcher, debouncer)

        searcher.isLoading.value = expected
        viewModel.isLoading.value.shouldBeFalse()
        connection.connect()
        viewModel.isLoading.value shouldEqual expected
    }

    @Test
    fun onLoadingChangedShouldSetItem() = runTest {
        val debouncer = Debouncer(100)
        val searcher = TestSearcherSingle(client, indexName)
        val viewModel = LoadingViewModel()
        val expected = true
        val connection = viewModel.connectSearcher(searcher, debouncer)

        viewModel.isLoading.value.shouldBeFalse()
        connection.connect()
        searcher.isLoading.value = true
        debouncer.job!!.join()
        viewModel.isLoading.value shouldEqual expected
    }

    @Test
    fun onEventSentShouldCallSearch() = runTest {
        val searcher = MockSearcher()
        val viewModel = LoadingViewModel()
        val debouncer = Debouncer(200)
        val connection = viewModel.connectSearcher(searcher, debouncer)

        searcher.searchCount shouldEqual 0
        connection.connect()
        viewModel.eventReload.send(Unit)
        searcher.job?.join()
        searcher.searchCount shouldEqual 1
    }
}
