package klippe.dev.truckkz;

import android.app.Application;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Saeniel on 03.04.2018.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        fillData();
    }

    // генерируем данные для адаптера
    void fillData() {
        CargoStorage.selectedCargos = new ArrayList<>();
        CargoStorage.notSelectedCargos = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursorData = null;

        try {
            cursorData = db.getData();
            if (cursorData.moveToFirst()) {
                while (!cursorData.isAfterLast()) {
                    String image = cursorData.getString(0);
                    String from = cursorData.getString(1);
                    String to = cursorData.getString(2);
                    String when = cursorData.getString(3);
                    String price = cursorData.getString(4);
                    String machineType = cursorData.getString(5);
                    String comment = cursorData.getString(6);
                    boolean isChecked = false;

                    Cargo c = new Cargo(image, from, to, when, price, machineType, comment, isChecked);
                    CargoStorage.notSelectedCargos.add(c);
                    cursorData.moveToNext();
                }
            }
        } finally {
            if (cursorData != null) {
                cursorData.close();
            }
        }
    }
}
