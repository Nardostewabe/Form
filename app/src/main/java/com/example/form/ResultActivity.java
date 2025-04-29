package com.example.form;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.form.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        Intent intent = getIntent();
        bind.name.setText(getString(R.string.name)+" "+intent.getStringExtra("name"));
        bind.email.setText(getString(R.string.email)+ " "+intent.getStringExtra("email"));
        bind.phone.setText(getString(R.string.phNum)+" "+intent.getStringExtra("phone"));
        bind.gender.setText(getString(R.string.gender)+" "+ intent.getStringExtra("gender"));
        bind.date.setText(getString(R.string.dateOfBirth)+" "+ intent.getStringExtra("date"));
        bind.country.setText(getString(R.string.enterCountry)+" "+ intent.getStringExtra("country"));

        Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
        if(imageUri!=null) {
            bind.myPhoto.setImageURI(imageUri);
        }
        else{
            bind.myPhoto.setImageResource(R.drawable.baseline_person_24);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase));
    }
}