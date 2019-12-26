package com.song2.myapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.song2.myapplication.R
import com.song2.myapplication.adapter.MovieAdapter
import com.song2.myapplication.data.GetMovieDataResponse
import com.song2.myapplication.data.MovieData
import com.song2.myapplication.data.MovieRepository
import com.song2.myapplication.util.config
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val movieRepository: MovieRepository = MovieRepository()

    private val movieAdapter by lazy { MovieAdapter() }
    private var dataList = arrayListOf<MovieData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_main_act_search_btn.setOnClickListener {
            setMovieRecyclerView(et_main_act_search.text.toString())
        }
    }

    private fun setMovieRecyclerView(keyword: String) {

        movieAdapter.apply {
            data = dataList
        }

        rv_main_act_movie_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieAdapter
        }

        movieRepository.getMovieData(config.clientId, config.secret, keyword, 30)
            .enqueue(object : Callback<GetMovieDataResponse> {
                override fun onFailure(call: Call<GetMovieDataResponse>, t: Throwable) {
                    Log.e("실패", t.toString())
                }

                override fun onResponse(
                    call: Call<GetMovieDataResponse>,
                    response: Response<GetMovieDataResponse>
                ) {
                    if (response.isSuccessful) {

                        dataList = response.body()!!.items

                        rv_main_act_movie_list.apply {
                            movieAdapter.notifyDataSetChanged()
                        }
                        Log.e("성공", dataList.toString())
                    }
                }
            })
    }
}