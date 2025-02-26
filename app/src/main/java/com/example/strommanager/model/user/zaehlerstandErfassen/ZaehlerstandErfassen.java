package com.example.strommanager.model.user.zaehlerstandErfassen;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.strommanager.retrofit.RetrofitService;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.strommanager.model.zaehlerstand.Zaehlerstand;
import com.example.strommanager.R;
import com.example.strommanager.retrofit.UserApi;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZaehlerstandErfassen extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    EditText editTextDate, editTextBezeichnung, editTextZaehlerstandkw;
    ImageButton imageButtonDatepicker;
    Button buttonSpeichern;
    ProgressBar progressBar;
    UserApi userApi;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaehlerstanderfassen);
            // initiate the date picker and a button
            imageButtonDatepicker = (ImageButton) findViewById(R.id.datepicker);

            editTextDate = (EditText) findViewById(R.id.date);
            editTextBezeichnung = (EditText) findViewById(R.id.bezeichnung);
            editTextZaehlerstandkw =(EditText) findViewById(R.id.zaehlerstandkw);
            buttonSpeichern =(Button) findViewById(R.id.erfassungspeichern);
            progressBar = findViewById(R.id.progress);
        // Initialisierung des UserApi-Objekts
        RetrofitService retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

            String userid = getIntent().getStringExtra("userId");
            String username = getIntent().getStringExtra("username");

            ActionBar ab = getSupportActionBar();
            ab.setTitle("Erfasse hier dein Zählerstand!");
            ab.setSubtitle(username);


            //OnClickListener für die Schaltfläche "Speichern"
        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Daten aus den EditText-Feldern lesen
                String bezeichnung = editTextBezeichnung.getText().toString();
                String date = editTextDate.getText().toString();
                int zaehlerstand = Integer.parseInt(editTextZaehlerstandkw.getText().toString());

                // Zählerstand Objekt wird erzeugt
                Zaehlerstand zaehlerstandObj = new Zaehlerstand();
                zaehlerstandObj.setBezeichnung(bezeichnung);
                zaehlerstandObj.setDate(date);
                zaehlerstandObj.setZaehlerstand(zaehlerstand);

                //  Progressbar anzeigen
                progressBar.setVisibility(View.VISIBLE);

                // Rufe die Methode zum Speichern des Zählerstands auf
                saveZaehlerstand(zaehlerstandObj);
            }
        });


        // perform click event on ImageButton
        imageButtonDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog

                datePickerDialog = new DatePickerDialog(ZaehlerstandErfassen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                editTextDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
    }

    private void saveZaehlerstand(Zaehlerstand zaehlerstandObj) {
        Call<Zaehlerstand> call = userApi.saveZaehlerstand(zaehlerstandObj);
        call.enqueue(new Callback<Zaehlerstand>() {
            @Override
            public void onResponse(Call<Zaehlerstand> call, Response<Zaehlerstand> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(ZaehlerstandErfassen.this, "Zählerstand erfolgreich gespeichert!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ZaehlerstandErfassen.this, "Fehler beim Speichern des Zählerstands: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Zaehlerstand> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ZaehlerstandErfassen.this, "Fehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
