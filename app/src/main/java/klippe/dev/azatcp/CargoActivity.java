package klippe.dev.azatcp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CargoActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";

    @BindView(R.id.lvEventList)
    ListView getEventList;

    @BindView(R.id.ivAbout)
    ImageView getIvAbout;

    @BindView(R.id.ivProfile)
    CircleImageView getIvProfile;

    @BindView(R.id.etSearch)
    EditText editText;

    ArrayList<Cargo> cargos = new ArrayList<Cargo>();
    CargoAdapter boxAdapter;
    String login;
    SharedPreferences mSettings;
    private Cursor cursorImage;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo);
        ButterKnife.bind(this);

        db = new DatabaseHelper(CargoActivity.this);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_LOGIN)) {
            login = mSettings.getString(APP_PREFERENCES_LOGIN, "");
        }

        cursorImage = db.getUserPic(login);
        File imgFile = new File(cursorImage.getString(0));

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            getIvProfile.setImageBitmap(myBitmap);
        }


        // создаем адаптер
        fillData();
        boxAdapter = new CargoAdapter(this, cargos);
        // настраиваем список
        getEventList.setAdapter(boxAdapter);

        getIvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CargoActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        getIvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CargoActivity.this, ProfileActivity.class);
                ArrayList<Cargo> bufevents = new ArrayList<Cargo>();
                for (Cargo a : cargos
                        ) {
                    if (a.isChecked) {
                        bufevents.add(a);
                    }
                }
                intent.putExtra("listEvent", bufevents);
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boxAdapter.getFilter().filter(s.toString());
            }
        });
    }

    //Считываем JSON файл из папки ресурсов
    String readFromAssetJSON() {
        String json = null;
        try {
            InputStream is = getAssets().open("cargo.JSON");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    // генерируем данные для адаптера
    void fillData() {
        Gson gson = new Gson();
        Cargo[] founderArray = gson.fromJson(readFromAssetJSON(), Cargo[].class);
        for (int i = 0; i < founderArray.length; i++) {
            cargos.add(founderArray[i]);
        }
    }

    @Override
    public void onBackPressed() {
    }
}


