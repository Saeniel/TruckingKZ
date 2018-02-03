package klippe.dev.azatcp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saeniel on 31.01.2018.
 */

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.etLoginRegister)
    EditText getEtLoginRegister;

    @BindView(R.id.etPasswordRegister)
    EditText getEtPasswordRegister;

    @BindView(R.id.etNameRegister)
    EditText getEtNameRegister;

    @BindView(R.id.imvUserPic)
    CircleImageView getImvUserPicture;

    @BindView(R.id.btnRegister)
    Button getBtnRegister;

    private Cursor cursor;
    private DatabaseHelper db;
    Context context;
    String login, password, name;
    Uri uriToImage;
    String imagePath;

    private int LOAD_IMAGE_RESULTS = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            getImvUserPicture.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            cursor.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        context = getApplicationContext();
        uriToImage = null;
        imagePath = " ";

        getImvUserPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
            }
        });

        getBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getEtLoginRegister.getText().toString().equals("") ||
                        getEtPasswordRegister.getText().toString().equals("") ||
                        getEtNameRegister.getText().toString().equals("") ||
                        imagePath.equals("")) {
                    Toast.makeText(context, "Fill all of the fields", Toast.LENGTH_LONG).show();
                } else {
                    login = getEtLoginRegister.getText().toString();
                    password = getEtPasswordRegister.getText().toString();
                    name = getEtNameRegister.getText().toString();

                    db = new DatabaseHelper(RegistrationActivity.this);

                    try {
                        cursor = db.addUser(login, password, name, imagePath);
                        Toast.makeText(context, "User added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "User already exists", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}