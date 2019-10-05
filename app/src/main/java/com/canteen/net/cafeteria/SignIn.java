package com.canteen.net.cafeteria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.canteen.net.cafeteria.Common.Common;
import com.canteen.net.cafeteria.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void SignIn(View view) {

        final EditText etphone = (EditText)findViewById(R.id.etPhone);
        final EditText etpass  = (EditText)findViewById(R.id.etPass);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Loading ...");
        mDialog.show();

        if(!etphone.getText().toString().equals("")) {

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(etphone.getText().toString()).exists()) {

                        mDialog.dismiss();

                        User user = dataSnapshot.child(etphone.getText().toString()).getValue(User.class);

                        user.setPhone(etphone.getText().toString());

                        if (user.getPassword().equals(etpass.getText().toString())) {
                            Intent intent = new Intent(SignIn.this, home.class);
                            Common.currentUser = user;
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(SignIn.this, "User not exists in Database!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
