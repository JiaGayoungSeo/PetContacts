package com.cornez.petcontacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;


public class EditActivity extends Activity {
    EditText etName;
    EditText etDetail;
    EditText etPhone;
    Button button;
    ImageView imgPet;
    Drawable noPetImage;
    Uri defaultImage = Uri.parse("drawable-xxhdpi/none.png");

    Intent intent;
    Bundle bundle;

    Uri petUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imgPet = findViewById(R.id.img_pet);
        noPetImage = imgPet.getDrawable();
        etName = findViewById(R.id.et_name);
        etDetail = findViewById(R.id.et_details);
        etPhone = findViewById(R.id.et_phone);
        button = findViewById(R.id.btnEdit);

        //Get data from MainActivity
        intent = getIntent();
        bundle = intent.getExtras();

        //Set EditText box with the current data
        etName.setText(bundle.getString("name"));
        etDetail.setText(bundle.getString("details"));
        etPhone.setText(bundle.getString("phone"));

        petUri = Uri.parse(bundle.getString("photoUri"));
        imgPet.setImageURI(petUri);

        //Set onClickListener when the user hit the image to change it to another one
        imgPet.setOnClickListener(getPhotoFromGallery);

        //When the user hit Update button, it gets back to MainActivity passing new data with the result code(RESULT_OK) and finishes this activity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName = etName.getText().toString();
                String mDetails = etDetail.getText().toString();
                String mPhone = etPhone.getText().toString();

                Intent newIntent = new Intent(getApplicationContext(),MainActivity.class);

                Bundle newBundle = new Bundle();
                newBundle.putInt("mId",bundle.getInt("id"));

                newBundle.putString("mName", mName);
                newBundle.putString("mDetails",mDetails);
                newBundle.putString("mPhone",mPhone);
                newBundle.putString("mPhoto",petUri.toString());


                newIntent.putExtras(newBundle);
                setResult(RESULT_OK,newIntent);
                finish();
            }
        });
    }

    //Set up onClickListener that allows the user to change its photo
    private final View.OnClickListener getPhotoFromGallery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);

            startActivityForResult(
                    Intent.createChooser(intent,
                            "Select Contact Image"), 1);
        }
    };

    public void onActivityResult(int reqCode,
                                 int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                defaultImage = data.getData();
                imgPet.setImageURI(data.getData());
                petUri = data.getData();
            }
        }
    }
}
