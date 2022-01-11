package com.example.mobilodememodulu;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CustomerPage extends AppCompatActivity {

    private ImageView qrCodeIV;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String androidId;
    //Long status = -1L;
    TextView tvStatus, tvPayStatu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        qrCodeIV = findViewById(R.id.idIVQrcode);
        tvStatus = findViewById(R.id.tvStatus);
        tvPayStatu = findViewById(R.id.tvPayStatu);
        //androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        androidId = "analizDenemeQR";
        qrGenerate();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Device");

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){
                    DeviceModel devicemod = datasnapshot.getValue(DeviceModel.class);
                    if(devicemod.getDeviceId().equals(androidId)) {
                        if (devicemod.getStatus() == 0){
                            tvPayStatu.setBackgroundColor(Color.parseColor("#00FF00"));
                            tvStatus.setText("Başarılı");
                        }

                        if (devicemod.getStatus() == 1){
                            tvPayStatu.setBackgroundColor(Color.parseColor("#FF0000"));
                            tvStatus.setText("Başarısız");
                        }

                        if (devicemod.getStatus() == 2){
                            tvPayStatu.setBackgroundColor(Color.parseColor("#ffff00"));
                            tvStatus.setText("Devam ediyor");
                        }

                        //DeviceModel devicemd = new DeviceModel(androidId, status);
                        //myRef.push().setValue(devicemd);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void qrGenerate(){
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        qrgEncoder = new QRGEncoder(androidId, null, QRGContents.Type.TEXT, dimen);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }
    }
}