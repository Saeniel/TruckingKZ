package klippe.dev.truckkz;

import android.Manifest;
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
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    @BindView(R.id.etSearchFrom)
    EditText etSearchFrom;

    @BindView(R.id.etSearchTo)
            EditText etSearchTo;

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

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(CargoActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(CargoActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

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

        etSearchFrom.addTextChangedListener(new TextWatcher() {
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

        etSearchTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
}


