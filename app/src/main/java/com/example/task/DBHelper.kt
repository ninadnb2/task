package com.example.task

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DailyNewsDB"
        private const val TABLE_NAME = "DailyNews"

        private const val NEWS_ID = "News_Id"
        private const val NEWS_TITLE = "News_Title"
        private const val NEWS_DESCRIPTION = "News_Description"
        private const val NEWS_BANNER_IMAGE = "News_Banner_Image"
        private const val CATEGORY = "Category"
        private const val REGION = "Region"
        private const val STATUS = "Status"
        private const val LANGUAGE = "Language"
        private const val CITY = "City"
        private const val COUNTRY = "Country"
        private const val CREATED_ON = "CreatedOn"
        private const val CREATED_BY = "CreatedBy"
        private const val UPDATED_ON = "UpdatedOn"
        private const val UPDATED_BY = "UpdatedBy"
        private const val IMAGE_URI = "Image_Uri" // Added image URI column
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ($NEWS_ID INTEGER PRIMARY KEY, "
                + "$NEWS_TITLE TEXT, "
                + "$NEWS_DESCRIPTION TEXT, "
                + "$NEWS_BANNER_IMAGE TEXT, "
                + "$CATEGORY TEXT, "
                + "$REGION TEXT, "
                + "$STATUS TEXT, "
                + "$LANGUAGE TEXT, "
                + "$CITY TEXT, "
                + "$COUNTRY TEXT, "
                + "$CREATED_ON TEXT, "
                + "$CREATED_BY TEXT, "
                + "$UPDATED_ON TEXT, "
                + "$UPDATED_BY TEXT, "
                + "$IMAGE_URI TEXT)") // Added image URI column

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addNews(news: DailyNews) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NEWS_TITLE, news.title)
        values.put(NEWS_DESCRIPTION, news.description)
        values.put(NEWS_BANNER_IMAGE, news.bannerImage)
        values.put(CATEGORY, news.category)
        values.put(REGION, news.region)
        values.put(STATUS, news.status)
        values.put(LANGUAGE, news.language)
        values.put(CITY, news.city)
        values.put(COUNTRY, news.country)
        values.put(CREATED_ON, news.createdOn)
        values.put(CREATED_BY, news.createdBy)
        values.put(UPDATED_ON, news.updatedOn)
        values.put(UPDATED_BY, news.updatedBy)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNews(): List<DailyNews> {
        val newsList = mutableListOf<DailyNews>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val news = DailyNews(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getInt(0) // News_Id
                    )
                    newsList.add(news)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            // Handle exception
        } finally {
            cursor?.close()
            db.close()
        }
        return newsList
    }

    fun getNewsById(newsId: Int): DailyNews? {
        var news: DailyNews? = null
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $NEWS_ID=?", arrayOf(newsId.toString()))

        try {
            if (cursor.moveToFirst()) {
                news = DailyNews(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getInt(0))
            }
        } catch (e: Exception) {
            // Handle exception
        } finally {
            cursor?.close()
            db.close()
        }
        return news
    }

    fun updateNews(originalNews: DailyNews, updatedNews: DailyNews) {
        val db = this.writableDatabase
        val values = ContentValues()

        // Add updated values to ContentValues
        values.put(NEWS_TITLE, updatedNews.title)
        values.put(NEWS_DESCRIPTION, updatedNews.description)
        values.put(NEWS_BANNER_IMAGE, updatedNews.bannerImage)
        values.put(CATEGORY, updatedNews.category)
        values.put(REGION, updatedNews.region)
        values.put(STATUS, updatedNews.status)
        values.put(LANGUAGE, updatedNews.language)
        values.put(CITY, updatedNews.city)
        values.put(COUNTRY, updatedNews.country)
        values.put(CREATED_ON, updatedNews.createdOn)
        values.put(CREATED_BY, updatedNews.createdBy)
        values.put(UPDATED_ON, updatedNews.updatedOn)
        values.put(UPDATED_BY, updatedNews.updatedBy)

        // Update the database
        db.update(TABLE_NAME, values, "$NEWS_ID=?", arrayOf(originalNews.newsId.toString()))
        db.close()
    }

    fun deleteNews(newsId: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$NEWS_ID=?", arrayOf(newsId.toString()))
    }
}