package edu.umich.si.inteco.minuku.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.model.User;

/**
 * Created by Michael Ho on 2/10/2017.
 */

public class UserSettingsDBHelper extends SQLiteOpenHelper {

    static final String DBNAME = "usersettings.sqlite";
    static final int VERSION = 5;
    static final String TABLENAME = "usersettings_list";
    // DB params
    private String ID = "Id";
    private String USER_NAME = "USER_NAME";
    private String USER_AGE = "USER_AGE";
    private String USER_IMGNUM = "USER_IMGNUM";
    private String IF_SELECTED = "IF_SELECTED";
    private String USER_NUMBER = "USER_NUMBER";

    public UserSettingsDBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    private void createDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLENAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                USER_NAME + " VARCHAR(30)," +
                USER_AGE + " INTEGER," +
                USER_IMGNUM + " VARCHAR(10)," +
                IF_SELECTED + " VARCHAR(15)," +
                USER_NUMBER + " VARCHAR(10)" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public long insertDB(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getUserName());
        values.put(USER_AGE, user.getUserAge());
        values.put(USER_IMGNUM, user.getImgNumber());
        values.put(IF_SELECTED, user.getIfSelected());
        values.put(USER_NUMBER, user.getUserNumber());

        long rowId = db.insert(TABLENAME, null, values);
        db.close();

        return rowId;
    }

    public int updateDB(String id, User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getUserName());
        values.put(USER_AGE, user.getUserAge());
        values.put(USER_IMGNUM, user.getImgNumber());
        values.put(IF_SELECTED, user.getIfSelected());
        values.put(USER_NUMBER, user.getUserNumber());
        String whereClause = ID + "='" + id + "'";

        int count = db.update(TABLENAME, values, whereClause, null);
        db.close();

        return count;
    }

    public boolean getIfUserSelectedById(String id) {
        boolean select = false;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {IF_SELECTED};
        String whereClause = ID + " = ?;";
        String[] whereArgs = {id};
        Cursor cursor = db.query(TABLENAME, columns, whereClause, whereArgs,
                null, null, null);
        if (cursor.moveToNext())
            if ("1".equalsIgnoreCase(cursor.getString(0)))
                select = true;

        cursor.close();
        db.close();

        return select;
    }

    public void setAllUserSelected() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + TABLENAME + " SET " + IF_SELECTED + " = \"1\";";
        db.execSQL(sql);

        db.close();
    }

    public boolean setAllUserUnSelected() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + TABLENAME + " SET " + IF_SELECTED + " = \"0\";";
        db.execSQL(sql);

        db.close();

        return true;
    }

    public int deleteUserById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = ID + "='" + id + "'";
        int count = db.delete(TABLENAME, whereClause, null);
        db.close();

        return count;
    }

    public ArrayList<User> getAllUserList() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> usertList = new ArrayList<User>();
        String sql = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String userName = cursor.getString(1);
            String useAge = cursor.getString(2);
            String userImgNum = cursor.getString(3);
            String ifSelected = cursor.getString(4);
            String userNumber = cursor.getString(5);
            User user = new User(userName, useAge, userImgNum, ifSelected, userNumber);
            usertList.add(user);
        }

        cursor.close();
        db.close();

        return usertList;
    }

    public ArrayList<User> getAllUserListSortByAge() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> sortedUserList = new ArrayList<User>();
        String sql = "SELECT * FROM " + TABLENAME + " ORDER BY " + USER_AGE + " ASC";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String userName = cursor.getString(1);
            String userAge = cursor.getString(2);
            String userImgNum = cursor.getString(3);
            String ifSelected = cursor.getString(4);
            String userNumber = cursor.getString(5);
            User user = new User(userName, userAge, userImgNum, ifSelected, userNumber);
            sortedUserList.add(user);
        }

        cursor.close();
        db.close();

        return sortedUserList;
    }

    public ArrayList<Integer> getAllIdList() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> idList = new ArrayList<Integer>();

        String sql = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int databaseId = cursor.getInt(0);
            idList.add(databaseId);
        }

        cursor.close();
        db.close();

        return idList;
    }

    public ArrayList<Integer> getAllIdListSortByAge() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> sortedIdList = new ArrayList<Integer>();

        String sql = "SELECT * FROM " + TABLENAME + " ORDER BY " + USER_AGE + " ASC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int databaseId = cursor.getInt(0);
            sortedIdList.add(databaseId);
        }

        cursor.close();
        db.close();

        return sortedIdList;
    }

    public String getSelectedUserNumbers() {
        String userNumbers = "";
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            if ("1".equalsIgnoreCase(cursor.getString(4))) {
                userNumbers += cursor.getString(5) + ",";
            }
        }

        cursor.close();
        db.close();

        return userNumbers;
    }

    public int getCurrentNumOfUser() {
        int userCount = 0;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            if ("1".equalsIgnoreCase(cursor.getString(4))) {
                userCount++;
            }
        }
        cursor.close();
        db.close();

        return userCount;
    }

    public int getTotalNumOfUser() {
        int userCount = 0;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            userCount++;
        }
        cursor.close();
        db.close();

        return userCount;
    }

    public boolean getIfColumnContainNull() {
        boolean containNull = false;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {USER_NAME, USER_AGE};
        Cursor cursor = db.query(TABLENAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            if ("".equalsIgnoreCase(cursor.getString(0)) || "".equalsIgnoreCase(cursor.getString(1)) || "0".equalsIgnoreCase(cursor.getString(1)))
                containNull = true;

            if (null == cursor.getString(0) || null == cursor.getString(1))
                containNull = true;
        }

        cursor.close();
        db.close();

        return containNull;
    }

    public boolean checkIfUserSelectIcon() {
        boolean ifAllUsersSelectedIcon = true;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            if ("24".equalsIgnoreCase(cursor.getString(3))) {
                ifAllUsersSelectedIcon = false;
            }
        }

        cursor.close();
        db.close();

        return ifAllUsersSelectedIcon;
    }

    public boolean checkIfIconTaken(String imgNum) {
        boolean ifIconTaken = false;
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {USER_IMGNUM};
        Cursor cursor = db.query(TABLENAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            if (imgNum.equalsIgnoreCase(cursor.getString(0)))
                ifIconTaken = true;
        }

        cursor.close();
        db.close();
        return ifIconTaken;
    }
}
