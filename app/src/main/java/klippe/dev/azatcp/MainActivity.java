package klippe.dev.azatcp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etLoginRegister)
    EditText getEtLogin;
    @BindView(R.id.etPasswordRegister)
    EditText getEtPassword;
    @BindView(R.id.btnLogin)
    Button getBtnLogin;
    @BindView(R.id.btnRegister)
    TextView getBtnRegistration;

    private Cursor cursor;
    private DatabaseHelper db;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = new DatabaseHelper(MainActivity.this);

//        Intent intent = new Intent(MainActivity.this, EventActivity.class);
//        startActivity(intent);

        getBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isTableEmpty = db.isTableEmpty();
               // cursor = db.getUser(getEtLogin.getText().toString());
                String login = getEtLogin.getText().toString();

                if(db.getUser(getEtLogin.getText().toString(),getEtPassword.getText().toString())){
                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_LOGIN, login);
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, EventActivity.class);
                    startActivity(intent);
                }else {
                   // Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                }
                /*if(cursor != null || cursor.isNull(0)) {
                    if (getEtPassword.getText().toString().equals(cursor.getString(0))) {
                        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_LOGIN, login);
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, EventActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        getBtnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
