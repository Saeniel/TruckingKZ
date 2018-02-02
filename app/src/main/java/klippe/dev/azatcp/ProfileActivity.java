package klippe.dev.azatcp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.imvProfileUserPic)
    CircleImageView getProfileUserPic;

    @BindView(R.id.tvProfileName)
    TextView getProfileName;

    @BindView(R.id.listView)
    ListView getEventList;

    @BindView(R.id.button)
    Button getBtnBack;

    ArrayList<Event> events = new ArrayList<Event>();
    EventAdapter boxAdapter;

    private Cursor cursor;
    private Cursor cursorImage;
    private DatabaseHelper db;
    String login;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";
    SharedPreferences mSettings;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        cursorImage.close();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        db = new DatabaseHelper(ProfileActivity.this);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_PREFERENCES_LOGIN)) {
            login = mSettings.getString(APP_PREFERENCES_LOGIN, "");
        }

        cursor = db.getUserName(login);
        getProfileName.setText(cursor.getString(0));

        cursorImage = db.getUserPic(login);
        Uri pathToImg = Uri.parse(cursorImage.getString(0));
        getProfileUserPic.setImageURI(pathToImg);

        events = (ArrayList<Event>) getIntent().getExtras().get("listEvent");
        boxAdapter = new EventAdapter(this, events);
        // настраиваем список
        getEventList.setAdapter(boxAdapter);

        getBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
