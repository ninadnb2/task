package com.example.task

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsUpdateFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var newsBanner: ImageView
    private var newsId: Int = -1
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.update_news, container, false)

        newsId = arguments?.getInt("news_id") ?: -1
        dbHelper = DBHelper(requireContext())


        val newsTitle = view.findViewById<EditText>(R.id.newsTitle)
        val newsDesc = view.findViewById<EditText>(R.id.newsDesc)
        newsBanner = view.findViewById(R.id.newsBanner)
        val newsCategory = view.findViewById<Spinner>(R.id.newsCategory)
        val newsRegion = view.findViewById<Spinner>(R.id.newsRegion)
        val newsStatus = view.findViewById<Spinner>(R.id.newsStatus)
        val newsLanguage = view.findViewById<EditText>(R.id.newsLanguage)
        val newsCity = view.findViewById<EditText>(R.id.newsCity)
        val newsCountry = view.findViewById<EditText>(R.id.newsCountry)
        val updateNews = view.findViewById<Button>(R.id.updateNews)
        val selectImageButton = view.findViewById<ImageButton>(R.id.selectImageButton)

        selectImageButton.setOnClickListener {
            openGallery()
        }


        val newsItem = dbHelper.getNewsById(newsId)

        newsItem?.let {
            newsTitle.setText(it.title)
            newsDesc.setText(it.description)

            // Find the position of the category in the spinner's adapter and set it as the selected item
            val categoryPosition =
                (newsCategory.adapter as ArrayAdapter<String>).getPosition(it.category)
            newsCategory.setSelection(categoryPosition)

            // Similarly, find and set the selected items for region and status
            val regionPosition = (newsRegion.adapter as ArrayAdapter<String>).getPosition(it.region)
            newsRegion.setSelection(regionPosition)

            val statusPosition = (newsStatus.adapter as ArrayAdapter<String>).getPosition(it.status)
            newsStatus.setSelection(statusPosition)

            newsLanguage.setText(it.language)
            newsCity.setText(it.city)
            newsCountry.setText(it.country)

            Glide.with(requireContext())
                .load(newsItem.bannerImage)
                .into(newsBanner)
        }

        updateNews.setOnClickListener {

            val updatedTitle = newsTitle.text.toString()
            val updatedDescription = newsDesc.text.toString()
            val updatedCategory = newsCategory.selectedItem.toString()
            val updatedRegion = newsRegion.selectedItem.toString()
            val updatedStatus = newsStatus.selectedItem.toString()
            val updatedLanguage = newsLanguage.text.toString()
            val updatedCity = newsCity.text.toString()
            val updatedCountry = newsCountry.text.toString()

            if (updatedTitle.isEmpty() || updatedDescription.isEmpty() || updatedCategory.isEmpty() ||
                updatedRegion.isEmpty() || updatedStatus.isEmpty() || updatedLanguage.isEmpty() ||
                updatedCity.isEmpty() || updatedCountry.isEmpty()
            ) {
                Toast.makeText(requireContext(), "All fields are compulsory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentTime = getCurrentLocalTime()
            val createdOn = dbHelper.getNewsById(newsId)?.createdOn
            val updatedNewsItem = DailyNews(
                updatedTitle,
                updatedDescription,
                imageUri.toString(),
                updatedCategory,
                updatedRegion,
                updatedStatus,
                updatedLanguage,
                updatedCity,
                updatedCountry,
                createdOn!!,
                "User1",
                currentTime,
                "User1"
            )

            // UPDATE the news to the database
            Toast.makeText(requireContext(), "News Updated Successfully", Toast.LENGTH_SHORT).show()
            dbHelper.updateNews(newsItem!!,updatedNewsItem)

            // Optionally, clear the input fields after adding the news
            newsTitle.text.clear()
            newsDesc.text.clear()
            newsCategory.setSelection(0)
            newsRegion.setSelection(0)
            newsStatus.setSelection(0)
            newsLanguage.text.clear()
            newsCity.text.clear()
            newsCountry.text.clear()
            imageUri=null
            (requireActivity() as MainActivity).shouldShowViewsOnResume = true
        }


        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,IMAGE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NewsUpdateFragment.IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI
            imageUri = data.data
            // Set the selected image to the image view
            newsBanner.setImageURI(imageUri)
            (requireActivity() as MainActivity).shouldShowViewsOnResume = true
        }
    }

    private fun getCurrentLocalTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 100
    }
}