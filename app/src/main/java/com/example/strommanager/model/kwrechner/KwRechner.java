package com.example.strommanager.model.kwrechner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.strommanager.R;

import java.text.DecimalFormat;

public class KwRechner extends AppCompatActivity {

    private EditText verbrauchEditText;
    private Spinner stundenSpinner;
    private Spinner tageSpinner;
    private TextView kostenTextView;
    private TextView verbrauchTextView;
    private TextView gesamtKostenView;

    private double strompreisProKWh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kw_rechner);

        verbrauchEditText = findViewById(R.id.editTextNumber);
        stundenSpinner = findViewById(R.id.spinnerStunden);
        tageSpinner = findViewById(R.id.spinnerTage);
        kostenTextView = findViewById(R.id.textViewPreis);
        verbrauchTextView = findViewById(R.id.textViewGesamtverbrauch);
        gesamtKostenView = findViewById(R.id.textViewGesamtkosten);

        kostenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        // Arrays mit den Stunden- und Tage-Optionen
        String[] stundenOptions = {"1", "2", "3", "4", "5", "6", "7"};
        String[] tageOptions = {"1","2","3","4","5","6","7","8","9","10",
                "11", "12", "13", "14", "15", "16","17",
                "18","19","20","21","22","23","24"};

        //Array-Adapter für Spinner
        ArrayAdapter<String> stundenAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stundenOptions);
        ArrayAdapter<String> tageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tageOptions);

        //Layout für die Dropdown-Liste
        stundenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Adapter für die Spinner
        stundenSpinner.setAdapter(stundenAdapter);
        tageSpinner.setAdapter(tageAdapter);

        // Listener für Verbrauch EditText, um die Kosten neu zu berechnen, wenn der Verbrauch geändert wird
        verbrauchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculatePrice();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Listener für Stunden Spinner, um die Kosten neu zu berechnen, wenn die Stunden geändert werden
        stundenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Listener für Tage Spinner, um die Kosten neu zu berechnen, wenn die Tage geändert werden
        tageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void calculatePrice() {
        String verbrauch = verbrauchEditText.getText().toString();

        if (!TextUtils.isEmpty(verbrauch)) {
            // Eingabe von Watt in Kilowatt umwandeln (durch 1000 teilen)
            double verbrauchInWatt = Double.parseDouble(verbrauch);
            double verbrauchInKilowatt = verbrauchInWatt / 1000.0;

            double betriebsstunden = Double.parseDouble(stundenSpinner.getSelectedItem().toString());
            double betriebstage = Double.parseDouble(tageSpinner.getSelectedItem().toString());

            // Berechne die Gesamtkosten
            double gesamtVerbrauch = verbrauchInKilowatt * betriebsstunden * betriebstage;
            double gesamtkosten = gesamtVerbrauch * strompreisProKWh;

            // Zeige den Gesamtverbrauch im TextView an
            verbrauchTextView.setText(String.format("%.2f", gesamtVerbrauch) + " kW");

            // Zeige die Gesamtkosten im TextView an
            gesamtKostenView.setText(String.format("%.2f", gesamtkosten) + " Euro");
        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Strompreis pro kWh eingeben");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                if (!TextUtils.isEmpty(userInput)) {
                    strompreisProKWh = Double.parseDouble(userInput);
                    kostenTextView.setText(String.format("%.2f", strompreisProKWh) + " Euro");
                    calculatePrice(); // Aktualisiere die Kosten, nachdem der Strompreis geändert wurde
                }
            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
