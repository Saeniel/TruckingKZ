package klippe.dev.azatcp;

import android.content.Intent;
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

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.lvEventList)
    ListView getEventList;
    @BindView(R.id.ivAbout)
    ImageView getIvAbout;
    @BindView(R.id.ivProfile)
    ImageView getIvProfile;
    ArrayList<Event> events = new ArrayList<Event>();
    EventAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
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


