package com.example.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide


class NewsDetailFragment : Fragment() {
    private lateinit var dbHelper: DBHelper
    private lateinit var newsTitleTextView: TextView
    private lateinit var newsDescriptionTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var regionTextView: TextView
    private lateinit var languageTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var createdOnTextView: TextView
    private lateinit var createdByTextView: TextView
    private lateinit var updatedOnTextView: TextView
    private lateinit var updatedByTextView: TextView
    private lateinit var updatedBy: TextView
    private lateinit var updatedOn: TextView
    private lateinit var createdBy: TextView
    private lateinit var createdOn: TextView
    private lateinit var category: TextView
    private lateinit var bannerImage: ImageView
    private lateinit var updateNews: Button
    private var newsId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_news, container, false)
        dbHelper = DBHelper(requireContext())
        initializeViews(view)
        setupListeners()
        return view
    }

    private fun initializeViews(view: View) {
        newsTitleTextView = view.findViewById(R.id.newsTitleTextView)
        newsDescriptionTextView = view.findViewById(R.id.newsDescriptionTextView)
        categoryTextView = view.findViewById(R.id.categoryTextView)
        regionTextView = view.findViewById(R.id.regionTextView)
        languageTextView = view.findViewById(R.id.languageTextView)
        cityTextView = view.findViewById(R.id.cityTextView)
        countryTextView = view.findViewById(R.id.countryTextView)
        createdOnTextView = view.findViewById(R.id.createdOnTextView)
        createdByTextView = view.findViewById(R.id.createdByTextView)
        updatedOnTextView = view.findViewById(R.id.updatedOnTextView)
        updatedBy = view.findViewById(R.id.updated)
        createdOn = view.findViewById(R.id.createdOn)
        createdBy = view.findViewById(R.id.created)
        updatedOn = view.findViewById(R.id.updatedOn)
        category = view.findViewById(R.id.category)
        updatedByTextView = view.findViewById(R.id.updatedByTextView)
        bannerImage = view.findViewById(R.id.bannerImage)
        updateNews = view.findViewById(R.id.updateNews)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the news ID from arguments bundle
        newsId = arguments?.getInt("news_id") ?: -1

        // Fetch news details from the database
        val newsItem = dbHelper.getNewsById(newsId)

        // Populate UI with news details
        populateUI(newsItem)
    }

    private fun setupListeners() {
        updateNews.setOnClickListener {
            val fragment = NewsUpdateFragment()
            replaceFragment(fragment)
        }
    }

    private fun replaceFragment(someFragment: Fragment?) {
        someFragment?.let { fragment ->
            newsTitleTextView.visibility = View.GONE
            newsDescriptionTextView.visibility = View.GONE
            categoryTextView.visibility = View.GONE
            regionTextView.visibility = View.GONE
            languageTextView.visibility = View.GONE
            cityTextView.visibility = View.GONE
            countryTextView.visibility = View.GONE
            createdOnTextView.visibility = View.GONE
            createdByTextView.visibility = View.GONE
            updatedOnTextView.visibility = View.GONE
            updatedByTextView.visibility = View.GONE
            updatedOn.visibility = View.GONE
            createdBy.visibility = View.GONE
            updatedBy.visibility = View.GONE
            createdOn.visibility = View.GONE
            category.visibility = View.GONE
            categoryTextView.visibility = View.GONE
            updateNews.visibility = View.GONE
            bannerImage.visibility = View.GONE

            val bundle = Bundle()
            bundle.putInt("news_id", newsId)
            fragment.arguments = bundle

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.detail_fragment_container, fragment)
            transaction.commit()
        }
    }


    private fun populateUI(newsItem: DailyNews?) {
        newsItem?.let {
            newsTitleTextView.text = it.title
            newsDescriptionTextView.text = it.description
            categoryTextView.text = it.category
            regionTextView.text = it.region
            languageTextView.text = it.language
            cityTextView.text = it.city
            countryTextView.text = it.country
            createdOnTextView.text = it.createdOn
            createdByTextView.text = it.createdBy
            updatedOnTextView.text = it.updatedOn
            updatedByTextView.text = it.updatedBy
            Glide.with(requireContext())
                .load(newsItem.bannerImage)
                .into(bannerImage)
        }
    }


}
