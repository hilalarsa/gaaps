package com.pastelstudio.hilal13.gaapp5;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.pastelstudio.hilal13.gaapp5.model.pekerjaan;

import org.w3c.dom.Text;

public class PesanActivity extends AppCompatActivity {
    TextView text1, text2, text3;
    EditText editText1, editText2;
    ImageButton imageButton1;
    Button button1;
    public driver drv1;
    public pekerjaan pekerjaan1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    String id = myRef.getKey();
    String deskripsi, status, tujuan, noTelp;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("driver");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        text1 = findViewById(R.id.out1);
        text2 = findViewById(R.id.out2);
        text3 = findViewById(R.id.out3);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        imageButton1 = findViewById(R.id.imageButton1);

        Intent i = getIntent();
        final String uid = i.getStringExtra("uid");
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driver drv1 = dataSnapshot.getValue(driver.class);
                text1.setText(drv1.getNama());
                text2.setText(drv1.getTelepon());
                text3.setText(drv1.getStatus());
                noTelp = drv1.getTelepon();
//                if(drv1.getStatus().equals("working"))
//                {
//                    button1.setVisibility(Button.GONE);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.print("Error404! : " + databaseError.getMessage());
                Toast.makeText(PesanActivity.this, "Error! : " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deskripsi = editText1.getText().toString();
                status = "onprogress";
                tujuan = editText2.getText().toString();

                final pekerjaan pekerjaan1 = new pekerjaan(deskripsi, "onprogress", tujuan);
                mDatabase.child(uid).child("pekerjaan").push().setValue(pekerjaan1);
                mDatabase.child(uid).child("status").setValue("working");

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                Toast.makeText(PesanActivity.this, "Driver dalam perjalanan!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+noTelp));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);

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
