package com.egiwon.architecturestudy.tabs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.egiwon.architecturestudy.R
import com.egiwon.architecturestudy.base.BaseFragment
import com.egiwon.architecturestudy.data.Content
import com.egiwon.architecturestudy.data.source.NaverDataRepository
import com.egiwon.architecturestudy.data.source.remote.NaverRemoteDataSource
import kotlinx.android.synthetic.main.fg_contents.*

class ContentsFragment : BaseFragment(
    R.layout.fg_contents
) {

    private val type: String by lazy { arguments?.getString(ARG_TYPE, "") ?: "" }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(rv_contents) {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )

            adapter = ContentsAdapter(type)
            setHasFixedSize(true)

        }

        btn_search.setOnClickListener {
            context?.let {
                requestSearch()
            }
        }
    }

    private fun requestSearch() {
        NaverDataRepository.getContents(
            type = type,
            query = et_search.text.toString(),
            callback = object : NaverRemoteDataSource.Callback {
                override fun onSuccess(list: List<Content.Item>) {
                    (rv_contents.adapter as? ContentsAdapter)?.setList(list)
                    progress_circular.visibility = View.GONE
                }

                override fun onFailure(throwable: Throwable) {
                    showToast(getString(R.string.callback_fail))
                    progress_circular.visibility = View.GONE
                }
            })

        progress_circular.visibility = View.VISIBLE
    }

    companion object {
        private const val ARG_TYPE = "type"

        fun newInstance(type: String) =
            ContentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                }
            }

    }
}