package klippe.dev.truckkz;

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

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CargoActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";

    @BindView(R.id.lvCargoList)
    ListView getCargoList;

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
    private ArrayList<Cargo> profileCargos;

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

        boxAdapter = new CargoAdapter(this, false);

        // настраиваем список
        getCargoList.setAdapter(boxAdapter);

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
                ArrayList<Cargo> bufCargos = new ArrayList<Cargo>();
                for (Cargo a : cargos
                        ) {
                    if (a.isChecked) {
                        bufCargos.add(a);
                    }
                }
                intent.putExtra("listCargo", bufCargos);
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


    @Override
    protected void onResume() {
        super.onResume();
        boxAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        /*
        try {
            ArrayList<Cargo> profileCargos = (ArrayList<Cargo>) getIntent().getExtras().get("profileCargos");
            if (!profileCargos.equals(null)) {
                for (int i = 0; i < profileCargos.size(); i++) {
                    Cargo c = profileCargos.get(i);
                    for (int j = 0; j < cargos.size(); j++) {
                        if (c.from.equals(cargos.get(j).from) &&
                                c.to.equals(cargos.get(j).to) &&
                                c.machineType.equals(cargos.get(j).machineType) &&
                                c.price.equals(cargos.get(j).price)) {
                            cargos.get(j).isChecked = true;
                        } else {
                            cargos.get(j).isChecked = false;
                        }
                    }
                }
            }
            getCargoList = null;
            boxAdapter = new CargoAdapter(this, cargos);
            getCargoList.setAdapter(boxAdapter);
        } catch (Exception e) {

        }
        */
    }
}


