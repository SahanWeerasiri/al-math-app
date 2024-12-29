package com.example.idealize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.idealize.databinding.ActivityLimitsBinding;
import com.example.idealize.databinding.InfoPopupBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class Limits extends AppCompatActivity {

    private ActivityLimitsBinding binding;
    private PyObject limit_python;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Dialog dialog_menu=new Dialog(Limits.this);
        InfoPopupBinding dialog_binding= InfoPopupBinding.inflate(getLayoutInflater());
        dialog_menu.setContentView(dialog_binding.getRoot());

        PyObject allows=limit_python.callAttr("getAllows");
        String out="Allowed operations and symbols\n\nx\nall integers\n";
        int len=allows.asList().size();
        for(int i=0;i<len;i++){
            out=out+String.valueOf(allows.asList().get(i).toString())+"\n";
        }
        dialog_binding.txtMenuInfo.setText(out);
        dialog_menu.show();
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivityLimitsBinding.inflate(getLayoutInflater());
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
        limit_python=py.getModule("limits");



        binding.btnLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String function,point;
                function=binding.txtLimitFunction.getText().toString();
                point=binding.txtLimitPoint.getText().toString();
                if (!function.isEmpty() && !point.isEmpty()){
                    String value;
                    int state;
                    PyObject result=limit_python.callAttr("getLimit",function,point);
                    state=result.asList().get(0).toInt();
                    value=result.asList().get(1).toString();
                    String output;
                    if (state==0){
                        output="Error in function";
                    }else if (state==1){
                        output="limit is "+value+" at x="+point ;
                    }else if (state==2){
                        output="No limit at x="+point;
                    }else if (state==3){
                        output="Invalid function";
                    }else{
                        output="Invalid point";
                    }
                    binding.txtLimitAnswer.setText(output);
                }else{
                    binding.txtLimitAnswer.setText("");
                    Toast.makeText(Limits.this,"Type the function and the point",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}