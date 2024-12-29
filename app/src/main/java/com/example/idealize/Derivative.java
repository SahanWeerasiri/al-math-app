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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.idealize.databinding.ActivityDerivativeBinding;
import com.example.idealize.databinding.InfoPopupBinding;
import com.example.idealize.databinding.MatrixAttrPopupBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Derivative extends AppCompatActivity {

    private ActivityDerivativeBinding binding;
    private PyObject differ_inter_py;
    private ArrayAdapter<String> adapterOrders;
    private static int current_order=-1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Dialog dialog_menu=new Dialog(Derivative.this);
        InfoPopupBinding dialog_binding= InfoPopupBinding.inflate(getLayoutInflater());
        dialog_menu.setContentView(dialog_binding.getRoot());

        PyObject allows=differ_inter_py.callAttr("getAvailable");
        int size=allows.asList().size();
        String print="\t\t\t\tAllows:\n\n";
        for (int i=0;i<size;i++){
            print=print+allows.asList().get(i).toString()+"\n";
        }
        print+="\n\n\t\t\t\tmoreover:\n\nlog() = ln()\nexp(x) = e^x\nasin() = sin inverse";

        dialog_binding.txtMenuInfo.setText(print);
        dialog_menu.show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivityDerivativeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py=Python.getInstance();
        differ_inter_py=py.getModule("derivatives");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");

            }
        });








        adapterOrders=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterOrders.setNotifyOnChange(true);
        String[] orders={"0","1","2","3"};
        adapterOrders.addAll(orders);
        binding.autoCompleteTextDifferOrder.setAdapter(adapterOrders);

        binding.autoCompleteTextDifferOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_order= position;
            }
        });

        binding.btnDiffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String function=binding.txtDifferFunction.getText().toString();
                String point=binding.txtDifferPoint.getText().toString();
                int order=current_order;
                if (function.isEmpty() || point.isEmpty() || order==-1){
                    Toast.makeText(Derivative.this,"Empty Inputs",Toast.LENGTH_LONG).show();
                    binding.txtDifferAnswer.setText("");
                }else{
                    PyObject result=differ_inter_py.callAttr("setFunction",function);
                    if (result.toBoolean()){
                        try{
                            PyObject output=differ_inter_py.callAttr("derivatives",order,Float.parseFloat(point));
                            if (output.asList().get(0).toBoolean()){
                                binding.txtDifferAnswer.setText(output.asList().get(1).toString());
                            }else{
                                if (output.asList().get(1).toString()=="0"){
                                    Toast.makeText(Derivative.this,"Derivative doesn\'t exist",Toast.LENGTH_LONG).show();
                                    binding.txtDifferAnswer.setText("");
                                }else{
                                    Toast.makeText(Derivative.this, output.asList().get(1).toString(),Toast.LENGTH_LONG).show();
                                    binding.txtDifferAnswer.setText("");
                                }
                            }
                        }catch (Exception e) {
                            Toast.makeText(Derivative.this, "Invalid point",Toast.LENGTH_LONG).show();
                            binding.txtDifferAnswer.setText("");
                        }
                    }else{
                        Toast.makeText(Derivative.this,"Invalid Function",Toast.LENGTH_LONG).show();
                        binding.txtDifferAnswer.setText("");
                    }
                }
            }
        });

        binding.btnIntegrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String function=binding.txtIntegrateFunction.getText().toString();
                String start=binding.txtIntegrateStart.getText().toString();
                String end=binding.txtIntegrateEnd.getText().toString();
                if (function.isEmpty() || start.isEmpty() || end.isEmpty()){
                    Toast.makeText(Derivative.this,"Empty Inputs",Toast.LENGTH_LONG).show();
                }else{
                    PyObject result=differ_inter_py.callAttr("setFunction",function);
                    if (result.toBoolean()){
                        try{
                            PyObject output=differ_inter_py.callAttr("integrating",Float.parseFloat(start),Float.parseFloat(end));
                            if (output.asList().get(0).toBoolean()){
                                binding.txtIntegrateAnswer.setText(output.asList().get(1).toString());
                            }else{
                                Toast.makeText(Derivative.this, output.asList().get(1).toString(),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e) {
                            Toast.makeText(Derivative.this, "Invalid start or end"+e,Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Derivative.this,"Invalid Function",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}