package com.example.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

open class NewsAdapter(private val newsList: List<DailyNews>,private val itemClickListener: OnItemClickListener?) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.newsTitleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.newsDescriptionTextView)
        val publishDateTextView: TextView = itemView.findViewById(R.id.publishDate)
        val deleteNewsButton:FloatingActionButton = itemView.findViewById(R.id.deleteNews)
        val newsImage:ImageView = itemView.findViewById(R.id.newsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList[position]
        holder.titleTextView.text = news.title
        holder.descriptionTextView.text = news.description
        holder.publishDateTextView.text = news.createdOn
        Glide.with(holder.itemView.context)
            .load(news.bannerImage)
            .into(holder.newsImage)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(news)
        }
        holder.deleteNewsButton.setOnClickListener {
            itemClickListener?.deleteClick(news,holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    interface OnItemClickListener {
        fun onItemClick(newsItem: DailyNews)

        fun deleteClick(newsItem: DailyNews,position: Int)
    }
}
