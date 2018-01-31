package klippe.dev.azatcp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView getEventList;
    ArrayList<Event> events = new ArrayList<Event>();
    EventAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        events = (ArrayList<Event>) getIntent().getExtras().get("listEvent");
        boxAdapter = new EventAdapter(this, events);
        // настраиваем список
        getEventList.setAdapter(boxAdapter);
    }
}
