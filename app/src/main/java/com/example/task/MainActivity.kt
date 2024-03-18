package com.example.task

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener {
    private lateinit var dbHelper: DBHelper
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsList: MutableList<DailyNews>
    private lateinit var adapter: NewsAdapter
    private lateinit var addNewsButton: FloatingActionButton
    private lateinit var fetchNewsButton: Button
    private lateinit var refreshNewsButton: FloatingActionButton

    private var isNewsFragmentOpen: Boolean = false
    var shouldShowViewsOnResume: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        addNewsButton.setOnClickListener {
            replaceFragment(NewsFragment())
        }
        fetchNewsButton.setOnClickListener {
            val retrievedNewsItems = dbHelper.getAllNews()
            newsList.clear()
            newsList.addAll(retrievedNewsItems.sortedByDescending { it.createdOn })
            adapter.notifyDataSetChanged()

        }
        refreshNewsButton.setOnClickListener {
            newsList.clear()
            val retrievedNewsItems = dbHelper.getAllNews().filter { it.status == "Published" }
            newsList.addAll(retrievedNewsItems.sortedByDescending { it.createdOn })
            adapter.notifyDataSetChanged()
        }
    }

    private fun initializeViews() {
        newsRecyclerView = findViewById(R.id.newsRecyclerView)
        dbHelper = DBHelper(this)
        newsList = mutableListOf()
        adapter = NewsAdapter(newsList, this)

        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.adapter = adapter

        // Fetch news items from the database
        val retrievedNewsItems = dbHelper.getAllNews().filter { it.status == "Published" }

        // Sort the news items based on publish date (assuming publishDate is a property in DailyNews)
        newsList.addAll(retrievedNewsItems.sortedByDescending { it.createdOn })

        // Notify adapter about changes
        adapter.notifyDataSetChanged()

        addNewsButton = findViewById(R.id.addNews)
        fetchNewsButton = findViewById(R.id.fetchNews)
        refreshNewsButton = findViewById(R.id.refreshNews)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        hideViews()

    }

    fun hideViews() {

        newsRecyclerView.visibility = View.GONE
        addNewsButton.visibility = View.GONE
        fetchNewsButton.visibility = View.GONE
        refreshNewsButton.visibility = View.GONE

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showViews() {

        newsRecyclerView.visibility = View.VISIBLE
        addNewsButton.visibility = View.VISIBLE
        fetchNewsButton.visibility = View.VISIBLE
        refreshNewsButton.visibility = View.VISIBLE
        newsList.clear()
        val retrievedNewsItems = dbHelper.getAllNews().filter { it.status == "Published" }
        newsList.addAll(retrievedNewsItems.sortedByDescending { it.updatedOn })
        adapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        Log.e("ninad", "onresume")
        if(!shouldShowViewsOnResume)
            showViews()
        shouldShowViewsOnResume=true
    }

    override fun onPause() {
        super.onPause()
        hideViews()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            showViews()
        } else {
            super.onBackPressed()
        }
    }


    override fun onItemClick(newsItem: DailyNews) {
        // Create a bundle to pass data to the fragment
        val bundle = Bundle()
        bundle.putInt("news_id", newsItem.newsId)

        // Create the fragment instance
        val newsDetailFragment = NewsDetailFragment()
        newsDetailFragment.arguments = bundle

        // Start fragment transaction to replace the current fragment with NewsDetailFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, newsDetailFragment)
            .addToBackStack(null) // Add to back stack to allow back navigation
            .commit()
        hideViews()
    }

    override fun deleteClick(newsItem: DailyNews, position: Int) {
        val dbHelper = DBHelper(applicationContext)
        dbHelper.deleteNews(newsItem.newsId)
        newsList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }
}