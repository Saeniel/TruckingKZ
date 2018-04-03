package klippe.dev.truckkz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.imvProfileUserPic)
    ImageView getProfileUserPic;

    @BindView(R.id.tvProfileName)
    TextView getProfileName;

    @BindView(R.id.listView)
    ListView getCargoList;

    @BindView(R.id.button)
    Button getBtnBack;

    ArrayList<Cargo> cargos = new ArrayList<Cargo>();
    CargoAdapter boxAdapter;

    private Cursor cursor;
    private Cursor cursorImage;
    private DatabaseHelper db;
    String login;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";
    SharedPreferences mSettings;

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

        cargos = (ArrayList<Cargo>) getIntent().getExtras().get("listCargo");
        boxAdapter = new CargoAdapter(this, true);
        // настраиваем список
        getCargoList.setAdapter(boxAdapter);

        getBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, CargoActivity.class);
        intent.putExtra("profileCargos", cargos);
        startActivity(intent);
    }
}
