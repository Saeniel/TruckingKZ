package klippe.dev.azatcp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
    ArrayList<Event> products = new ArrayList<Event>();
    EventAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        // создаем адаптер
        fillData();
        boxAdapter = new EventAdapter(this, products);
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
                startActivity(intent);
            }
        });
    }

    // генерируем данные для адаптера
    void fillData() {
        for (int i = 1; i <= 20; i++) {
            products.add(new Event("Title " + i, "descriptin " + i,
                    R.drawable.test));
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


