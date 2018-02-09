package com.pastelstudio.hilal13.gaapp5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pastelstudio.hilal13.gaapp5.adapter.driverList;
import com.pastelstudio.hilal13.gaapp5.model.driver;

import java.util.ArrayList;
import java.util.List;

public class ListDriver extends AppCompatActivity {
    ListView listViewDriv;
    List<driver> drv1;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_driver);

        listViewDriv = (ListView) findViewById(R.id.listViewDriv);
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        drv1 = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                drv1.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    driver pkj = postSnapshot.getValue(driver.class);
                    //adding artist to the list
                    drv1.add(pkj);
                }
                driverList artistAdapter = new driverList(ListDriver.this, drv1);
                //attaching adapter to the listview
                listViewDriv.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
