package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    String email, password;
    UserLocalStore userLocalStore;
    TextView textViewEmail, textViewPassowrd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLocalStore = new UserLocalStore(this);

        TextView textViewNewToISpend = (TextView) findViewById(R.id.textViewNewToISpend);
        textViewNewToISpend.setPaintFlags(textViewNewToISpend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewNewToISpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
            }
        });

        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewPassowrd = (TextView) findViewById(R.id.textViewPassword);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if (validate()) {
                    User user = new User(email, password);
                    DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
                    databaseHelper.validateLogin(user);
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewEmail.setTextColor(Color.parseColor("#689f38"));
                    textViewEmail.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewEmail.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewEmail.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewPassowrd.setTextColor(Color.parseColor("#689f38"));
                    textViewPassowrd.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewPassowrd.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewPassowrd.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
    }

    public boolean validate() {
        String email = editTextEmail.getText().toString();
        if(!isValidEmail(email)) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return false;
        }

        String password = editTextPassword.getText().toString();
        if(!isValidPassword(password)) {
            editTextPassword.setError("Password should have at least 6 characters");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    public void showErrorMessage()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityLogin.this);
        dialogBuilder.setMessage("The email and password you entered don't match.");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    public void loginUser(User returnedUser)
    {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(ActivityLogin.this, ActivityWelcome.class));
    }
}
