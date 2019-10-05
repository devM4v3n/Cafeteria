package com.canteen.net.cafeteria;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.canteen.net.cafeteria.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void SignUp(View view) {

        final EditText etName = findViewById(R.id.etName);
        final EditText etPhone = findViewById(R.id.etPhone);
        final EditText etPass = findViewById(R.id.etPass);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = database.getReference("User");

            final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
            mDialog.setMessage("Please Wait...");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(etPhone.getText().toString()).exists()) {
                        mDialog.dismiss();
                        Toast.makeText(SignUp.this, "Phone Number already Registered !", Toast.LENGTH_SHORT).show();
                    } else {
                        mDialog.dismiss();
                        User user = new User(etName.getText().toString(), etPass.getText().toString());
                        table_user.child(etPhone.getText().toString()).setValue(user);
                        Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignUp.this, "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
}
