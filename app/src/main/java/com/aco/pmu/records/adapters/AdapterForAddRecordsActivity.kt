package com.aco.pmu.records.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.android.synthetic.main.item_image.view.*
import java.util.*
import kotlin.collections.ArrayList

class AdapterForAddRecordsActivity(private val context: Context) : RecyclerView.Adapter<AdapterForAddRecordsActivity.ImageViewHolder>() {

    private val images: ArrayList<Image>
    private val inflater: LayoutInflater
    private var listener: OnItemClickListener? = null
    private var listener2: OnItemLongClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(inflater.inflate(R.layout.item_image, parent, false))
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

    fun removeImage(image: Image) {
        images.remove(image)
    }

    fun getImagePosition(position: Int): Int {
        return images.indices.elementAt(position)
    }

    fun getImageAt(position: Int): Image {
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
        var imageView: ImageView = itemView.image_thumbnail

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(images, this)
                }
            }

            itemView.setOnLongClickListener {
                listener2?.onItemLongClick(this)
                false
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(images: ArrayList<Image>, viewHolder: RecyclerView.ViewHolder)
    }
    
    interface OnItemLongClickListener {
        fun onItemLongClick(viewHolder: RecyclerView.ViewHolder)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setOnItemLongClickListener(listener2: OnItemLongClickListener) {
        this.listener2 = listener2
    }
}
