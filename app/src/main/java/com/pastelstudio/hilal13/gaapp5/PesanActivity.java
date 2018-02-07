package com.pastelstudio.hilal13.gaapp5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pastelstudio.hilal13.gaapp5.model.driver;

import org.w3c.dom.Text;

public class PesanActivity extends AppCompatActivity {
    TextView text1, text2, text3;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("driver");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        text1 = (TextView) findViewById(R.id.out1);
        text2 = (TextView) findViewById(R.id.out2);
        text3 = (TextView) findViewById(R.id.out3);


        Intent i = getIntent();
        String uid= i.getStringExtra("uid");
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driver drv1 = dataSnapshot.getValue(driver.class);
                text1.setText(drv1.getNama());
                text2.setText(drv1.getStatus());
                text3.setText(drv1.getTelepon());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(PesanActivity.this, "Value : "+uid, Toast.LENGTH_LONG).show();

    }
}
