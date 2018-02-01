package klippe.dev.azatcp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etLogin)
    EditText getEtLogin;
    @BindView(R.id.etPassword)
    EditText getEtPassword;
    @BindView(R.id.btnLogin)
    Button getBtnLogin;
    @BindView(R.id.btnRegister)
    Button getBtnRegistration;

    private Cursor cursor;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = new DatabaseHelper(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, EventActivity.class);
        startActivity(intent);
        getBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isTableEmpty = db.isTableEmpty();
                cursor = db.getUser(getEtLogin.getText().toString());

                if(cursor != null) {
                    if (getEtPassword.getText().toString().equals(cursor.getString(0))) {
                        Intent intent = new Intent(MainActivity.this, EventActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                }
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
}
