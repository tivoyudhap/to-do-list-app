package com.mastip.personaltodolist.manager.database

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.model.TaskModel
import com.mastip.personaltodolist.utilities.Constant
import com.mastip.personaltodolist.utilities.CustomSharedPreferences
import java.util.*

class Storage(private var context: Context) : SQLiteOpenHelper(context, Constant.DB_NAME + ".db", null, 1) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TB_TASK)
        sqLiteDatabase.execSQL(CREATE_TB_LOGIN)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        val QUERY = Constant.TAG_DROP_TABLE + Constant.TABLE_TASK
        val QUERYLOGIN = Constant.TAG_DROP_TABLE + Constant.TABLE_LOGIN
        sqLiteDatabase.execSQL(QUERY)
        sqLiteDatabase.execSQL(QUERYLOGIN)
    }

    fun addTransaction(taskModel: TaskModel): Long {
        val sqLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(Constant.TAG_TASK, taskModel.task)
        values.put(Constant.TAG_ID_USER, CustomSharedPreferences.getDataString(Constant.TAG_USERNAME))
        values.put(Constant.TAG_CREATED_AT, taskModel.time)
        values.put(Constant.TAG_SUBMISSION, taskModel.submission)
        values.put(Constant.TAG_PRIORITY, taskModel.priority)
        values.put(Constant.TAG_STATUS, taskModel.status)
        val id = sqLiteDatabase.insert(Constant.TABLE_TASK, null, values)
        sqLiteDatabase.close()
        return id
    }

    fun disposeTransaction(id: String): String? {
        val time = Date().time
        val sqLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(Constant.TAG_STATUS, 2)
        values.put(Constant.TAG_FINISH, time)
        val res = sqLiteDatabase.update(Constant.TABLE_TASK, values, Constant.TAG_ID + " = " + id, null)
        return if (res > 0) {
            sqLiteDatabase.close()
            context.getString(R.string.success_to_delete_task)
        } else {
            sqLiteDatabase.close()
            context.getString(R.string.failed_to_delete_task)
        }
    }

    fun getAllData(cat: Int): ArrayList<TaskModel> {
        var QUERY = ""
        QUERY = if (cat == Constant.TAG_UNFINISH_ADDTASK) {
            "SELECT * FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_STATUS + " = 1 AND " + Constant.TAG_ID_USER + " = '" + CustomSharedPreferences.getDataString(Constant.TAG_USERNAME) + "' ORDER BY " + Constant.TAG_SUBMISSION + " ASC, " + Constant.TAG_CREATED_AT + " DESC"
        } else {
            "SELECT * FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_STATUS + " = 2 AND " + Constant.TAG_ID_USER + " = '" + CustomSharedPreferences.getDataString(Constant.TAG_USERNAME) + "' ORDER BY " + Constant.TAG_FINISH + " DESC"
        }
        val datas = ArrayList<TaskModel>()
        val sqLiteDatabase = readableDatabase
        val cursor = sqLiteDatabase.rawQuery(QUERY, null)
        if (cursor != null) {
            cursor.moveToFirst()
            for (a in 0 until cursor.count) {
                datas.add(TaskModel(cursor.getString(0), cursor.getString(2), cursor.getLong(4), cursor.getLong(5), cursor.getInt(6), cursor.getInt(7), cursor.getLong(3)))
                cursor.moveToNext()
            }
        }
        sqLiteDatabase.close()
        return datas
    }

    val allData: ArrayList<TaskModel>
        get() {
            val QUERY = "SELECT * FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_ID_USER + " = '" + CustomSharedPreferences.getDataString(Constant.TAG_USERNAME) + "' ORDER BY " + Constant.TAG_PRIORITY + " DESC, " + Constant.TAG_CREATED_AT + " DESC"
            val datas = ArrayList<TaskModel>()
            val sqLiteDatabase = readableDatabase
            val cursor = sqLiteDatabase.rawQuery(QUERY, null)
            if (cursor != null) {
                cursor.moveToFirst()
                for (a in 0 until cursor.count) {
                    datas.add(TaskModel(cursor.getString(0), cursor.getString(2), cursor.getLong(4), cursor.getLong(5), cursor.getInt(6), cursor.getInt(7), cursor.getLong(3)))
                    cursor.moveToNext()
                }
            }
            sqLiteDatabase.close()
            return datas
        }

    fun retrieveCount(category: Int): Int {
        val sqLiteDatabase = readableDatabase
        var QUERY = ""
        QUERY = if (category == Constant.TAG_UNFINISH_ADDTASK) {
            "SELECT COUNT(*) FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_ID_USER + " = '" + CustomSharedPreferences.getDataString(Constant.TAG_USERNAME) + "' AND " + Constant.TAG_STATUS + " = " + Constant.TAG_UNFINISH_ADDTASK
        } else {
            "SELECT COUNT(*) FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_ID_USER + " = '" + CustomSharedPreferences.getDataString(Constant.TAG_USERNAME) + "' AND " + Constant.TAG_STATUS + " = " + Constant.TAG_FINISH_ADDTASK
        }
        sqLiteDatabase.close()
        return DatabaseUtils.longForQuery(sqLiteDatabase, QUERY, null).toInt()
    }

    fun checkIfTheUserIsAvailable(username: String): Boolean {
        val sqLiteDatabase = readableDatabase
        val QUERY = "SELECT * FROM " + Constant.TABLE_LOGIN + " WHERE " + Constant.TAG_ID_USER + " = '" + username + "'"
        val cursor = sqLiteDatabase.rawQuery(QUERY, null)
        return cursor.count > 0
    }

    fun checkPasswordForTheUser(username: String, password: String): Boolean {
        val sqLiteDatabase = readableDatabase
        val QUERY = "SELECT * FROM " + Constant.TABLE_LOGIN + " WHERE " + Constant.TAG_ID_USER + " = '" + username + "' AND " + Constant.TAG_PASSWORD + " = '" + password + "'"
        val cursor = sqLiteDatabase.rawQuery(QUERY, null)
        return if (cursor.count > 0) {
            cursor.moveToFirst()
            CustomSharedPreferences.putDataString(Constant.TAG_USERNAME, cursor.getString(1))
            sqLiteDatabase.close()
            true
        } else {
            sqLiteDatabase.close()
            false
        }
    }

    fun createUser(username: String?, password: String?) {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.TAG_ID_USER, username)
        contentValues.put(Constant.TAG_PASSWORD, password)
        sqLiteDatabase.insert(Constant.TABLE_LOGIN, null, contentValues)
        sqLiteDatabase.close()
    }

    companion object {
        private const val CREATE_TB_TASK = (Constant.TAG_CREATE_TABLE + Constant.TABLE_TASK + Constant.TAG_OPEN
                + Constant.TAG_ID + " integer primary key autoincrement,"
                + Constant.TAG_ID_USER + " text,"
                + Constant.TAG_TASK + " text,"
                + Constant.TAG_SUBMISSION + " long,"
                + Constant.TAG_CREATED_AT + " long,"
                + Constant.TAG_FINISH + " long,"
                + Constant.TAG_PRIORITY + " integer,"
                + Constant.TAG_STATUS + " integer"
                + Constant.TAG_CLOSE)
        private const val CREATE_TB_LOGIN = (Constant.TAG_CREATE_TABLE + Constant.TABLE_LOGIN + Constant.TAG_OPEN
                + Constant.TAG_ID + " integer primary key autoincrement,"
                + Constant.TAG_ID_USER + " text unique,"
                + Constant.TAG_PASSWORD + " text"
                + Constant.TAG_CLOSE)
    }

}