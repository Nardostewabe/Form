package com.example.form;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.form.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Spinner spinner;
    EditText datePicker;
    TextView formTitle;
    int pickImageRequest = 100;
    Uri selectedImageUri;
    String name,email,phone,date;
    String gender,country;
    ProgressBar loading;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
        Toast.makeText(this,getString(R.string.success),Toast.LENGTH_SHORT).show();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spinner = binding.country;
        loading = binding.loading;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.country)
        );

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            spinner.setAdapter(adapter);
            loading.setVisibility(View.GONE);
        }, 1000);



        datePicker = binding.dateOfBirth;
        datePicker.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view,selectedYear,selectedMonth,selectedDay)->{
                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                datePicker.setText(selectedDate);
            },
                    year, month, day);
            datePickerDialog.show();
        });


        if(savedInstanceState!=null) {
            binding.userName.setText(savedInstanceState.getString("name"));
            binding.email.setText(savedInstanceState.getString("email"));
            binding.phNumber.setText(savedInstanceState.getString("phoneNumber"));
            binding.dateOfBirth.setText(savedInstanceState.getString("date"));
            int gender = savedInstanceState.getInt("gender");
            if(gender!= -1){
                binding.genderGroup.check(gender);
            }

            int country = savedInstanceState.getInt("country");
            spinner.setSelection(country);
        }

        binding.picUpload.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent,pickImageRequest);
        });

        binding.signUp.setOnClickListener(v->{
            name = binding.userName.getText().toString();
            email = binding.email.getText().toString();
            phone = binding.phNumber.getText().toString();
            date = binding.dateOfBirth.getText().toString();

            int selectedGenderId = binding.genderGroup.getCheckedRadioButtonId();
            RadioButton genderButton = findViewById(selectedGenderId);
            gender = (genderButton != null) ? genderButton.getText().toString() : "Not specified";

            country = spinner.getSelectedItem().toString();

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("email",email);
            intent.putExtra("phone",phone);
            intent.putExtra("date",date);
            intent.putExtra("gender",gender);
            intent.putExtra("country",country);
            if(selectedImageUri!=null){
                intent.putExtra("imageUri",selectedImageUri.toString());
            }
            launcher.launch(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pickImageRequest && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.imageName.setText(selectedImageUri.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String currentLang = LocalHelper.getLanguage(this);
        String newLang = currentLang.equals("en") ? "amh" : "en";
        LocalHelper.setLocale(this, newLang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",binding.userName.getText().toString());
        outState.putString("email",binding.email.getText().toString());
        outState.putString("phoneNumber",binding.phNumber.getText().toString());
        outState.putString("date",binding.dateOfBirth.getText().toString());

        int gender = binding.genderGroup.getCheckedRadioButtonId();
        outState.putInt("gender",gender);

        int country = spinner.getSelectedItemPosition();
        outState.putInt("country",country);
    }

}