package com.zygotecnologia.zygotv.presentation.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zygotecnologia.zygotv.R
import com.zygotecnologia.zygotv.data.network.TmdbApi
import com.zygotecnologia.zygotv.data.network.TmdbClient
import com.zygotecnologia.zygotv.data.repository.MoviesRepositoryImpl
import com.zygotecnologia.zygotv.domain.model.Genre
import com.zygotecnologia.zygotv.domain.model.Show
import com.zygotecnologia.zygotv.presentation.activity.DetailActivity
import com.zygotecnologia.zygotv.presentation.adapter.MovieAdapter
import com.zygotecnologia.zygotv.presentation.adapter.ShowAdapter
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentMovie : Fragment() {

    private val tmdbApi = TmdbClient.getInstance()

    private val showList: RecyclerView by lazy { view?.findViewById(R.id.rv_show_list)!! }
    private val viewModel: MovieViewModel =
        MovieViewModel.ViewModelFactory(MoviesRepositoryImpl()).create(MovieViewModel::class.java)

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
        setObservers()
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

            val genre2s: MutableList<Genre> = ArrayList()
            var popularity = 0.0
            lateinit var moviePopularity: Show

            genres.forEach {
                val genre = Genre(it.id, it.name)

                val moviesGenre: MutableList<Show> = ArrayList()
                shows.forEach { movie ->
                    if (movie.genreIds?.contains(it.id) == true) {
                        moviesGenre.add(movie)
                        if (popularity < movie.popularity) {

                            popularity = movie.popularity
                            moviePopularity = movie
                        }
                    }
                }
                if (moviesGenre.size > 0) {
                    genre.movies = moviesGenre
                    genre2s.add(genre)
                }

                if (moviePopularity.backdropPath !== "") {

                    val url = TmdbApi.TMDB_BASE_IMAGE_URL + moviePopularity.backdropPath

                    imgPoster?.let {
                        Glide.with(imgPoster.context).load(
                            url
                        ).into(it)
                    }
                }
                txtOriginalName.text = moviePopularity.title
                imgPoster.setOnClickListener {
                    handleClick(moviePopularity)
                }
                showList.adapter = MovieAdapter(genre2s, clickListener = {
                    handleClick(it)
                })
            }
        }
    }

    private fun setObservers() {

        viewModel.movieList.observe(viewLifecycleOwner, Observer { movieListResponse ->
            showList.adapter = movieListResponse?.let { list ->
                ShowAdapter(list as MutableList<Show>, clickListener = {
                    handleClick(it)
                })
            }

            viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            })
        })
    }

    private fun handleClick(movie: Show) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("title", movie.title)
        intent.putExtra("poster", movie.posterPath)
        intent.putExtra("name", movie.name)
        intent.putExtra("backdropPath", movie.backdropPath)
        intent.putExtra("genre", movie.overview)
        startActivity(intent)
    }
}