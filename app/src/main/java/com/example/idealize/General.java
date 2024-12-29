package com.example.idealize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.idealize.databinding.ActivityGeneralBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class General extends AppCompatActivity {
    private ActivityGeneralBinding binding;
    PyObject general_py;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        binding=ActivityGeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");
            }
        });







        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(General.this));
        }

        Python py=Python.getInstance();
        general_py=py.getModule("general");

        binding.btnNcrGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=binding.txtNGeneral.getText().toString();
                String r=binding.txtRGeneral.getText().toString();
                if (n.isEmpty() || r.isEmpty()){
                    Toast.makeText(General.this, "n , r are empty", Toast.LENGTH_SHORT).show();
                    binding.txtNcrNprAnswerGeneral.setText("Answer");
                }else{
                    float answer=(general_py.callAttr("ncr",n,r)).toFloat();
                    if (answer==-1){
                        Toast.makeText(General.this, "n , r should be positive integers", Toast.LENGTH_SHORT).show();
                        binding.txtNcrNprAnswerGeneral.setText("Answer");
                    }else{
                        binding.txtNcrNprAnswerGeneral.setText(String.valueOf(answer));
                    }
                }
            }
        });

        binding.btnNprGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=binding.txtNGeneral.getText().toString();
                String r=binding.txtRGeneral.getText().toString();
                if (n.isEmpty() || r.isEmpty()){
                    Toast.makeText(General.this, "n , r are empty", Toast.LENGTH_SHORT).show();
                    binding.txtNcrNprAnswerGeneral.setText("Answer");
                }else{
                    float answer=(general_py.callAttr("npr",n,r)).toFloat();
                    if (answer==-1){
                        Toast.makeText(General.this, "n , r should be positive integers", Toast.LENGTH_SHORT).show();
                        binding.txtNcrNprAnswerGeneral.setText("Answer");
                    }else{
                        binding.txtNcrNprAnswerGeneral.setText(String.valueOf(answer));
                    }
                }
            }
        });

        binding.btnBiTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=binding.txtBiTermA.getText().toString();
                String operator=binding.txtBiTermOperator.getText().toString();
                String b=binding.txtBiTermB.getText().toString();
                String n=binding.txtBiTermPower.getText().toString();
                if (a.isEmpty() || operator.isEmpty() || b.isEmpty() || n.isEmpty()){
                    Toast.makeText(General.this, "Inputs are empty", Toast.LENGTH_SHORT).show();
                    binding.txtBiTermAnswer.setText("Answer");
                }else{
                    PyObject output=general_py.callAttr("biTermExpansion",a,operator,b,n);
                    if (!output.asList().get(0).toBoolean()){
                        Toast.makeText(General.this, output.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                        binding.txtBiTermAnswer.setText("Answer");
                    }else{
                        binding.txtBiTermAnswer.setText(output.asList().get(1).toString());
                    }
                }
            }
        });

        binding.btnRootsQuadratic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=binding.txtAQuadratic.getText().toString();
                String b=binding.txtBQuadratic.getText().toString();
                String c=binding.txtCQuadratic.getText().toString();
                if (a.isEmpty() || b.isEmpty() || c.isEmpty() ){
                    Toast.makeText(General.this, "Inputs are empty", Toast.LENGTH_SHORT).show();
                    binding.txtAnswerQuadratic.setText("Answer");
                }else{
                    PyObject output=general_py.callAttr("quadraticEquations",a,b,c);
                    if (!output.asList().get(0).toBoolean()){
                        Toast.makeText(General.this, output.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                        binding.txtAnswerQuadratic.setText("Answer");
                    }else{
                        String x1=output.asList().get(1).asList().get(0).toString();
                        String x2=output.asList().get(1).asList().get(1).toString();
                        String msg=output.asList().get(2).toString();
                        binding.txtAnswerQuadratic.setText("x1 = "+x1+"\nx2 = "+x2+"\n"+msg);
                    }
                }
            }
        });
    }
}