package com.begdev.bigbid.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.api.model.Person
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo


class DBHandlerLocal
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        var query = ("CREATE TABLE " + LIKED_TABLE_NAME + " ("
                + ITEM_ID_COL + " INTEGER PRIMARY KEY, "
                + TITLE_COL + " TEXT, "
                + DESCRIPTION_COL + " TEXT, "
                + CATEGORY_COL + " TEXT, "
                + PRICE_COL + " FLOAT, "
                + PHOTO_COL + " TEXT ) ")
        db.execSQL(query)
        query = ("CREATE TABLE " + USER_TABLE_NAME + " ("
                + USERID_COL + " INTEGER PRIMARY KEY, "
                + EMAIL_COL + " TEXT, "
                + USERNAME_COL + " TEXT, "
                + ACCOUNT_TYPE_COL + " TEXT, "
                + PASSWORD_HASH_COL + " TEXT )")
        db.execSQL(query)
        query = ("CREATE TABLE " + OWNER_TABLE_NAME + " ("
                + ITEM_ID_COL + " INTEGER PRIMARY KEY, "
                + TITLE_COL + " TEXT, "
                + DESCRIPTION_COL + " TEXT, "
                + END_TIME_COL + " DATE, "
                + BIDDING_COND_COL + " TEXT, "
                + PRICE_COL + " FLOAT, "
                + PHOTO_COL + " TEXT, "
                + CATEGORY_COL + " TEXT )")
        db.execSQL(query)
        query = ("CREATE TABLE " + BIDS_TABLE_NAME + " ("
                + BID_ID_COL + " INTEGER PRIMARY KEY, "
                + ITEM_ID_COL + " INTEGER, "
                + BIDDER_USERNAME_COL + " TEXT, "
                + TIME_BID_COL + " DATE, "
                + PRICE_COL + " FLOAT )")
        db.execSQL(query)
    }

    fun addLikedItem(
        itemId: Int,
        title: String? = null,
        description: String? = null,
        category: String? = null,
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ITEM_ID_COL, itemId)
        values.put(TITLE_COL, title)
        values.put(DESCRIPTION_COL, description)
        values.put(CATEGORY_COL, category)
        db.insert(LIKED_TABLE_NAME, "", values)
        db.close()
    }

    fun saveBidsListLocal(bidsList: List<Bid>, username: String? = null) {
        val db = this.writableDatabase
//        db.delete(BIDS_TABLE_NAME, null, null)
        bidsList.forEach {
            val values = ContentValues()
            values.put(BID_ID_COL, it.id)
            values.put(ITEM_ID_COL, it.itemId)
            values.put(BIDDER_USERNAME_COL, it.bidderUsername)
            values.put(TIME_BID_COL, it.timeBid)
            values.put(PRICE_COL, it.price)
            if (username != null) {
                values.put(BIDDER_USERNAME_COL, username)
            }
            db.insert(BIDS_TABLE_NAME, "", values)
        }
        db.close()
    }

    fun saveBidLocal(bid: Bid) {
        val db = this.writableDatabase
//        db.delete(BIDS_TABLE_NAME, null, null)
        val values = ContentValues()
        values.put(BID_ID_COL, bid.id)
        values.put(ITEM_ID_COL, bid.itemId)
        values.put(BIDDER_USERNAME_COL, bid.bidderUsername)
        values.put(TIME_BID_COL, bid.timeBid)
        values.put(PRICE_COL, bid.price)
        db.insert(LIKED_TABLE_NAME, "", values)
        db.close()
    }

    fun getItemBidsLocalList(itemId: Int): List<Bid> {
        val db = this.readableDatabase
        val bids = mutableListOf<Bid>()
        val cursor = db.rawQuery(
            "SELECT * FROM ${BIDS_TABLE_NAME} WHERE ${ITEM_ID_COL} = ${itemId}", null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BID_ID_COL))
                val itemId = getInt(getColumnIndexOrThrow(ITEM_ID_COL))
                val bidderUsername = getString(getColumnIndexOrThrow(BIDDER_USERNAME_COL))
                val timeBid = getString(getColumnIndexOrThrow(TIME_BID_COL))
                val price = getFloat(getColumnIndexOrThrow(PRICE_COL))
//                val photo = getString(getColumnIndexOrThrow(PHOTO_COL))
//                val biddingCondition = getString(getColumnIndexOrThrow(BIDDING_COND_COL))
                val bid =
                    Bid(
                        id = id,
                        itemId = itemId,
                        bidderUsername = bidderUsername,
                        timeBid = timeBid,
                        price = price,
                    )
                bids.add(bid)
            }
        }
        return bids
    }

    fun getUserBidsLocal(): List<Bid> {
        val db = this.readableDatabase
        val bids = mutableListOf<Bid>()
        val cursor = db.rawQuery(
            "SELECT * FROM ${BIDS_TABLE_NAME} WHERE ${BIDDER_USERNAME_COL} = \"${UsersRepo.currentUser?.username}\" order by ${TIME_BID_COL} desc",
            null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BID_ID_COL))
                val itemId = getInt(getColumnIndexOrThrow(ITEM_ID_COL))
                val bidderUsername = getString(getColumnIndexOrThrow(BIDDER_USERNAME_COL))
                val timeBid = getString(getColumnIndexOrThrow(TIME_BID_COL))
                val price = getFloat(getColumnIndexOrThrow(PRICE_COL))
//                val photo = getString(getColumnIndexOrThrow(PHOTO_COL))
//                val biddingCondition = getString(getColumnIndexOrThrow(BIDDING_COND_COL))
                val bid =
                    Bid(
                        id = id,
                        itemId = itemId,
                        bidderUsername = bidderUsername,
                        timeBid = timeBid,
                        price = price,
                    )
                bids.add(bid)
            }
        }
        return bids
    }

    fun unlikeItem(itemId: Int) {
        val db = this.writableDatabase
        db.delete(LIKED_TABLE_NAME, "item_id = $itemId", null)
    }

    fun saveLikedItems(itemsList: List<Item>) {
        val db = this.writableDatabase
        db.delete(LIKED_TABLE_NAME, null, null)
        itemsList.forEach {
            val values = ContentValues()
            values.put(ITEM_ID_COL, it.id)
            values.put(TITLE_COL, it.title)
            values.put(DESCRIPTION_COL, it.description)
            values.put(CATEGORY_COL, it.category)
            values.put(PRICE_COL, it.currentPrice)
            values.put(PHOTO_COL, it.photo)
//            values.put(BIDDING_COND_COL, it.biddingCondition)
            db.insert(LIKED_TABLE_NAME, "", values)
        }
        db.close()

    }

    fun saveUserDataLocal(
        userId: Int,
        username: String,
        email: String,
        passwordHash: String,
        accountType: String
    ) {
        val db = this.writableDatabase
        db.delete(USER_TABLE_NAME, null, null)
        val values = ContentValues()
        values.put(USERID_COL, userId)
        values.put(EMAIL_COL, email)
        values.put(USERNAME_COL, username)
        values.put(PASSWORD_HASH_COL, passwordHash)
        values.put(ACCOUNT_TYPE_COL, accountType)
        db.insert(USER_TABLE_NAME, "", values)
        db.close()
    }

    fun saveOwnerItems(itemList: List<Item>) {
        val db = this.writableDatabase
        db.delete(OWNER_TABLE_NAME, null, null)
        itemList.forEach {
            val values = ContentValues()
            values.put(ITEM_ID_COL, it.id)
            values.put(TITLE_COL, it.title)
            values.put(DESCRIPTION_COL, it.description)
            values.put(CATEGORY_COL, it.category)
            values.put(PRICE_COL, it.currentPrice)
            values.put(PHOTO_COL, it.photo)
            values.put(BIDDING_COND_COL, it.biddingCondition)
            values.put(END_TIME_COL, it.auctionEndTime)
            db.insert(OWNER_TABLE_NAME, "", values)
        }
        db.close()
    }

    fun getOwnerItemsList(): List<Item> {
        val db = this.readableDatabase
        val items = mutableListOf<Item>()
        val cursor = db.rawQuery(
            "SELECT * FROM ${OWNER_TABLE_NAME} order by ${BIDDING_COND_COL}", null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ITEM_ID_COL))
                val title = getString(getColumnIndexOrThrow(TITLE_COL))
                val description = getString(getColumnIndexOrThrow(DESCRIPTION_COL))
                val category = getString(getColumnIndexOrThrow(CATEGORY_COL))
                val currentPrice = getFloat(getColumnIndexOrThrow(PRICE_COL))
                val photo = getString(getColumnIndexOrThrow(PHOTO_COL))
                val biddingCondition = getString(getColumnIndexOrThrow(BIDDING_COND_COL))
                val item =
                    Item(
                        id = id,
                        title = title,
                        description = description,
                        category = category,
                        currentPrice = currentPrice,
                        photo = photo,
                        biddingCondition = biddingCondition,
                    )
                items.add(item)
            }
        }
        return items
    }

    fun getOwnerItem(itemId: Int): Item {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${OWNER_TABLE_NAME} where ${ITEM_ID_COL} = ${itemId}", null
        )
        cursor.moveToNext()
        with(cursor) {
            val id = getInt(getColumnIndexOrThrow(ITEM_ID_COL))
            val title = getString(getColumnIndexOrThrow(TITLE_COL))
            val description = getString(getColumnIndexOrThrow(DESCRIPTION_COL))
            val category = getString(getColumnIndexOrThrow(CATEGORY_COL))
            val currentPrice = getFloat(getColumnIndexOrThrow(PRICE_COL))
            val photo = getString(getColumnIndexOrThrow(PHOTO_COL))
            val biddingCondition = getString(getColumnIndexOrThrow(BIDDING_COND_COL))
            val auctionEndTime = getString(getColumnIndexOrThrow(END_TIME_COL))
            val item =
                Item(
                    id = id,
                    title = title,
                    description = description,
                    category = category,
                    currentPrice = currentPrice,
                    photo = photo,
                    biddingCondition = biddingCondition,
                    ownerId = UsersRepo.currentUser?.id,
                    auctionEndTime = auctionEndTime
                )
            return item
        }
    }

    fun checkCredentialsOffline(login: String, passwordHash: String): Person? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${USER_TABLE_NAME} WHERE (${EMAIL_COL} = ? OR ${USERNAME_COL} = ?) AND password_hash = ?",
            arrayOf(login, login, passwordHash)
        )
        val result = cursor.moveToFirst()
        val userId = cursor.getInt(cursor.getColumnIndexOrThrow(USERID_COL))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COL))
        val username = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME_COL))
        val accountType = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME_COL))
        cursor.close()
//        cursor.getInt(cursor.getColumnIndexOrThrow(USERID_COL))
        return Person(id = userId, email = email, username = username, accountType = accountType)

    }


    fun uploadLikedIdsFromLocal() {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            LIKED_TABLE_NAME, arrayOf(ITEM_ID_COL), null, null, null, null, null
        )
        cursor.moveToFirst()
        while (cursor.moveToNext()) {
            ItemsRepo.likedItemsIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(ITEM_ID_COL)))
//            Age.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
        }
        cursor.close()
    }

    fun getLikedItemsList(): List<Item> {
        val db = this.readableDatabase
        val items = mutableListOf<Item>()
        val cursor = db.rawQuery(
            "SELECT * FROM ${LIKED_TABLE_NAME}", null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ITEM_ID_COL))
                val title = getString(getColumnIndexOrThrow(TITLE_COL))
                val description = getString(getColumnIndexOrThrow(DESCRIPTION_COL))
                val category = getString(getColumnIndexOrThrow(CATEGORY_COL))
                val currentPrice = getFloat(getColumnIndexOrThrow(PRICE_COL))
                val photo = getString(getColumnIndexOrThrow(PHOTO_COL))
//                val biddingCondition = getString(getColumnIndexOrThrow(BIDDING_COND_COL))
                val item =
                    Item(
                        id = id,
                        title = title,
                        description = description,
                        category = category,
                        currentPrice = currentPrice,
                        photo = photo,
//                        biddingCondition = biddingCondition,
                    )
                items.add(item)
                ItemsRepo.likedItemsIds.add(item.id!!)
            }
        }
        return items
    }


    fun getLikedItem(itemId: Int): Item {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${LIKED_TABLE_NAME} where ${ITEM_ID_COL} = ${itemId}", null
        )
        cursor.moveToNext()
        with(cursor) {
            val id = getInt(getColumnIndexOrThrow(ITEM_ID_COL))
            val title = getString(getColumnIndexOrThrow(TITLE_COL))
            val description = getString(getColumnIndexOrThrow(DESCRIPTION_COL))
            val category = getString(getColumnIndexOrThrow(CATEGORY_COL))
            val currentPrice = getFloat(getColumnIndexOrThrow(PRICE_COL))
            val photo = getString(getColumnIndexOrThrow(PHOTO_COL))
            val item =
                Item(
                    id = id,
                    title = title,
                    description = description,
                    category = category,
                    currentPrice = currentPrice,
                    photo = photo,
                )
            return item
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + LIKED_TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "auction.db"
        private const val DB_VERSION = 1
        private const val LIKED_TABLE_NAME = "favourite"
        private const val USER_TABLE_NAME = "user"
        private const val OWNER_TABLE_NAME = "owner"
        private const val BIDS_TABLE_NAME = "bids"

        //        private const val ID_COL = "item_id"
        private const val TITLE_COL = "title"
        private const val DESCRIPTION_COL = "description"
        private const val CATEGORY_COL = "category"

        //        private const val CURENT_PRICE = "price"
        private const val FAV_STATE_COL = "fav_state"
        private const val USERID_COL = "user_id"
        private const val EMAIL_COL = "email"
        private const val USERNAME_COL = "username"
        private const val PASSWORD_HASH_COL = "password_hash"
        private const val ACCOUNT_TYPE_COL = "account_type"
        private const val END_TIME_COL = "end_time"
        private const val ITEM_ID_COL = "item_id"
        private const val BID_ID_COL = "bid_id"
        private const val BIDDER_USERNAME_COL = "bidder_username"
        private const val TIME_BID_COL = "time_bid"
        private const val PRICE_COL = "price"
        private const val PHOTO_COL = "photo"
        private const val BIDDING_COND_COL = "bidding_cond"
//        private const val TRACKS_COL = "tracks"
    }
}
