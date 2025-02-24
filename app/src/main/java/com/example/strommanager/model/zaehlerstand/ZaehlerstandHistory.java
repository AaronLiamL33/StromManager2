package com.example.strommanager.model.zaehlerstand;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.strommanager.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;




public class ZaehlerstandHistory extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener
{

    TextView totalConsumption;



    // create array of Strings
    // and store name of courses
    String[] country = { "Stromzähler 1", "Stromzähler 2", "Stromzähler 3", "Stromzähler 4", "Stromzähler 5"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaehlerstandhistory);

        totalConsumption = findViewById(R.id.totalConsumption);


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //// es müssen werte aus der DB tabelle user_zaehlerstand wo row zaehlerstand abgefragt werden anschließend hier in einer variable übergeben werden///

        int test = 3456;
        int test2 = 3730;
        int test3 = 4112;
        int test4 = 4326;
        int test5 = 4634;


        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> users = new ArrayList<>();
        users.add(new BarEntry(1, test));
        users.add(new BarEntry(2, test2));
        users.add(new BarEntry(3, test3));
        users.add(new BarEntry(4, test4));
        users.add(new BarEntry(5, test5));


        BarDataSet barDataSet = new BarDataSet(users, "Verbrauch in Watt");
        barDataSet.setColors(ColorTemplate.rgb("#4CAF50"));
        barDataSet.setValueTextColor(Color.BLUE);
        barDataSet.setValueTextSize(10f);


        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.animateY(2000);

        String sum = String.valueOf(test + test2 + test3 + test4 + test5);
        totalConsumption.setText("Verbrauch insgesamt " + sum + "kW");




        String userInformation = getIntent().getStringExtra("username");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Zählerstandhistorie von " + userInformation);


    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
