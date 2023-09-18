package com.example.iicpshuttle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "User.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "UserInfo";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_STUDENT_ID + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_PHONE_NUMBER + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean checkCredentials(String studentID, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_STUDENT_ID, COLUMN_PASSWORD};
        String selection = COLUMN_STUDENT_ID + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {studentID, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

}


/*package com.example.iicpshuttle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        // 获取可读数据库实例
        SQLiteDatabase db = this.getReadableDatabase();

        // 执行查询操作
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int studentIdIndex = cursor.getColumnIndex("student_id");
                int passwordIndex = cursor.getColumnIndex("password");
                int phoneIndex = cursor.getColumnIndex("phone");

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String studentId = cursor.getString(studentIdIndex);
                String password = cursor.getString(passwordIndex);
                String phone = cursor.getString(phoneIndex);

                // 创建用户对象并添加到列表中
                User user = new User(id, name, studentId, password, phone);
                userList.add(user);
            }
            cursor.close();
        }

        // 关闭数据库连接
        db.close();

        return userList;
    }


    // set database name and version
    private static final String DATABASE_NAME = "user_data.db";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // crate database
        String createTableQuery = "CREATE TABLE user " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "student_id TEXT, " +
                "password TEXT," +
                "phone TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
*/