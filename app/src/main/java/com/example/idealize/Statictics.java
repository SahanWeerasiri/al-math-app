package com.example.idealize;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.idealize.databinding.ActivityStaticticsBinding;
import com.example.idealize.databinding.MatrixAttrPopupBinding;
import com.example.idealize.databinding.StatAttrPopupBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;

public class Statictics extends AppCompatActivity {
    private ActivityStaticticsBinding binding;
    private StatAttrPopupBinding dialog_binding;
    private PyObject stat_py;

    private ArrayAdapter<String> adapterCalculations,adapterDataSets,adapterSave;

    private static HashMap<String,PyObject> data;
    private static String SAVE_TYPES[]={"Value and Frequency","Interval (Start,End) and Frequency","Values"};
    private static String CAL_TYPES[]={"Combination","Unit Transformation","Percentages","Z Score"};
    private static int current_save_type=-1,current_cal_type=-1;
    private static String current_cal_additional="",current_data1="",current_data2="",current_data_detail="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivityStaticticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");
            }
        });







        data=new HashMap<>();

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
        Python py=Python.getInstance();
        stat_py=py.getModule("statistics");

        binding.autoCompleteTextStatCalData1.setEnabled(false);
        binding.autoCompleteTextStatCalData2.setEnabled(false);
        binding.txtStatAdditionalCal.setEnabled(false);

        String show="\nTypes\n1)Value and the Frequency (x,f) :\n\t\t\t x1(space)f1\n\t\t\tx2(space)f2\n2)Interval and Frequency (I,f) :\n\t\t\tstart1(space)end1(space)f1\n\t\t\tstart2(space)end2(space)f2\n3)Values :\n\t\t\tx1(space)x2(space)x3(space)....\n\n";
        binding.txtStatExample.setText(show);

        adapterDataSets=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterDataSets.setNotifyOnChange(true);
        binding.autoCompleteTextStatCalData1.setAdapter(adapterDataSets);
        binding.autoCompleteTextStatCalData2.setAdapter(adapterDataSets);
        binding.autoCompleteTextStatDetails.setAdapter(adapterDataSets);

        adapterCalculations=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterCalculations.setNotifyOnChange(true);
        adapterCalculations.addAll(CAL_TYPES);
        binding.autoCompleteTextStatCal.setAdapter(adapterCalculations);

        adapterSave=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterSave.setNotifyOnChange(true);
        adapterSave.addAll(SAVE_TYPES);
        binding.autoCompleteTextStatSaveType.setAdapter(adapterSave);

        binding.autoCompleteTextStatCal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_cal_type= position;
                if (position==0){
                    binding.autoCompleteTextStatCalData1.setEnabled(true);
                    binding.autoCompleteTextStatCalData2.setEnabled(true);
                    binding.txtStatAdditionalCal.setEnabled(false);
                }else if (position==1 || position==2 || position==3){
                    binding.autoCompleteTextStatCalData1.setEnabled(true);
                    binding.autoCompleteTextStatCalData2.setEnabled(false);
                    binding.txtStatAdditionalCal.setEnabled(true);
                }else{
                    binding.autoCompleteTextStatCalData1.setEnabled(false);
                    binding.autoCompleteTextStatCalData2.setEnabled(false);
                    binding.txtStatAdditionalCal.setEnabled(false);
                }
            }
        });
        binding.autoCompleteTextStatSaveType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_save_type= position;
            }
        });
        binding.autoCompleteTextStatDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_data_detail=adapterDataSets.getItem(position);
            }
        });
        binding.autoCompleteTextStatCalData1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_data1=adapterDataSets.getItem(position);
            }
        });
        binding.autoCompleteTextStatCalData2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_data2=adapterDataSets.getItem(position);
            }
        });

        binding.btnStatSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val=binding.txtStatValues.getText().toString();
                String name=binding.txtStatIdentity.getText().toString();
                if (name.isEmpty() || val.isEmpty() || current_save_type==-1){
                    Toast.makeText(Statictics.this, "Empty Data", Toast.LENGTH_SHORT).show();
                }else{
                    if (data.containsKey(name)){
                        Toast.makeText(Statictics.this, "This name is already used", Toast.LENGTH_SHORT).show();
                    }else{
                        PyObject output=stat_py.callAttr("saveData",current_save_type,val);
                        if (output.asList().get(0).toBoolean()){
                            data.put(name,output.asList().get(1));
                            adapterDataSets.add(name);
                            adapterDataSets.notifyDataSetChanged();
                            Toast.makeText(Statictics.this, "Saved", Toast.LENGTH_SHORT).show();
                            binding.txtStatIdentity.setText("");
                            binding.txtStatValues.setText("");
                        }else{
                            Toast.makeText(Statictics.this, output.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        binding.btnStatOpenData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!current_data_detail.isEmpty()){
                    dialog_binding= StatAttrPopupBinding.inflate(getLayoutInflater());
                    Dialog d=new Dialog(Statictics.this);
                    d.setCancelable(false);
                    d.setContentView(dialog_binding.getRoot());
                    dialog_binding.statName.setText(current_data_detail);
                    PyObject out=data.get(current_data_detail).callAttr("getData");
                    dialog_binding.statAttributes.setText(out.asList().get(1).toString());
                    dialog_binding.statPopupCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();
                        }
                    });
                    d.show();
                }else{
                    Toast.makeText(Statictics.this, "Select a data set", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnStatCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_cal_additional=binding.txtStatAdditionalCal.getText().toString();
                if (current_cal_type==0){
                    if (current_data1.isEmpty() || current_data2.isEmpty()){
                        Toast.makeText(Statictics.this, "Empty Data", Toast.LENGTH_SHORT).show();
                        binding.txtStatCalAnswer.setText("");
                    }else{
                        PyObject out=stat_py.callAttr("divert",0,data.get(current_data1),data.get(current_data2));
                        if (out.asList().get(0).toBoolean()){
                            binding.txtStatCalAnswer.setText("Mean = "+out.asList().get(1).toString()+"\nSD = "+out.asList().get(2).toString());
                        }else{
                            Toast.makeText(Statictics.this, out.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                            binding.txtStatCalAnswer.setText("");
                        }
                    }

                }else if(current_cal_type==1){
                    if (current_data1.isEmpty() || current_cal_additional.isEmpty()){
                        Toast.makeText(Statictics.this, "Empty Data", Toast.LENGTH_SHORT).show();
                        binding.txtStatCalAnswer.setText("");
                    }else{
                        PyObject out=stat_py.callAttr("divert",1,data.get(current_data1),current_cal_additional);
                        if (out.asList().get(0).toBoolean()){
                            binding.txtStatCalAnswer.setText("Mean = "+out.asList().get(1).toString()+"\nSD = "+out.asList().get(2).toString());
                        }else{
                            Toast.makeText(Statictics.this, out.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                            binding.txtStatCalAnswer.setText("");
                        }
                    }
                }else if(current_cal_type==2){
                    if (current_data1.isEmpty() || current_cal_additional.isEmpty()){
                        Toast.makeText(Statictics.this, "Empty Data", Toast.LENGTH_SHORT).show();
                        binding.txtStatCalAnswer.setText("");
                    }else{
                        PyObject out=stat_py.callAttr("divert",2,data.get(current_data1),current_cal_additional);
                        if (out.asList().get(0).toBoolean()){
                            binding.txtStatCalAnswer.setText(out.asList().get(1).toString());
                        }else{
                            Toast.makeText(Statictics.this, out.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                            binding.txtStatCalAnswer.setText("");
                        }
                    }

                }else if(current_cal_type==3){
                    if (current_data1.isEmpty() || current_cal_additional.isEmpty()){
                        Toast.makeText(Statictics.this, "Empty Data", Toast.LENGTH_SHORT).show();
                        binding.txtStatCalAnswer.setText("");
                    }else{
                        PyObject out=stat_py.callAttr("divert",3,data.get(current_data1),current_cal_additional);
                        if (out.asList().get(0).toBoolean()){
                            binding.txtStatCalAnswer.setText(out.asList().get(1).toString());
                        }else{
                            Toast.makeText(Statictics.this, out.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                            binding.txtStatCalAnswer.setText("");
                        }
                    }


                }else{
                    Toast.makeText(Statictics.this, "Select a type", Toast.LENGTH_SHORT).show();
                }

            }
        });
    //divert(flag,*data)
    }
}