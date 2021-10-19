package com.example.flickr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {
    var images = ArrayList<Image>()
    lateinit var recycle: RecyclerView
    lateinit var search: EditText
    lateinit var imgv:ImageView
    lateinit var btnsearch: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycle = findViewById(R.id.rv)
        search = findViewById(R.id.edsearch)
        btnsearch = findViewById(R.id.button)
        imgv = findViewById(R.id.imageView)
        recycle.adapter = RVAdapter(this, images)
        recycle.layoutManager = LinearLayoutManager(this)
        btnsearch.setOnClickListener {

            if (search.text.isNotEmpty()) {

                RequestAPI()


            } else {
                Toast.makeText(applicationContext, "Search is empty", Toast.LENGTH_SHORT).show()
            }
        }

        imgv.setOnClickListener { closeImg() }

    }

    private fun RequestAPI() {
        CoroutineScope(IO).launch {
            val data = async { getPhotos() }.await()
            if (data.isNotEmpty()) {
                showPhotos(data)
            } else {
                //Toast.makeText(this@MainActivity, "No Images Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getPhotos(): String {
        var response = ""
        try {
            response =
                URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&per_page=10&api_key=1a8e792c9028b8a8af023ae2289360ed&tags=${search.text}&format=json&nojsoncallback=1")
                    .readText(Charsets.UTF_8)
        } catch (e: Exception) {
            println("ISSUE: $e")
        }
        return response
    }

    suspend fun showPhotos(data: String) {
        withContext(Main){
            Log.d("RESULT-Wrong", "welcome")

            val jsonObj = JSONObject(data)
            val photos = jsonObj.getJSONObject("photos").getJSONArray("photo")
            for(i in 0 until photos.length()){
                val title = photos.getJSONObject(i).getString("title")
                val farmID = photos.getJSONObject(i).getString("farm")
                val serverID = photos.getJSONObject(i).getString("server")
                val id = photos.getJSONObject(i).getString("id")
                val secret = photos.getJSONObject(i).getString("secret")
                val photoLink = "https://farm$farmID.staticflickr.com/$serverID/${id}_$secret.jpg"
                images.add(Image(title, photoLink))
            }
            recycle.adapter?.notifyDataSetChanged()
    }
}
    fun openImg(link: String){
        Glide.with(this).load(link).into(imgv)
        imgv.isVisible = true
        recycle.isVisible = false
        btnsearch.isVisible = false
        search.isVisible = false
    }

    private fun closeImg(){
        imgv.isVisible = false
        recycle.isVisible = true
        btnsearch.isVisible = true
        search.isVisible = true
    }
}