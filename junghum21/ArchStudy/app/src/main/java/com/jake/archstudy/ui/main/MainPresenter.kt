package com.jake.archstudy.ui.main

import com.jake.archstudy.R
import com.jake.archstudy.data.model.Market
import com.jake.archstudy.data.source.UpbitRepository
import com.jake.archstudy.util.ResourceProvider

class MainPresenter(
    override val view: MainContract.View,
    private val repository: UpbitRepository,
    private val resourceProvider: ResourceProvider
) : MainContract.Presenter {

    override fun onCreate() {
        getMarketAll()
    }

    override fun onDestroy() {

    }

    private fun getMarketAll() {
        repository.getMarketAll(
            { response ->
                val markets = response.asSequence()
                    .groupBy { it.market.substringBefore("-") }
                    .map { map ->
                        val title = map.key
                        val markets = map.value.joinToString { it.market }
                        Market(title, markets)
                    }
                view.setViewPager(markets)
            },
            {
                val message = resourceProvider.getString(R.string.fail_network)
                view.showToast(message)
            }
        )
    }


}