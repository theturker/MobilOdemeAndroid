package com.example.mobilodememodulu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.opengl.EGLObjectHandle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class CashierPage extends AppCompatActivity {

    Button btnSelect;
    RelativeLayout rl;
    NumberPicker np;
    Long status = -1L;
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_page);

        btnSelect = findViewById(R.id.btnSelect);

        rl = findViewById(R.id.rl);
        np = findViewById(R.id.np);

        qrCamera();

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Device");
                myRef.child("/-Mt9peVHv45LQ4ZHZnPS/status").setValue(status);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){
                            DeviceModel devicemod = datasnapshot.getValue(DeviceModel.class);
                            if(!devicemod.getDeviceId().equals(deviceID)) {
                                DeviceModel devicemd = new DeviceModel(deviceID, status);
                                myRef.push().setValue(devicemd);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Device");
                myRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){
                            DeviceModel devicemod = datasnapshot.getValue(DeviceModel.class);

                            if(devicemod.getDeviceId().equals(intentResult.getContents())) {
                                rl.setVisibility(View.VISIBLE);
                                deviceID = intentResult.getContents();
                                selectedNumber();
                            }
                        else
                            Toast.makeText(getBaseContext(), "Okutulan barkod = " + intentResult.getContents() + "Firebase üzerindeki barkodlar = " + deviceID , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void selectedNumber() {
        np.setMinValue(0);
        np.setMaxValue(2);
        np.setDisplayedValues( new String[] {  "Başarılı", "Başarısız", "Devam ediyor" } );
        np.setWrapSelectorWheel(true);

        status = Long.valueOf(np.getValue());//Picker haraket halinde olmadığında value manuel alındı.
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                status = Long.valueOf(newVal);
            }
        });

    }

    private void qrCamera(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(CashierPage.this);
        intentIntegrator.setPrompt("Müşteri ekranındaki QR kodu okutunuz.");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }
}