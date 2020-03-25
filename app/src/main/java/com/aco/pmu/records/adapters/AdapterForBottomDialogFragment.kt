package com.aco.pmu.records.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.android.synthetic.main.item_image_large.view.*
import java.util.*

class AdapterForBottomDialogFragment(private val context: Context) : RecyclerView.Adapter<AdapterForBottomDialogFragment.ImageViewHolder>() {

    private val images: ArrayList<Image>
    private val inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(inflater.inflate(R.layout.item_image_large, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        Glide.with(context)
            .load(image.path)
            .apply(RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun getChildAtPosition(position: Int): Image {
        return images[position]
    }

    fun setData(images: List<Image>?) {
        this.images.clear()
        if (images != null) {
            this.images.addAll(images)
        }
        notifyDataSetChanged()
    }

    init {
        inflater = LayoutInflater.from(context)
        images = ArrayList()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.image_thumbnail_large
    }
}
