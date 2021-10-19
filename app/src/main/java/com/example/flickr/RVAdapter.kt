package com.example.flickr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickr.databinding.ItemRowBinding

//import kotlinx.android.synthetic.main.single_item.view.*

class RVAdapter(val activity: MainActivity, private val photos: ArrayList<Image>) : RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val photo = photos[position]

        holder.binding.apply {
           tltle.text = photo.title
           Glide.with(activity)
               .load(photo.link)
               .into(imageView2)
            clItr.setOnClickListener{activity.openImg(photo.link)}
        }

    }


    override fun getItemCount() = photos.size

}
