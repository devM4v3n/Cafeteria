package com.canteen.net.cafeteria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Signup(View view) {
        Intent signUp = new Intent(this, SignUp.class);
        startActivity(signUp);
    }

    public void Signin(View view) {
        Intent signIn = new Intent(this, SignIn.class);
        startActivity(signIn);
    }
}
