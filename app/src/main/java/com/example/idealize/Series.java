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
import com.example.idealize.databinding.ActivitySeriesBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Series extends AppCompatActivity {

    private ActivitySeriesBinding binding;
    private PyObject series_py;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivitySeriesBinding.inflate(getLayoutInflater());
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
        series_py=py.getModule("series");

        binding.btnSeriesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                    return False,1,'number of first terms should be more than 2'
        state,n=validationOfData(n)
        if not state:
            return False,2,'Invalid data'
        if isSymmetric(n):
            a=n[0]
            d=n[1]-n[0]
            ur=str(a)+'+'+str(d)+'*(r-1)'
            sn='n/2*(2*'+str(a)+'+(n-1)*'+str(d)+')'
            return True,3,ur,'Symmetric',a,d,sn
        elif isGeometric(n):
            a=n[0]
            r=n[1]/n[0]
            ur=str(a)+'*'+str(r)+'**(r-1)'
            sn=str(a)+'*(1-'+str(r)+'**n)/(1-'+str(r)+')'
            return True,4,ur,'Geometric',a,r,sn
        else:
            ur=''
            sn=''
            return False,5,'This is not Symmetric or Geometric. Insert the Ur.'
                */
                String data=binding.txtSeriesFunction.getText().toString();
                if(data.isEmpty()){
                    Toast.makeText(Series.this, "Empty data", Toast.LENGTH_SHORT).show();
                    clear();
                }else{
                    PyObject rst=series_py.callAttr("createUrSn",data);
                    if(!rst.asList().get(0).toBoolean()){
                        String msg=rst.asList().get(1).toString();
                        Toast.makeText(Series.this, msg, Toast.LENGTH_SHORT).show();
                        clear();
                    }else{
                        String details="";
                        String ur,type,a,d,sn;
                        int flag;
                        flag=rst.asList().get(1).toInt();
                        ur=rst.asList().get(2).toString();
                        type=rst.asList().get(3).toString();
                        if (flag==3 || flag==4){
                            a=rst.asList().get(4).toString();
                            d=rst.asList().get(5).toString();
                            sn=rst.asList().get(6).toString();
                            details=details+"Ur = "+ur+"\na = "+a+"\n";
                            if(flag==3){
                                details=details+"d = "+d+"\n";
                            }else{
                                details=details+"r = "+d+"\n";
                            }
                            details=details+"Sn = "+sn+"\nType = "+type;
                        }else{
                            details=details+"Ur = "+ur+"\nType = "+type;
                        }
                        binding.txtSeriesDetails.setText(details);

                    }
                }
            }
        });

        binding.btnSeriesTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term=binding.txtSeriesTerm.getText().toString();
                if(term.isEmpty()){
                    Toast.makeText(Series.this, "Empty data", Toast.LENGTH_SHORT).show();
                    binding.txtSeriesTermAnswer.setText("");
                }else{
                    if (term=="0"){
                        Toast.makeText(Series.this, "r should be positive integer", Toast.LENGTH_SHORT).show();
                        binding.txtSeriesTermAnswer.setText("");
                    }else{
                        PyObject rst=series_py.callAttr("getTerm",term);
                        if(rst.asList().get(0).toBoolean()){
                            binding.txtSeriesTermAnswer.setText(rst.asList().get(1).toString());
                        }else{
                            Toast.makeText(Series.this, "Empty Ur", Toast.LENGTH_SHORT).show();
                            binding.txtSeriesTermAnswer.setText("");
                        }
                    }

                }
            }
        });

        binding.btnSeriesFindSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=binding.txtSeriesN.getText().toString();
                if(n.isEmpty()){
                    Toast.makeText(Series.this, "Empty data", Toast.LENGTH_SHORT).show();
                    binding.txtSeriesTermAnswer.setText("");
                }else{
                   /* int temp_n=0;
                    if (n=="-"){
                        Toast.makeText(Series.this, n, Toast.LENGTH_SHORT).show();
                       // temp_n=Integer.parseInt(n);
                    }else{
                        temp_n=1;
                    }*/

                    PyObject rst=series_py.callAttr("seriesValue",n);
                    String show="";
                    if(rst.asList().get(0).toBoolean()){
                        String sum=rst.asList().get(1).toString();

                        try{
                            Float.parseFloat(n);
                        }catch (Exception e){
                            n="infinite";
                        }

                        show=show+"Sum of "+n+" terms = "+sum+"\n";
                        PyObject converge=rst.asList().get(2);
                            String msg=converge.asList().get(2).toString();
                            show=show+msg;


                        binding.txtSeriesSumAnswer.setText(show);
                    }else{
                        if (rst.asList().get(1).toInt()==1){
                            Toast.makeText(Series.this, "r should be positive integer", Toast.LENGTH_SHORT).show();
                        }else{
                        Toast.makeText(Series.this, "Empty Ur", Toast.LENGTH_SHORT).show();
                        }
                        binding.txtSeriesSumAnswer.setText("");
                    }
                }

            }
        });

    }
    private void clear(){
        binding.txtSeriesDetails.setText("");
        binding.txtSeriesTerm.setText("");
        binding.txtSeriesTermAnswer.setText("");
        binding.txtSeriesSumAnswer.setText("");binding.txtSeriesN.setText("");
    }
}