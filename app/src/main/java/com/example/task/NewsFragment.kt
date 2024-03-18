package com.example.task

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.task.DBHelper
import com.example.task.DailyNews
import com.example.task.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var newsBannerImageView: ImageView
    private var imageUri: Uri? = null
    private lateinit var newsTitle: EditText
    private lateinit var newsDesc: EditText
    private lateinit var newsCategory: Spinner
    private lateinit var newsRegion: Spinner
    private lateinit var newsStatus: Spinner
    private lateinit var newsLanguage: EditText
    private lateinit var newsCity: EditText
    private lateinit var newsCountry: EditText
    private lateinit var selectImageButton: ImageButton
    private lateinit var addNewsButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.news_input, container, false)

        dbHelper = DBHelper(requireContext())

        newsTitle = view.findViewById(R.id.newsTitle)
        newsDesc = view.findViewById(R.id.newsDesc)
        newsCategory = view.findViewById(R.id.newsCategory)
        newsRegion = view.findViewById(R.id.newsRegion)
        newsStatus = view.findViewById(R.id.newsStatus)
        newsLanguage = view.findViewById(R.id.newsLanguage)
        newsCity = view.findViewById(R.id.newsCity)
        newsCountry = view.findViewById(R.id.newsCountry)
        newsBannerImageView = view.findViewById(R.id.newsBanner)
        addNewsButton = view.findViewById<Button>(R.id.addNews)
        selectImageButton = view.findViewById<ImageButton>(R.id.selectImageButton)

        // Set click listener for image selection
        selectImageButton.setOnClickListener {
            openGallery()
        }

        addNewsButton.setOnClickListener {
            val title = newsTitle.text.toString()
            val description = newsDesc.text.toString()
            val category = newsCategory.selectedItem.toString()
            val region = newsRegion.selectedItem.toString()
            val status = newsStatus.selectedItem.toString()
            val language = newsLanguage.text.toString()
            val city = newsCity.text.toString()
            val country = newsCountry.text.toString()

            if (title.isEmpty() || description.isEmpty() || category.isEmpty() || region.isEmpty() ||
                status.isEmpty() || language.isEmpty() || city.isEmpty() || country.isEmpty() || imageUri == null
            ) {
                Toast.makeText(requireContext(), "All fields are compulsory", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val currentTime = getCurrentLocalTime()
            val newNewsItem = DailyNews(
                title,
                description,
                imageUri.toString(), // Use the image URI
                category,
                region,
                status,
                language,
                city,
                country,
                currentTime,
                "User1",
                currentTime,
                "User1"
            )

            // Add the news to the database
            Toast.makeText(requireContext(), "News Added Successfully", Toast.LENGTH_SHORT).show()
            dbHelper.addNews(newNewsItem)

            // Optionally, clear the input fields after adding the news
            newsTitle.text.clear()
            newsDesc.text.clear()
            newsCategory.setSelection(0)
            newsRegion.setSelection(0)
            newsStatus.setSelection(0)
            newsLanguage.text.clear()
            newsCity.text.clear()
            newsCountry.text.clear()
            imageUri = null
        }

        return view
    }

    private fun getCurrentLocalTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI
            imageUri = data.data
            // Set the selected image to the image view
            newsBannerImageView.setImageURI(imageUri)
            (requireActivity() as MainActivity).shouldShowViewsOnResume = true
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 100
    }


    fun hideViews() {
        newsBannerImageView.visibility = View.GONE
        newsTitle.visibility = View.GONE
        newsCategory.visibility = View.GONE
        newsRegion.visibility = View.GONE
        newsLanguage.visibility = View.GONE
        newsCity.visibility = View.GONE
        newsCountry.visibility = View.GONE
        addNewsButton.visibility = View.GONE
    }

}