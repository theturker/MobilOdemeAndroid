package com.example.mobilodememodulu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCashier = findViewById(R.id.btnCashier);
        Button btnCustomer = findViewById(R.id.btnCustomer);

        btnCashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cashierIntent = new Intent(MainActivity.this, CashierPage.class);
                startActivity(cashierIntent);
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent customerIntent = new Intent(MainActivity.this, CustomerPage.class);
                startActivity(customerIntent);
            }
        });
    }

}