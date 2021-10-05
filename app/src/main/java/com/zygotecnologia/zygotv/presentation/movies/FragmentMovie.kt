package com.zygotecnologia.zygotv.presentation.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.zygotecnologia.zygotv.R
import com.zygotecnologia.zygotv.network.TmdbApi
import com.zygotecnologia.zygotv.network.TmdbClient
import com.zygotecnologia.zygotv.presentation.activity.DetailActivity
import com.zygotecnologia.zygotv.presentation.adapter.MovieAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentMovie : Fragment() {

    private val tmdbApi = TmdbClient.getInstance()

    private val showList: RecyclerView by lazy { view?.findViewById(R.id.rv_show_list)!! }
    private lateinit var imgView:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(
            R.layout.fragment_movies,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(Dispatchers.IO) { loadShows() }
        binding()
        clickListener()
    }

    private suspend fun loadShows() {
        val genres =
            tmdbApi
                .fetchGenresMovieAsync(TmdbApi.TMDB_API_KEY, "BR")
                ?.genres
                ?: emptyList()
        val shows =
            tmdbApi
                .fetchMovieAsync(TmdbApi.TMDB_API_KEY, "BR")
                ?.results
                ?.map { show ->
                    show.copy(genres = genres.filter { show.genreIds?.contains(it.id) == true })
                }
                ?: emptyList()


        withContext(Dispatchers.Main) {
            showList.adapter = MovieAdapter(shows)
        }
    }

    fun binding(){
        imgView = view?.findViewById(R.id.img_serie)!!
    }

    fun clickListener(){
        imgView.setOnClickListener{
            handleClick(view)

        }
    }
    private fun handleClick(movie: View?) {
        val intent = startActivity(Intent(context, DetailActivity::class.java))

    }
}