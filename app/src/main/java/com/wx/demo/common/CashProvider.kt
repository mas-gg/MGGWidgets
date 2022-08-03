package com.wx.demo.common

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class CashProvider : ContentProvider() {
    companion object {
        private const val AUTHORITY = "com.iHandy135.wx.demo.provider"
        const val CASH = "content://${AUTHORITY}/CASH"
    }

    private val uriMatcher by lazy {
        UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "CASH/*", 0)
            addURI(AUTHORITY, "CASH", 1)
        }
    }
    private lateinit var dbHelper: CashDBHelper

    override fun onCreate(): Boolean {
        return context?.let {
            dbHelper = CashDBHelper(it)
            true
        } ?: false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        return when (uriMatcher.match(uri)) {
            0 -> {
                val key = uri.pathSegments[1]
                db.query("CASH", projection, "_key = ?", arrayOf(key), null, null, sortOrder)
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            0 -> {
                "vnd.android.cursor.item/$AUTHORITY.CASH"
            }
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            0, 1 -> {
                val key = db.insert("CASH", null, values)
                Uri.parse("content://$AUTHORITY/CASH/$key")
            }
            else -> null
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            0 -> {
                val key = uri.pathSegments[1]
                db.delete("CASH", "_key = ?", arrayOf(key))
            }
            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase
        val row = when (uriMatcher.match(uri)) {
            0, 1 -> {
                db.update("CASH", values, "_key = ?", arrayOf(values?.getAsString("_key")))
            }
            else -> 0
        }
        return row
    }
}