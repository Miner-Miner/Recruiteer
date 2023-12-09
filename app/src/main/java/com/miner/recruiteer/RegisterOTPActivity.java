package com.miner.recruiteer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;

import java.util.Random;

public class RegisterOTPActivity extends AppCompatActivity {

    PinView pinView;
    TextView tvOtp,tvOtpRe;
    Button btnOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otpactivity);

        pinView = findViewById(R.id.pinView);
        tvOtp = findViewById(R.id.tvOtp);
        tvOtpRe = findViewById(R.id.tvOtpRe);
        btnOtp = findViewById(R.id.btnOtp);

        Random rand = new Random();
        final int[] Otp = {0};

        while (Otp[0] <1000){
            Otp[0] = rand.nextInt(9999);
        }

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvOtp.setText("Otp avaliable " + millisUntilFinished / 1000 +" seconds remaining.");
                tvOtpRe.setVisibility(View.GONE);
            }

            public void onFinish() {
                tvOtp.setText("OTP not recieved. ");
                Otp[0] = 0;
                tvOtpRe.setVisibility(View.VISIBLE);
            }
        }.start();

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinView.getText().toString().equals(String.valueOf(Otp[0]))){
                    Toast.makeText(RegisterOTPActivity.this, "OTP confirmed.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterOTPActivity.this, String.valueOf(Otp[0]), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvOtpRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Otp[0] = rand.nextInt(9999);
                while (Otp[0] <1000){
                    Otp[0] = rand.nextInt(9999);
                }
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tvOtp.setText("Otp avaliable " + millisUntilFinished / 1000 +" seconds remaining.");
                        tvOtpRe.setVisibility(View.GONE);
                    }

                    public void onFinish() {
                        tvOtp.setText("OTP not recieved. ");
                        Otp[0] = 0;
                        tvOtpRe.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        });
    }
}