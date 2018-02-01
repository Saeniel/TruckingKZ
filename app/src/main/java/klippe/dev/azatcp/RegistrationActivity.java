package klippe.dev.azatcp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    ImageView getImvUserPicture;

    @BindView(R.id.btnRegister)
    Button getBtnRegister;

    private Cursor cursor;
    private DatabaseHelper db;
    Context context;
    String login, password, name;
    Uri uriToImage;

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriToImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriToImage);
                ImageView imageView = findViewById(R.id.imvUserPic);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        context = getApplicationContext();
        uriToImage = null;

        getImvUserPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        getBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getEtLoginRegister.getText().toString().equals("") ||
                   getEtPasswordRegister.getText().toString().equals("") ||
                   getEtNameRegister.getText().toString().equals("") ||
                        uriToImage == null) {
                    Toast.makeText(context, "Fill all of the fields", Toast.LENGTH_LONG).show();
                } else {
                    login = getEtLoginRegister.getText().toString();
                    password = getEtPasswordRegister.getText().toString();
                    name = getEtNameRegister.getText().toString();

                    db = new DatabaseHelper(RegistrationActivity.this);
                    cursor = db.addUser(login, password, name, uriToImage.toString());
                    Toast.makeText(context, "User added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
