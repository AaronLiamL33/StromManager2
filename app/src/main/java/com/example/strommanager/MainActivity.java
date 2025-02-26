package com.example.strommanager;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.strommanager.model.kwrechner.KwRechner;
import com.example.strommanager.model.user.zaehlerstandErfassen.ZaehlerstandErfassen;
import com.example.strommanager.model.user.login.Login;
import com.example.strommanager.model.zaehlerstand.ZaehlerstandHistory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView; //objekt  element AdView
    ImageView avatarPic; //objekt element imageView
    FloatingActionButton takePicture; // objekt FloatingActionButton
    ActivityResultLauncher <Intent> activityResultLauncher; // objekt ActivityResultLauncher
    TextView avatarname;
    Button signOut; // wird nicht aufgerufen



///////////////////////   ACTIONBAR /////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        ///////// SIGNOUT ////////////
        if (id == R.id.buttonSignOut) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();

            Toast.makeText(MainActivity.this,"Logged Out",Toast.LENGTH_LONG).show();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////// MAIN OF DASHBOARD ////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //values aus einer anderen Activity holen (login actitivy) value->key
        String userInformation = getIntent().getStringExtra("username");
        String userid = getIntent().getStringExtra("userId");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Dashbord");
        ab.setSubtitle("Willkommen!");



        takePicture = findViewById(R.id.floatingActionButtonCamera);
        avatarPic = findViewById(R.id.avatarPicture);
        avatarname = findViewById(R.id.usernamedisplay);
        avatarname.setText(userInformation);



        ////////////// CAMERA SNAPSHOT //////////////////

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() !=null){
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    avatarPic.setImageBitmap(bitmap);
                }
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    activityResultLauncher.launch(intent);
                }else {
                    Toast.makeText(MainActivity.this,"Camera not Supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        //////// BARCHART //////////////

        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> users = new ArrayList<>();
        users.add(new BarEntry(1,3456));
        users.add(new BarEntry(2,3730));
        users.add(new BarEntry(3,4112));
        users.add(new BarEntry(4,4326));
        users.add(new BarEntry(5,4634));
        users.add(new BarEntry(6,4977));
        users.add(new BarEntry(7,5287));
        users.add(new BarEntry(8,5580));



        BarDataSet barDataSet = new BarDataSet(users, "users");
        barDataSet.setColors(ColorTemplate.rgb("#4CAF50"));
        barDataSet.setValueTextColor(Color.BLUE);
        barDataSet.setValueTextSize(10f);


        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Kilowatt Test");
        barChart.animateY(2000);

        /////// GOOGLE ADS //////////

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {


            }
        });


        ///////// Zählererfassen Button /////////////

        Button zaehler = findViewById(R.id.buttonZaehler);

        zaehler.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                String userInformation = getIntent().getStringExtra("username");
                String userid = getIntent().getStringExtra("userId");
                Intent intent = new Intent(MainActivity.this, ZaehlerstandErfassen.class);
                startActivity(intent);
            }
        });

        ///////// Zählerhistory Button /////////////


        Button zaehlerstandhistory = (Button)findViewById((R.id.zaehlerstandhistory));

        zaehlerstandhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ZaehlerstandHistory.class);
                intent.putExtra("userId",userid);
                intent.putExtra("username",userInformation);
                startActivity(intent);

            }
        });


        ///// kw Rechner /////

        Button kwRechner = (Button) findViewById(R.id.buttonKwRechner);

        kwRechner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getApplicationContext(), KwRechner.class);
                startActivity(intent);
           }
        });

    }
}