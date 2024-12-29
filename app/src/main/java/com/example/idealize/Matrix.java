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
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.idealize.databinding.ActivityMatrixBinding;
import com.example.idealize.databinding.MatrixAttrPopupBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matrix extends AppCompatActivity {

    private static HashMap<String,Object> map;
    private static MatrixAttrPopupBinding dialog_binding;
    private static ActivityMatrixBinding binding;
    private static PyObject matrix_python;
    private ArrayAdapter<String> adapterItems,adapterItems_operations;
    private static String[] TYPES={"Square","Null","Row","Column","Diagonal","Unit","Symmetric","Skew_Symmetric","Upper","Lower","Orthogonal"};

    private static String current_attribute_matrix="",MATRIX_A="",MATRIX_B="",OPERATION="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivityMatrixBinding.inflate(getLayoutInflater());
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
        matrix_python=py.getModule("matrix");
        map=new HashMap<>();

        adapterItems=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterItems.setNotifyOnChange(true);
        binding.autoCompleteTextMatrixAttributes.setAdapter(adapterItems);
        binding.autoCompleteTextMatrixA.setAdapter(adapterItems);
        binding.autoCompleteTextMatrixB.setAdapter(adapterItems);

        adapterItems_operations=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterItems_operations.setNotifyOnChange(true);
        adapterItems_operations.addAll("Addition","Subtraction","Scalar Multiplication","Matrix Multiplication","Transpose","Determinant","Inverse");
        binding.autoCompleteTextOperation.setAdapter(adapterItems_operations);

        binding.autoCompleteTextMatrixAttributes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_attribute_matrix= adapterItems.getItem(position);
            }
        });
        binding.autoCompleteTextMatrixA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MATRIX_A= adapterItems.getItem(position);
            }
        });
        binding.autoCompleteTextMatrixB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MATRIX_B= adapterItems.getItem(position);
            }
        });
        binding.autoCompleteTextOperation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OPERATION= adapterItems_operations.getItem(position);
                if (OPERATION=="Transpose" || OPERATION=="Determinant" || OPERATION=="Inverse"){
                    binding.autoCompleteTextMatrixB.setEnabled(false);
                    binding.txtScalar.setEnabled(false);
                }else if (OPERATION=="Scalar Multiplication"){
                    binding.autoCompleteTextMatrixB.setEnabled(false);
                    binding.txtScalar.setEnabled(true);
                }else{
                    binding.autoCompleteTextMatrixB.setEnabled(true);
                    binding.txtScalar.setEnabled(false);
                }

            }
        });

        binding.btnMatrixSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,values;
                name=binding.txtMatrixName.getText().toString();
                values =binding.txtMatrixValues.getText().toString();
                if (!name.isEmpty() & !values.isEmpty()){
                    if (map.containsKey(name)){
                        Toast.makeText(Matrix.this,"This name is already defined.\n Change the name",Toast.LENGTH_LONG).show();
                    }else{
                        PyObject output=matrix_python.callAttr("setMatrix",values);
                        boolean state=output.asList().get(0).toBoolean();
                        PyObject current_matrix=output.asList().get(1);
                        if (!state){
                            Toast.makeText(Matrix.this,"Invalid Matrix",Toast.LENGTH_LONG).show();
                        }else {
                            map.put(name, current_matrix);
                            adapterItems.add(name);
                            adapterItems.notifyDataSetChanged();
                            Toast.makeText(Matrix.this,"Saved",Toast.LENGTH_LONG).show();
                            binding.txtMatrixName.setText("");
                            binding.txtMatrixValues.setText("");
                        }
                    }
                }else{
                    Toast.makeText(Matrix.this,"Can't leave name or values empty",Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.btnMatrixCategories.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(!current_attribute_matrix.isEmpty()){

                    PyObject myMatrix= PyObject.fromJava(map.get(current_attribute_matrix));

                    PyObject order_py=matrix_python.callAttr("getOrder",myMatrix);
                    int row=order_py.asList().get(0).toInt();
                    int col=order_py.asList().get(1).toInt();

                    PyObject categories_py=matrix_python.callAttr("categorize",myMatrix);
                    Map<PyObject, PyObject> category_map=categories_py.asMap();
                    String line="";
                    for (int i=0;i<TYPES.length;i++){
                        line=line+TYPES[i]+" Matrix\t\t:\t"+category_map.get(PyObject.fromJava(TYPES[i])).toString()+"\n";
                    }

                    String show_matrix=matrix_python.callAttr("myPrint",myMatrix).toString();

                    dialog_binding=MatrixAttrPopupBinding.inflate(getLayoutInflater());
                    Dialog d=new Dialog(Matrix.this);
                    d.setCancelable(false);
                    d.setTitle("HELP");
                    d.setContentView(dialog_binding.getRoot());
                    dialog_binding.matrixName.setText(current_attribute_matrix);
                    dialog_binding.matrix.setText(show_matrix);
                    dialog_binding.matrixAttributes.setText(line);
                    dialog_binding.matrixOrder.setText("( "+row+" X " +col+" )");
                    dialog_binding.matrixPopupCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();
                        }
                    });
                    d.show();



                }else{
                    Toast.makeText(Matrix.this,"Select a matrix",Toast.LENGTH_LONG).show();
                }

            }
        });
        binding.btnMatrixCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String print;
                boolean state;
                PyObject result,A,B;
                //"Addition","Subtraction","Scalar Multiplication","Matrix Multiplication","Transpose","Determinant","Inverse"
                if (!OPERATION.isEmpty() &&( !MATRIX_A.isEmpty() || !MATRIX_B.isEmpty())){
                    A= (PyObject) map.get(MATRIX_A);
                    B= (PyObject) map.get(MATRIX_B);
                    if(OPERATION=="Addition"){
                        PyObject output=matrix_python.callAttr("addition",A,B);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            print=matrix_python.callAttr("myPrint",result).toString();
                            binding.txtMatrixAnswer.setText(print);
                        }else{
                            Toast.makeText(Matrix.this,"Can't Add. Check for validity of matrices",Toast.LENGTH_LONG).show();
                        }
                    }else if(OPERATION=="Subtraction"){
                        PyObject output=matrix_python.callAttr("substraction",A,B);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            print=matrix_python.callAttr("myPrint",result).toString();
                            binding.txtMatrixAnswer.setText(print);
                        }else{
                            Toast.makeText(Matrix.this,"Can't subtract. Check for validity of matrices",Toast.LENGTH_LONG).show();
                        }
                    }else if(OPERATION=="Scalar Multiplication" && !binding.txtScalar.getText().toString().isEmpty()){
                        PyObject output;
                        String scalar;

                            scalar=binding.txtScalar.getText().toString();
                            output=matrix_python.callAttr("scalarMutipication",A,scalar);
                            state=output.asList().get(0).toBoolean();
                            result=output.asList().get(1);
                            if (state){
                                print=matrix_python.callAttr("myPrint",result).toString();
                                binding.txtMatrixAnswer.setText(print);
                            }else{
                                Toast.makeText(Matrix.this,"Can't multiply. Check for validity of the matrix and the scalar",Toast.LENGTH_LONG).show();
                            }



                    }else if(OPERATION=="Matrix Multiplication"){
                        PyObject output=matrix_python.callAttr("multipicationMatrix",A,B);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            print=matrix_python.callAttr("myPrint",result).toString();
                            binding.txtMatrixAnswer.setText(print);
                        }else{
                            Toast.makeText(Matrix.this,"Can't multiply. Check for validity of matrices",Toast.LENGTH_LONG).show();
                        }
                    }else if(OPERATION=="Transpose"){
                        PyObject output=matrix_python.callAttr("transposeMatrix",A);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            print=matrix_python.callAttr("myPrint",result).toString();
                            binding.txtMatrixAnswer.setText(print);
                        }else{
                            Toast.makeText(Matrix.this,"Can't get transpose. Check for validity of the matrix",Toast.LENGTH_LONG).show();
                        }
                    }else if(OPERATION=="Determinant"){
                        PyObject output=matrix_python.callAttr("determinantMatrix",A);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            print=result.toString();
                            binding.txtMatrixAnswer.setText(print);
                        }else{
                            Toast.makeText(Matrix.this,"Can't get determinant. Check for validity of the matrix",Toast.LENGTH_LONG).show();
                        }
                    }else if(OPERATION=="Inverse"){
                        PyObject output=matrix_python.callAttr("inverseMatrix",A);
                        state=output.asList().get(0).toBoolean();
                        result=output.asList().get(1);
                        if (state){
                            print=matrix_python.callAttr("myPrint",result).toString();
                            binding.txtMatrixAnswer.setText(print);
                        }else{
                            Toast.makeText(Matrix.this,"Can't invert. Check for validity of the matrix",Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    Toast.makeText(Matrix.this,"Select arguments",Toast.LENGTH_LONG).show();
                }

            }
        });
    }





}