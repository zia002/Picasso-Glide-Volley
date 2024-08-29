package com.hashcodeinc.learn

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hashcodeinc.learn.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var picassoImg:ImageView
    private lateinit var glideImg:ImageView
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        picassoImg=binding.picassoImg
        glideImg=binding.glideImg
        binding.loadPicasso.setOnClickListener {
            val linkPicasso=binding.picassoLink.text.toString()
            if(linkPicasso.trim()!="" && linkPicasso.isNotEmpty()){
                loadPicasso(linkPicasso)
            }
            else Toast.makeText(this,"Invalid Link",Toast.LENGTH_SHORT).show()
        }
        binding.loadGlide.setOnClickListener {
            Toast.makeText(applicationContext,"Wait! Meme is Loading",Toast.LENGTH_SHORT).show()
            requestMeme()
        }
    }
    private fun loadPicasso(link:String){
        Picasso.get()
            .load(link)
            .placeholder(R.drawable.load)
            .error(R.drawable.failed)
            .into(picassoImg,object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    Toast.makeText(applicationContext,"Successfully Loaded",Toast.LENGTH_SHORT).show()
                }
                override fun onError(e: Exception?) {
                    Toast.makeText(applicationContext,"Failed to Load",Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun loadGlide(link:String){
        Glide.with(applicationContext)
            .load(link)
            .placeholder(R.drawable.load)
            .error(R.drawable.failed)
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    glideImg.setImageDrawable(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    Toast.makeText(applicationContext,"Successfully Loaded",Toast.LENGTH_SHORT).show()
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(applicationContext,"Failed to Load",Toast.LENGTH_SHORT).show()
                    glideImg.setImageDrawable(errorDrawable)
                }
            })
    }
    private fun requestMeme() {
        val link = "https://api.apileague.com/retrieve-random-meme?keywords=rocket"
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, link, null,
            { response ->
                val data = response.getString("url")
                loadGlide(data)
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["x-api-key"] = "381fb7f0533f470d90a292b72ea8654a"
                return headers
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}
