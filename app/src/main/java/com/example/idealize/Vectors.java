package com.example.idealize;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.example.idealize.databinding.ActivityVectorsBinding;
import com.example.idealize.databinding.MatrixAttrPopupBinding;
import com.example.idealize.databinding.VectorsAttrPopupBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;

public class Vectors extends AppCompatActivity {

    private ActivityVectorsBinding binding;
    private static HashMap<String,PyObject> map;
    private static VectorsAttrPopupBinding dialog_binding;
    private PyObject vector_python;
    private ArrayAdapter<String> adapterItems,adapterItems_operations;
    private static String current_prop_vector,VECTOR_A,VECTOR_B,VECTOR_C,OPERATION;
    private static String[] POST={"i","j","k","l","m","n"};
    private static String[] OPERATIONS={"Is Parallel","Is Perpendicular","Addition","Subtraction","Scalar Multiplication","Dot Product","Cross Product","Scalar Triple Product","Vector Triple Product"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivityVectorsBinding.inflate(getLayoutInflater());
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
        vector_python=py.getModule("vectors");

        map=new HashMap<String,PyObject>();

        adapterItems=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterItems.setNotifyOnChange(true);
        binding.autoCompleteTextVecA.setAdapter(adapterItems);
        binding.autoCompleteTextVecB.setAdapter(adapterItems);
        binding.autoCompleteTextVecC.setAdapter(adapterItems);
        binding.autoCompleteTextVectorProperties.setAdapter(adapterItems);

        adapterItems_operations=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterItems_operations.setNotifyOnChange(true);
        adapterItems_operations.addAll(OPERATIONS);
        binding.autoCompleteTextOperations.setAdapter(adapterItems_operations);

        binding.autoCompleteTextVectorProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_prop_vector= adapterItems.getItem(position);
                PyObject output=vector_python.callAttr("getVecName",current_prop_vector);
                current_prop_vector= output.toString();
            }
        });
        binding.autoCompleteTextVecA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VECTOR_A= adapterItems.getItem(position);
                PyObject output=vector_python.callAttr("getVecName",VECTOR_A);
                VECTOR_A= output.toString();
            }
        });
        binding.autoCompleteTextVecB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VECTOR_B= adapterItems.getItem(position);
                PyObject output=vector_python.callAttr("getVecName",VECTOR_B);
                VECTOR_B= output.toString();
            }
        });
        binding.autoCompleteTextVecC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VECTOR_C= adapterItems.getItem(position);
                PyObject output=vector_python.callAttr("getVecName",VECTOR_C);
                VECTOR_C= output.toString();
            }
        });
        binding.autoCompleteTextOperations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OPERATION= adapterItems_operations.getItem(position);
                if (OPERATION=="Addition" || OPERATION=="Subtraction" || OPERATION=="Dot Product" || OPERATION=="Cross Product" || OPERATION=="Is Parallel" || OPERATION=="Is Perpendicular"){
                    binding.autoCompleteTextVecC.setEnabled(false);
                    binding.txtScalar.setEnabled(false);
                    binding.autoCompleteTextVecB.setEnabled(true);
                }else if (OPERATION=="Scalar Multiplication"){
                    binding.autoCompleteTextVecB.setEnabled(false);
                    binding.autoCompleteTextVecC.setEnabled(false);
                    binding.txtScalar.setEnabled(true);
                }else{
                    binding.autoCompleteTextVecB.setEnabled(true);
                    binding.autoCompleteTextVecC.setEnabled(true);
                    binding.txtScalar.setEnabled(false);
                }

            }
        });

        binding.btnVectorSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,values;
                name=binding.txtVectorName.getText().toString();
                values =binding.txtVectorValues.getText().toString();
                if (!name.isEmpty() & !values.isEmpty()){
                    if(!map.containsKey(name)) {
                        PyObject output = vector_python.callAttr("define", values, name);
                        boolean state = output.asList().get(0).toBoolean();
                        PyObject current_vector = output.asList().get(1);
                        if (!state) {
                            Toast.makeText(Vectors.this, output.asList().get(2).toString(), Toast.LENGTH_LONG).show();
                        } else {
                            map.put(name, current_vector);
                            adapterItems.add(name+" ("+output.asList().get(2).toString()+")");
                            adapterItems.notifyDataSetChanged();
                            Toast.makeText(Vectors.this, "Saved", Toast.LENGTH_LONG).show();
                            binding.txtVectorName.setText("");
                            binding.txtVectorValues.setText("");
                        }
                    }else{
                        Toast.makeText(Vectors.this,"This name is already used", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Vectors.this,"Can't leave name or values empty",Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.btnVectorProps.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                try{
                if(!current_prop_vector.isEmpty()){

                    PyObject my_vector= map.get(current_prop_vector);

                    String magnitude=my_vector.callAttr("getMagnitude").toString();
                    PyObject directions=my_vector.callAttr("getDirections");
                    String show=my_vector.callAttr("getShow").toString();


                    String dir="";
                    int length=directions.asList().size();
                    for(int i=0;i<length;i++){
                        dir=dir+"Directions of Cosine with respect to "+POST[i]+" (rad) = "+String.valueOf(directions.asList().get(i))+"\n";
                    }



                    dialog_binding= VectorsAttrPopupBinding.inflate(getLayoutInflater());
                    Dialog d=new Dialog(Vectors.this);
                    d.setCancelable(false);
                    d.setTitle("Properties");
                    d.setContentView(dialog_binding.getRoot());
                    dialog_binding.vectorName.setText(current_prop_vector);
                    dialog_binding.vector.setText(show);
                    dialog_binding.vectorDirections.setText(dir);
                    dialog_binding.vectorMag.setText("|"+current_prop_vector+"| = " +magnitude);
                    dialog_binding.vectorPopupCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();
                        }
                    });
                    d.show();



                }else{
                    Toast.makeText(Vectors.this,"Select a matrix",Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                    Toast.makeText(Vectors.this,"Select a Vector",Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.btnVectorCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String print;
                boolean state;
                PyObject result,A,B,C;
                //"Is Parallel","Is Perpendicular","Addition","Subtraction","Dot Product","Cross Product"
               try{

                    A= map.get(VECTOR_A);
                    B= map.get(VECTOR_B);
                    C=map.get(VECTOR_C);
                    if(OPERATION=="Addition"){
                        PyObject output=vector_python.callAttr("addition",A,B);
                        print=vector_python.callAttr("myPrint",output).toString();
                            binding.txtVectorAnswer.setText(print);
                    }else if(OPERATION=="Subtraction"){
                        PyObject output=vector_python.callAttr("subtraction",A,B);
                        print=vector_python.callAttr("myPrint",output).toString();
                        binding.txtVectorAnswer.setText(print);
                    }else if(OPERATION=="Dot Product"){
                        PyObject output=vector_python.callAttr("dotProduct",A,B);

                        binding.txtVectorAnswer.setText(output.toString());
                    }else if(OPERATION=="Cross Product"){
                        PyObject output=vector_python.callAttr("crossProduct",A,B);
                        state=output.asList().get(0).toBoolean();
                        if (state){
                            print=vector_python.callAttr("myPrint",output.asList().get(1)).toString();
                            binding.txtVectorAnswer.setText(print);
                        }else{
                            Toast.makeText(Vectors.this,"Cross Product is available up to 3 dimensions.",Toast.LENGTH_LONG).show();
                            binding.txtVectorAnswer.setText("");
                        }

                    }else if(OPERATION=="Is Parallel"){
                        PyObject output=vector_python.callAttr("isParallel",A,B);
                        print=String.valueOf(output.toBoolean());
                        binding.txtVectorAnswer.setText(print);
                    }else if(OPERATION=="Is Perpendicular"){
                        PyObject output=vector_python.callAttr("isPerpendicular",A,B);
                        print=String.valueOf(output.toBoolean());
                        binding.txtVectorAnswer.setText(print);


                       // "Scalar Multiplication","Scalar Triple Product","Vector Triple Product
                        }
                else if(OPERATION=="Scalar Multiplication" ){
                        PyObject output;
                        String scalar;

                        scalar=binding.txtScalar.getText().toString();
                        if(scalar.isEmpty()){
                            Toast.makeText(Vectors.this,"Give the scalar",Toast.LENGTH_LONG).show();
                            binding.txtVectorAnswer.setText("");
                        }else{
                            output=vector_python.callAttr("scale",A,scalar);
                            print=vector_python.callAttr("myPrint",output).toString();
                            binding.txtVectorAnswer.setText(print);
                        }


                }else if(OPERATION=="Scalar Triple Product"){

                        PyObject output=vector_python.callAttr("scalarTripleProduct",A,B,C);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            binding.txtVectorAnswer.setText(result.toString());
                        }else{
                            Toast.makeText(Vectors.this,"Available up to 3 dimensions.",Toast.LENGTH_LONG).show();
                            binding.txtVectorAnswer.setText("");
                        }

                }else if(OPERATION=="Vector Triple Product"){

                        PyObject output=vector_python.callAttr("vectorTripleProduct",A,B,C);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            print=vector_python.callAttr("myPrint",result).toString();
                            binding.txtVectorAnswer.setText(print);
                        }else{
                            Toast.makeText(Vectors.this,"Available up to 3 dimensions.",Toast.LENGTH_LONG).show();
                            binding.txtVectorAnswer.setText("");
                        }

                }else{
                    Toast.makeText(Vectors.this,"Select arguments",Toast.LENGTH_LONG).show();
                        binding.txtVectorAnswer.setText("");
                }

            } catch (Exception e) {
                    Toast.makeText(Vectors.this,"Select parameters"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}