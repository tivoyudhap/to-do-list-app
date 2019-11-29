package com.mastip.personaltodolist.manager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.model.TaskModel;
import com.mastip.personaltodolist.utilities.Constant;
import com.mastip.personaltodolist.utilities.CustomSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindString;

/**
 * Created by HateLogcatError on 8/25/2017.
 */

public class Storage extends SQLiteOpenHelper {

    private static String CREATE_TB_TASK = Constant.TAG_CREATE_TABLE + Constant.TABLE_TASK + Constant.TAG_OPEN
            + Constant.TAG_ID + " integer primary key autoincrement,"
            + Constant.TAG_ID_USER + " text,"
            + Constant.TAG_TASK + " text,"
            + Constant.TAG_SUBMISSION + " long,"
            + Constant.TAG_CREATED_AT + " long,"
            + Constant.TAG_FINISH + " long,"
            + Constant.TAG_PRIORITY + " integer,"
            + Constant.TAG_STATUS + " integer"
            + Constant.TAG_CLOSE;

    private static String CREATE_TB_LOGIN = Constant.TAG_CREATE_TABLE + Constant.TABLE_LOGIN + Constant.TAG_OPEN
            + Constant.TAG_ID + " integer primary key autoincrement,"
            + Constant.TAG_ID_USER + " text unique,"
            + Constant.TAG_PASSWORD + " text"
            + Constant.TAG_CLOSE;

    @BindString(R.string.success_to_delete_task) String warningSuccessDeleteTask;
    @BindString(R.string.failed_to_delete_task) String warningFailedDeleteTask;

    Context context;

    public Storage(Context context) {
        super(context, Constant.DB_NAME + ".db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TB_TASK);
        sqLiteDatabase.execSQL(CREATE_TB_LOGIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String QUERY = Constant.TAG_DROP_TABLE + Constant.TABLE_TASK;
        String QUERYLOGIN = Constant.TAG_DROP_TABLE + Constant.TABLE_LOGIN;
        sqLiteDatabase.execSQL(QUERY);
        sqLiteDatabase.execSQL(QUERYLOGIN);
    }

    public long addTransaction(TaskModel taskModel) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.TAG_TASK, taskModel.getTask());
        values.put(Constant.TAG_ID_USER, new CustomSharedPreferences().getDataString(Constant.TAG_USERNAME));
        values.put(Constant.TAG_CREATED_AT, taskModel.getTime());
        values.put(Constant.TAG_SUBMISSION, taskModel.getSubmission());
        values.put(Constant.TAG_PRIORITY, taskModel.getPriority());
        values.put(Constant.TAG_STATUS, taskModel.getStatus());

        long id = sqLiteDatabase.insert(Constant.TABLE_TASK, null, values);
        sqLiteDatabase.close();

        return id;
    }

    public String disposeTransaction(String id) {
        long time = new Date().getTime();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.TAG_STATUS, 2);
        values.put(Constant.TAG_FINISH, time);

        int res = sqLiteDatabase.update(Constant.TABLE_TASK, values, Constant.TAG_ID + " = " + id, null);

        if(res > 0) {
            sqLiteDatabase.close();
            return warningSuccessDeleteTask;
        }
        else {
            sqLiteDatabase.close();
            return warningFailedDeleteTask;
        }
    }

    public ArrayList<TaskModel> getAllData(int cat) {
        String QUERY = "";
        if(cat == Constant.TAG_UNFINISH_ADDTASK) {
            QUERY = "SELECT * FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_STATUS + " = 1 AND " + Constant.TAG_ID_USER + " = '" + new CustomSharedPreferences().getDataString(Constant.TAG_USERNAME) + "' ORDER BY " + Constant.TAG_SUBMISSION + " ASC, " + Constant.TAG_CREATED_AT + " DESC";
        }
        else {
            QUERY = "SELECT * FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_STATUS + " = 2 AND " + Constant.TAG_ID_USER + " = '" + new CustomSharedPreferences().getDataString(Constant.TAG_USERNAME) + "' ORDER BY " + Constant.TAG_FINISH + " DESC";
        }
        ArrayList<TaskModel> datas = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);

        if(cursor != null) {
            cursor.moveToFirst();

            for(int a = 0; a < cursor.getCount(); a++) {
                datas.add(new TaskModel(cursor.getString(0), cursor.getString(2), cursor.getLong(4), cursor.getLong(5), cursor.getInt(6), cursor.getInt(7), cursor.getLong(3)));
                cursor.moveToNext();
            }
        }

        sqLiteDatabase.close();

        return datas;
    }

    public ArrayList<TaskModel> getAllData() {
        String QUERY = "SELECT * FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_ID_USER + " = '" + new CustomSharedPreferences().getDataString(Constant.TAG_USERNAME) + "' ORDER BY " + Constant.TAG_PRIORITY + " DESC, " + Constant.TAG_CREATED_AT + " DESC";

        ArrayList<TaskModel> datas = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);

        if(cursor != null) {
            cursor.moveToFirst();

            for(int a = 0; a < cursor.getCount(); a++) {
                datas.add(new TaskModel(cursor.getString(0), cursor.getString(2), cursor.getLong(4), cursor.getLong(5), cursor.getInt(6), cursor.getInt(7), cursor.getLong(3)));
                cursor.moveToNext();
            }
        }

        sqLiteDatabase.close();

        return datas;
    }

    public int retrieveCount(int category) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String QUERY = "";

        if(category == Constant.TAG_UNFINISH_ADDTASK) {
            QUERY = "SELECT COUNT(*) FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_ID_USER + " = '" + new CustomSharedPreferences().getDataString(Constant.TAG_USERNAME) + "' AND " + Constant.TAG_STATUS + " = " + Constant.TAG_UNFINISH_ADDTASK;
        }
        else {
            QUERY = "SELECT COUNT(*) FROM " + Constant.TABLE_TASK + " WHERE " + Constant.TAG_ID_USER + " = '" + new CustomSharedPreferences().getDataString(Constant.TAG_USERNAME) + "' AND " + Constant.TAG_STATUS + " = " + Constant.TAG_FINISH_ADDTASK;
        }

        sqLiteDatabase.close();

        return (int) DatabaseUtils.longForQuery(sqLiteDatabase, QUERY, null);
    }

    public boolean checkIfTheUserIsAvailable(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String QUERY = "SELECT * FROM " + Constant.TABLE_LOGIN + " WHERE " + Constant.TAG_ID_USER + " = '" + username + "'";

        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);

        if(cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkPasswordForTheUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String QUERY = "SELECT * FROM " + Constant.TABLE_LOGIN + " WHERE " + Constant.TAG_ID_USER + " = '" + username + "' AND " + Constant.TAG_PASSWORD + " = '" + password + "'";

        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            new CustomSharedPreferences().putDataString(Constant.TAG_USERNAME, cursor.getString(1));

            sqLiteDatabase.close();
            return true;
        }
        else {
            sqLiteDatabase.close();
            return false;
        }
    }

    public void createUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.TAG_ID_USER, username);
        contentValues.put(Constant.TAG_PASSWORD, password);

        sqLiteDatabase.insert(Constant.TABLE_LOGIN, null, contentValues);

        sqLiteDatabase.close();
    }
}
