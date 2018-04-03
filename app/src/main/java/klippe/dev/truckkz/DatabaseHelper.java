package klippe.dev.truckkz;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Objects;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "sandbox.db";
    private static final int DATABASE_VERSION = 1;
    Cursor c;
    Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    //Получение имени пользователя по логину
    public Cursor getUserName(String login) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String sqlTable = "users";
        qb.setTables(sqlTable);
        String sql = "SELECT name FROM users WHERE login = '" + login + "'";
        try {
            c = db.rawQuery(sql, null);
            c.moveToFirst();
        } catch (Exception e) {
            Toast.makeText(mContext, "No such user in database", Toast.LENGTH_LONG).show();
        }
        return c;
    }

    //Получение аватара из бд по логину
    public Cursor getUserPic(String login) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String sqlTable = "users";
        qb.setTables(sqlTable);
        String sql = "SELECT photo FROM users WHERE login = '" + login + "'";
        try {
            c = db.rawQuery(sql, null);
            c.moveToFirst();
        } catch (Exception e) {
            Toast.makeText(mContext, "No such user in database", Toast.LENGTH_LONG).show();
        }
        return c;
    }

    //Добавление пользователя
    public Cursor addUser(String login, String password, String name, String imagePath) {
        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("users");
        String sql = "INSERT INTO users VALUES (?, ?, ?, ?)";
        Cursor c = db.rawQuery(sql, new String[]{login, password, name, imagePath});
        c.moveToFirst();
        return c;
    }

    //Авторизация и проверка пароля и логина
    public boolean getUser(String login, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String sqlTable = "users";
        qb.setTables(sqlTable);
        String sql = "SELECT password FROM users WHERE login = '" + login + "'";
        try {
            c = db.rawQuery(sql, null);
            c.moveToFirst();

            if (Objects.equals(c.getString(0), pass)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "No such user in database", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //Проверяем пустая ли база данных
    public boolean isTableEmpty() {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database, "users");

        if (NoOfRows == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getData() {

        ArrayList<Cargo> result = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String sqlTable = "cargo";

        qb.setTables(sqlTable);
        String sql = "SELECT * FROM " + sqlTable;

        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();

        return c;
    }
}
