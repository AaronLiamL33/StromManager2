package com.example.strommanager.model.user.login;
import com.example.strommanager.model.user.helpers.MD5Converter;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.strommanager.MainActivity;
import com.example.strommanager.retrofit.RetrofitService;
import com.example.strommanager.retrofit.UserApi;
import com.example.strommanager.R;
import com.example.strommanager.model.user.User;
import com.example.strommanager.model.user.signup.SignUp;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    TextInputEditText textInputEditTextUsername,textInputEditTextPassword;

    androidx.appcompat.widget.AppCompatButton buttonLogin;
    TextView textViewSignUp;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextUsername = findViewById(R.id.username);  // mit findViewById wird die ID aus dem XML activity_login.xml aufgerufen und in die textInputEditTextUsername übergeben
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);

        textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); // password wird im textfeld maskiert

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textInputEditTextUsername.getText().toString();
                String password = textInputEditTextPassword.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                    // password wird mit MD5 als hash convertiert
                    String hashedPassword = MD5Converter.convertToMD5(password);

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(hashedPassword);

                    RetrofitService retrofitService = new RetrofitService();
                    UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

                    Call<User> callWithBody = userApi.login(user);
                    callWithBody.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response != null && response.isSuccessful()) {
                                Toast.makeText(Login.this, "Herzlich willkommen!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, "Anmeldung fehlgeschlagen", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Fehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Benutzername und Passwort erforderlich", Toast.LENGTH_LONG).show();
                }


                // Wird nicht mehr verwendet aufgrund, Umstellung auf Spring-Service

               /* if(!username.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData("http://10.0.2.2/LoginRegister/login.php", "POST", field, data); //cmd ipconfig eigene IPv4 http://10.102.10.14/LoginRegister/login.php
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    String[] userInformation = result.split(","); // splittet den string in einem array
                                    if(result.contains("login successfully")){ // login.php in der variable $userInformation vergleicht nach den exakten String "login sucessfully" wenn true -> if körper
                                        Toast.makeText(getApplicationContext(),"Herzlich willkommen " + userInformation[1],Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class); // öffnet MainActivity xml im android wenn login erfolgreich ist
                                        intent.putExtra("username",userInformation[1]);
                                        intent.putExtra("userId",userInformation[2]);
                                        startActivity(intent);

                                    }
                                    else{
                                        if (result.toLowerCase().contains("exception")) {
                                            Toast.makeText(getApplicationContext(),"Please try again later",Toast.LENGTH_SHORT).show();
                                        }
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                    //End ProgressBar (Set visibility to GONE)

                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
                }*/
            }
        });


    }
}
