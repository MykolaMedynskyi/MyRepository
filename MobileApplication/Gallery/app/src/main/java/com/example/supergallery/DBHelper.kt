package com.example.supergallery

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_VERSION = 1
val DATABASE_NAME = "imagesDB"
val TABLE_IMAGES = "images"

val ID = "id"
val IMAGE_NAME = "name"
val IMAGE_DESCRIPTION = "description"
val IMAGE_RATING = "rating"

class DBHelper(var context:Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db:SQLiteDatabase) {
        val query = "CREATE TABLE " + TABLE_IMAGES + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGE_NAME + " VARCHAR(45), " +
                IMAGE_DESCRIPTION + " VARCHAR(45), " +
                IMAGE_RATING + " INTEGER);"
        db.execSQL(query)
    }

    override fun onUpgrade(db:SQLiteDatabase, oldVersion:Int, newVersion:Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_IMAGES")

        onCreate(db)
    }

    fun insertImage(image: Image){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(IMAGE_NAME, image.name)
        cv.put(IMAGE_DESCRIPTION, image.description)
        cv.put(IMAGE_RATING, image.rating)

        var result = db.insert(TABLE_IMAGES, null, cv)

        if (result == -1.toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun printAll():String {
        var res = ""

        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_IMAGES"
        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var id = cursor.getString(cursor.getColumnIndex(ID))
                    var name = cursor.getString(cursor.getColumnIndex(IMAGE_NAME))
                    var description = cursor.getString(cursor.getColumnIndex(IMAGE_DESCRIPTION))
                    res = ("$res\n$id, $name, $description")
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return res
    }

    fun allImages(): ArrayList<Image>{
        val imgList: ArrayList<Image> = ArrayList()

        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_IMAGES ORDER BY rating DESC"
        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(ID))
                    val name = cursor.getString(cursor.getColumnIndex(IMAGE_NAME))
                    val description = cursor.getString(cursor.getColumnIndex(IMAGE_DESCRIPTION))
                    val rating = cursor.getInt(cursor.getColumnIndex(IMAGE_RATING))
                    imgList.add(Image(id, name, description, rating))
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return imgList
    }

    fun count():Int {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_IMAGES"
        val cursor = db.rawQuery(query, null)
        var count = 0

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.count
            }
        }

        cursor.close()
        db.close()
        println("$count!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!$count")
        return count
    }

    fun deleteItem(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_IMAGES, IMAGE_NAME+"=?", arrayOf(name))

        db.close()
    }

    fun updateItem(name: String, rating: Int, des: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(IMAGE_RATING, rating)
        cv.put(IMAGE_DESCRIPTION, des)

        db.update(TABLE_IMAGES, cv, IMAGE_NAME+"=?", arrayOf(name))

    }

}