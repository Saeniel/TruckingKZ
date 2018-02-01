package klippe.dev.azatcp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.lvEventList)
    ListView getEventList;
    @BindView(R.id.ivAbout)
    ImageView getIvAbout;
    @BindView(R.id.ivProfile)
    ImageView getIvProfile;
    ArrayList<Event> events = new ArrayList<Event>();
    EventAdapter boxAdapter;

    private Cursor cursorImage;
    private DatabaseHelper db;
    String login;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";
    SharedPreferences mSettings;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursorImage.close();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        db = new DatabaseHelper(EventActivity.this);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_PREFERENCES_LOGIN)) {
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
                for (Event a:events
                     ) {
                    if(a.checked){
                        bufevents.add(a);
                    }
                }
                intent.putExtra("listEvent",bufevents);
                startActivity(intent);
            }
        });
    }

    // генерируем данные для адаптера
    void fillData() {
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

        Gson gson = new Gson();
        Event[] founderArray = gson.fromJson(json, Event[].class);
        for(int i=0;i<founderArray.length;i++){
            events.add(founderArray[i]);
        }
    }

    // выводим информацию о корзине
    public void showResult(View v) {
        String result = "Товары в корзине:";
        for (Event p : boxAdapter.getBox()) {
            if (p.checked)
                result += "\n" + p.title;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}


