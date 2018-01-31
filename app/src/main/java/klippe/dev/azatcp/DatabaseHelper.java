package klippe.dev.azatcp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Saeniel on 31.01.2018.
 */

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "sandbox.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor addUser(String login, String password, String name, String imagePath) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("users");
        String sql = "INSERT INTO users VALUES (?, ?, ?, ?)";
        Cursor c = db.rawQuery(sql, new String[]{login, password, name, imagePath});
        return c;
    }

    public Cursor getUser(String login) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("users");
        String sql = "";
        Cursor c = db.rawQuery(sql, new String[]{});

        c.moveToFirst();
        return c;
    }
}
