package klippe.dev.azatcp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";

    @BindView(R.id.lvEventList)
    ListView getEventList;

    @BindView(R.id.ivAbout)
    ImageView getIvAbout;
    @BindView(R.id.ivProfile)
    ImageView getIvProfile;
    @BindView(R.id.etSearch)
    EditText editText;
    ArrayList<Event> events = new ArrayList<Event>();
    EventAdapter boxAdapter;
    String login;
    SharedPreferences mSettings;
    private Cursor cursorImage;
    private DatabaseHelper db;

    public static Bitmap decodeUriToBitmap(Context mContext, Uri sendUri) {
        Bitmap getBitmap = null;
        try {
            InputStream image_stream;
            try {
                image_stream = mContext.getContentResolver().openInputStream(sendUri);
                getBitmap = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap;
    }



    @Override
    protected void onResume() {
        super.onResume();
        cursorImage = db.getUserPic(login);
        Uri pathToImg = Uri.parse(cursorImage.getString(0));
       // Picasso.with(getApplicationContext()).load(pathToImg).into(getIvProfile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        db = new DatabaseHelper(EventActivity.this);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_LOGIN)) {
            login = mSettings.getString(APP_PREFERENCES_LOGIN, "");
        }

        cursorImage = db.getUserPic(login);
        Uri pathToImg = Uri.parse(cursorImage.getString(0));
        getIvProfile.setImageURI(pathToImg);

        // создаем адаптер
        fillData();
        boxAdapter = new EventAdapter(this, events);
        // настраиваем список
        getEventList.setAdapter(boxAdapter);

        getIvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        getIvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, ProfileActivity.class);
                ArrayList<Event> bufevents = new ArrayList<Event>();
                for (Event a : events
                        ) {
                    if (a.checked) {
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
    String readFromAssetJSON(){
        String json = null;
        try {
            InputStream is = getAssets().open("event.JSON");
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
        Event[] founderArray = gson.fromJson(readFromAssetJSON(), Event[].class);
        for (int i = 0; i < founderArray.length; i++) {
            events.add(founderArray[i]);
        }
    }

    @Override
    public void onBackPressed() {
    }
}


