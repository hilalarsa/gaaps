package com.pastelstudio.hilal13.gaapp5;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ImageButton imageButton1;
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

        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);

        Intent i = getIntent();
        String uid= i.getStringExtra("uid");
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driver drv1 = dataSnapshot.getValue(driver.class);
                text1.setText(drv1.getNama());
                text2.setText(drv1.getTelepon());
                text3.setText(drv1.getStatus());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(PesanActivity.this, "Value : "+uid, Toast.LENGTH_LONG).show();

imageButton1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        openWhatsApp(imageButton1);
    }
});

        }
    public void openWhatsApp(View view){
        PackageManager pm=getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = ""; // Replace with your own message.

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
