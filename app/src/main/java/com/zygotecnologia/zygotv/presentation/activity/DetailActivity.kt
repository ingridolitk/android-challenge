package com.zygotecnologia.zygotv.presentation.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.zygotecnologia.zygotv.R
import com.zygotecnologia.zygotv.presentation.adapter.DescAdapter
import kotlinx.android.synthetic.main.activity_detail2.*
import kotlinx.android.synthetic.main.item_show.*
import kotlinx.android.synthetic.main.show.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var nameSerie: TextView
        lateinit var title: TextView
        lateinit var plot: TextView
        lateinit var genre: TextView
        lateinit var imgMovie: ImageView

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail2)

//        imgMovie = img_show
//
//        val value = intent.getStringExtra("actor")
//        val titleDesc = intent.getStringExtra("title")
//        val plotDesc = intent.getStringExtra("plot")
//        val genreDesc = intent.getStringExtra("genre")
//
//        nameSerie.text = value
//        title.text = titleDesc
//        plot.text = plotDesc
//        genre.text = genreDesc
//
//        val intent = intent
//        val url = intent.getStringExtra("imgMovie")
//        Picasso.get().load(url).into(imgMovie)
    }
}