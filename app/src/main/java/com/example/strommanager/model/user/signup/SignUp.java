package com.example.strommanager.model.user.signup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.strommanager.model.user.helpers.MD5Converter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.strommanager.model.user.User;
import com.example.strommanager.R;
import com.example.strommanager.model.user.login.Login;
import com.google.android.material.textfield.TextInputEditText;
import com.example.strommanager.retrofit.RetrofitService;
import com.example.strommanager.retrofit.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextName, textInputEditTextUsername, textInputEditTextPassword, textInputEditTextEmail,textInputEditText;
    androidx.appcompat.widget.AppCompatButton buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // UI elemente mit findView über die ID des UI elements referenzieren
        textInputEditTextName = findViewById(R.id.name);
        textInputEditText = findViewById(R.id.surname);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextEmail = findViewById(R.id.email);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        RetrofitService retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Benutzereingaben aus den TextInputEditText-Feldern lesen
                String name = textInputEditTextName.getText().toString();
                String surname = textInputEditText.getText().toString();
                String username = textInputEditTextUsername.getText().toString();
                String password = textInputEditTextPassword.getText().toString();
                String hashedPassword = MD5Converter.convertToMD5(password);
                String email = textInputEditTextEmail.getText().toString();
                // Erstelle ein User-Objekt mit den eingegebenen Daten
                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setUsername(username);
                user.setPassword(hashedPassword);
                user.setEmail(email);

                // Zeige die ProgressBar an
                progressBar.setVisibility(View.VISIBLE);

                // Führe die Benutzerregistrierung durch
                registerUser(user);
            }
        });
    }

    private void registerUser(User user) {
        Call<User> call = userApi.save(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Registrierung erfolgreich!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Registrierung fehlgeschlagen: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUp.this, "Fehler: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}