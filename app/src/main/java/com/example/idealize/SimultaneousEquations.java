package com.example.idealize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.idealize.databinding.ActivitySimultaneousEquationsBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SimultaneousEquations extends AppCompatActivity {

    private ActivitySimultaneousEquationsBinding binding;
    private PyObject SE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivitySimultaneousEquationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");

            }
        });








        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py=Python.getInstance();
        SE=py.getModule("simultaneous");

        String text="EX:-\nx + y + z = 7\ny + 3z = 1\n-x -2y + z = 3\n\nInsert these equations only using coefficients and a space between ech other like below.\n\n1 1 1 7\n0 1 3 1\n-1 -2 1 3\n";
        binding.txtSeExample.setText(text);

        binding.btnSeSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=binding.txtSeValues.getText().toString();
                if (data.isEmpty()){
                    Toast.makeText(SimultaneousEquations.this,"Empty equations" , Toast.LENGTH_SHORT).show();
                    binding.txtSeAnswer.setText("");
                }else{


                PyObject output=SE.callAttr("main",data);
                if (output.asList().get(0).toBoolean()){
                    binding.txtSeAnswer.setText(output.asList().get(1).toString());
                }else{
                    String msg=output.asList().get(1).toString();
                    Toast.makeText(SimultaneousEquations.this,msg , Toast.LENGTH_SHORT).show();
                    binding.txtSeAnswer.setText(msg);
                }
                }
            }
        });
    }
}