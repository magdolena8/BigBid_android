package com.begdev.bigbid.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.begdev.bigbid.data.repository.ItemsRepo


class DBHandlerLocal
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + LIKED_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, "
                + TITLE_COL + " TEXT, "
                + DESCRIPTION_COL + " TEXT, "
                + FAV_STATE_COL + " TEXT )")

        db.execSQL(query)
    }

    fun addLikedItem(
        itemId: Int,
        title: String? = null,
        description: String? = null,
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_COL, itemId)
        values.put(TITLE_COL, title)
        values.put(DESCRIPTION_COL, description)
        db.insert(LIKED_TABLE_NAME, "", values)
//       db.insertOrThrow()
        db.close()
    }

    fun deleteFavourite(itemId: Int){
        val db = this.writableDatabase
        db.delete(LIKED_TABLE_NAME, "item_id = $itemId", null)

    }

    fun cleanupFavourites(){
        val db = this.writableDatabase
        db.delete(LIKED_TABLE_NAME, null, null)
        db.close()
    }


    fun getLikedItemsIds() {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            LIKED_TABLE_NAME, arrayOf(ID_COL), null, null, null, null, null
        )
        cursor.moveToFirst()
        while(cursor.moveToNext()){
            ItemsRepo.likedItemsIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL)))
//            Age.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
        }
        cursor.close()

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + LIKED_TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "auctiondb"
        private const val DB_VERSION = 1
        private const val LIKED_TABLE_NAME = "favourite"
        private const val ID_COL = "item_id"
        private const val TITLE_COL = "title"
        private const val DESCRIPTION_COL = "description"
        private const val FAV_STATE_COL = "fav_state"
//        private const val TRACKS_COL = "tracks"
    }
}
