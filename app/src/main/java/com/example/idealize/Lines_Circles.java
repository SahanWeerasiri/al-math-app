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
import com.example.idealize.databinding.ActivityLinesCirclesBinding;
import com.example.idealize.databinding.MatrixAttrPopupBinding;
import com.example.idealize.databinding.VectorsAttrPopupBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;

public class Lines_Circles extends AppCompatActivity {

    private ActivityLinesCirclesBinding binding;
    private VectorsAttrPopupBinding dialog_binding;
    private PyObject simultaneous_py;
    private PyObject line_circle_py;

    //Line
    private ArrayAdapter<String> adapterLines,adapterLinesTypes,adapterLinesCal;

    private static HashMap<String,PyObject> lines;
    private static HashMap<String,String> NAMES,SHOWS;
    private static String SAVE_TYPES[]={"2 Points","A point and the gradient","The gradient and the y intercept","The line (ax+by+c=0)"};
    private static String CAL_TYPES[]={"Is Parallel","Is Perpendicular","Intercepts","Shortest Distance from a point","Angle between 2 lines","Distance between 2 points","Availability of a point on the line","Position of 2 points w.r.t. a line","Line at a intercept"};
    private static int current_save_type=-1,current_cal_type=-1;
    private static String current_cal_data,current_line1,current_line2,current_line_detail;

    //Circle
    private ArrayAdapter<String> adapterCircles,adapterCirclesTypes,adapterCirclesCal;
    private static HashMap<String,PyObject> circles;
    private static HashMap<String,String> NAMES_CIRCLES,SHOWS_CIRCLES;
    private static String SAVE_TYPES_CIRCLES[]={"Center and Radius","The Circle \n(x^2 +y^2 +2gx +2fy +c =0)","Diameter Edge Points"};
    private static String CAL_TYPES_CIRCLES[]={"Position of a Point w.r.t the Circle","Position of a Circle and a Line","Tangent By a Point on the Circle","Tangent by a Point outside \nthe Circle","Length of a Tangent","Tangent Chord","Circle Through Intercepts of \na Circle and a Line","Position of 2 Circles","Circle Through Intercepts of\n 2 Circles","Is Initially Intercept","Is Bisecting"};
    private static int current_save_type_circles=-1,current_cal_type_circles=-1;
    private static String current_cal_data_circles,current_circle_line,current_circle1,current_circle2,current_circle_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding= ActivityLinesCirclesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");
            }
        });







        lines=new HashMap<>();
        NAMES=new HashMap<>();
        SHOWS=new HashMap<>();

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py=Python.getInstance();
        line_circle_py=py.getModule("lines");
        simultaneous_py=py.getModule("simultaneous");


        adapterLines=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterLines.setNotifyOnChange(true);
        binding.autoCompleteTextLineCalLine1.setAdapter(adapterLines);
        binding.autoCompleteTextLineCalLine2.setAdapter(adapterLines);
        binding.autoCompleteTextLineDetails.setAdapter(adapterLines);
        binding.autoCompleteTextCircleCalLine.setAdapter(adapterLines);

        adapterLinesTypes=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterLinesTypes.setNotifyOnChange(true);
        adapterLinesTypes.addAll(SAVE_TYPES);
        binding.autoCompleteTextLineSaveType.setAdapter(adapterLinesTypes);


        adapterLinesCal=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterLinesCal.setNotifyOnChange(true);
        adapterLinesCal.addAll(CAL_TYPES);
        binding.autoCompleteTextLineCalType.setAdapter(adapterLinesCal);

        binding.autoCompleteTextLineSaveType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_save_type= position;
            }
        });
        binding.autoCompleteTextLineDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_line_detail= adapterLines.getItem(position);
            }
        });
        binding.autoCompleteTextLineCalLine1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_line1=adapterLines.getItem( position);
            }
        });
        binding.autoCompleteTextLineCalLine2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_line2=adapterLines.getItem( position);
            }
        });
        binding.autoCompleteTextLineCalType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_cal_type= position;
                if (position==0 ||position==1 || position==2 || position==4){
                    binding.autoCompleteTextLineCalLine1.setEnabled(true);
                    binding.autoCompleteTextLineCalLine2.setEnabled(true);
                    binding.txtLineAdditionalPoints.setEnabled(false);
                }else if (position==3 || position==6 || position==7){
                    binding.autoCompleteTextLineCalLine1.setEnabled(true);
                    binding.autoCompleteTextLineCalLine2.setEnabled(false);
                    binding.txtLineAdditionalPoints.setEnabled(true);
                }else if (position==5){
                    binding.autoCompleteTextLineCalLine1.setEnabled(false);
                    binding.autoCompleteTextLineCalLine2.setEnabled(false);
                    binding.txtLineAdditionalPoints.setEnabled(true);
                }else if (position==8){
                    binding.autoCompleteTextLineCalLine1.setEnabled(true);
                    binding.autoCompleteTextLineCalLine2.setEnabled(true);
                    binding.txtLineAdditionalPoints.setEnabled(true);
                }else {
                    binding.autoCompleteTextLineCalLine1.setEnabled(false);
                    binding.autoCompleteTextLineCalLine2.setEnabled(false);
                    binding.txtLineAdditionalPoints.setEnabled(false);
                }
            }
        });

        binding.autoCompleteTextLineCalLine1.setEnabled(false);
        binding.autoCompleteTextLineCalLine2.setEnabled(false);
        binding.txtLineAdditionalPoints.setEnabled(false);






        binding.btnLineSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_save_type==-1){
                    Toast.makeText(Lines_Circles.this, "Select a Type", Toast.LENGTH_SHORT).show();
                }else{
                    String data=binding.txtLineValues.getText().toString();
                    String name=binding.txtLineIdentity.getText().toString();
                    if (data.isEmpty() || name.isEmpty()){
                        Toast.makeText(Lines_Circles.this, "Empty data", Toast.LENGTH_SHORT).show();
                    }else{
                        if (NAMES.containsKey(name)){
                            Toast.makeText(Lines_Circles.this, "This name is already used.", Toast.LENGTH_SHORT).show();
                        }else{

                        PyObject rst=line_circle_py.callAttr("saveLine",data,current_save_type);
                        boolean state=rst.asList().get(0).toBoolean();
                        if (state){
                            PyObject my_line=rst.asList().get(1);
                            String show=my_line.callAttr("getShow").toString();
                            String key=name+" -> "+show;
                            if (SHOWS.containsKey(show)){
                                Toast.makeText(Lines_Circles.this, "This line is already saved.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                SHOWS.put(show,"1");
                                NAMES.put(name,"1");
                            adapterLines.add(key);
                            lines.put(key,my_line);
                            adapterLines.notifyDataSetChanged();
                            Toast.makeText(Lines_Circles.this, "Saved", Toast.LENGTH_SHORT).show();
                            binding.txtLineIdentity.setText("");
                            binding.txtLineValues.setText("");
                            }
                        }else{
                            Toast.makeText(Lines_Circles.this, rst.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                    }
                }

            }
        });

        binding.btnLineDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_line_detail==null){
                    Toast.makeText(Lines_Circles.this,"Select a Line", Toast.LENGTH_SHORT).show();
                }else{
                    PyObject current_py_line=lines.get(current_line_detail);
                    PyObject rst=current_py_line.callAttr("getDetails");
                    String m,c,show,name;
                    m=rst.asList().get(0).toString();
                    c=rst.asList().get(1).toString();
                    show=rst.asList().get(3).toString();
                    name=(current_line_detail.split(" -> "))[0];
                    dialog_binding= VectorsAttrPopupBinding.inflate(getLayoutInflater());
                    Dialog d=new Dialog(Lines_Circles.this);
                    d.setCancelable(false);
                    d.setTitle("Details");
                    d.setContentView(dialog_binding.getRoot());
                    dialog_binding.vectorName.setText(name);
                    dialog_binding.vector.setText(show);

                    String x,y,z,l;
                    x=rst.asList().get(2).asList().get(0).toString();
                    y=rst.asList().get(2).asList().get(1).toString();
                    z=rst.asList().get(2).asList().get(2).toString();
                    l="("+x+")*X + ("+y+")*Y + ("+z+") = 0";
                    dialog_binding.vectorDirections.setText(l);
                    String msg="Gradient (m) = "+m+"\nY intercept (c) = "+c;

                    dialog_binding.vectorMag.setText(msg);
                    dialog_binding.vectorPopupCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();
                        }
                    });
                    d.show();
                }

            }
        });

        binding.btnLineCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_cal_type==-1){
                    Toast.makeText(Lines_Circles.this,"Select a Type", Toast.LENGTH_SHORT).show();
                }else{
                    /*
                    "Is Parallel",0
                    "Is Perpendicular",1
                    "Intercepts",2
                    "Shortest Distance from a point",3
                    "Angle between 2 lines",4
                    "Distance between 2 points",5
                    "Availability of a point on the line",6
                    "Position of 2 points w.r.t. a line",7
                    "Line at a intercept" 8
                    */
                    PyObject rst=null;
                    PyObject l1 = null,l2=null;
                    current_cal_data=binding.txtLineAdditionalPoints.getText().toString();
                    if(current_line1!=null){
                        l1=lines.get(current_line1);
                    }
                    if(current_line2!=null){
                        l2=lines.get(current_line2);
                    }

                    if(current_cal_type==0){
                        if(l1!=null && l2!=null){
                            rst=line_circle_py.callAttr("isParallel",l1,l2);
                            binding.txtLineCalAnswer.setText(rst.asList().get(1).toString());
                            rst=null;
                            l1=null;
                            l2=null;
                            current_cal_data=null;
                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }

                    }else if(current_cal_type==1){
                        if(l1!=null && l2!=null){
                            rst=line_circle_py.callAttr("isShunt",l1,l2);
                            binding.txtLineCalAnswer.setText(rst.asList().get(1).toString());
                            rst=null;
                            l1=null;
                            l2=null;
                            current_cal_data=null;
                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else if(current_cal_type==2){
                        if(l1!=null && l2!=null){
                            rst=line_circle_py.callAttr("getIntersectionPoint",l1,l2);
                            if (rst.asList().get(0).toBoolean()){
                                rst=simultaneous_py.callAttr("main",rst.asList().get(1).toString(),1);
                                if(rst.asList().get(0).toBoolean()){
                                    binding.txtLineCalAnswer.setText("("+rst.asList().get(1).asList().get(0).toString()+","+rst.asList().get(1).asList().get(1).toString()+")");
                                }else{
                                    binding.txtLineCalAnswer.setText(rst.asList().get(1).toString());
                                }
                            }else{
                                binding.txtLineCalAnswer.setText("");
                                Toast.makeText(Lines_Circles.this,rst.asList().get(1).toString(),Toast.LENGTH_SHORT).show();
                            }
                            rst=null;
                            current_cal_data=null;
                            l1=null;
                            l2=null;
                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else if(current_cal_type==3){
                        if(l1!=null && !current_cal_data.isEmpty()){
                            rst=line_circle_py.callAttr("getDistanceToPoint",l1,current_cal_data);
                            if (rst.asList().get(0).toBoolean()){
                                binding.txtLineCalAnswer.setText(rst.asList().get(1).toString());
                                rst=null;
                                current_cal_data=null;
                                l1=null;
                                l2=null;
                            }else{
                                binding.txtLineCalAnswer.setText("");
                                Toast.makeText(Lines_Circles.this,rst.asList().get(1).toString(),Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else if(current_cal_type==4){
                        if(l1!=null && l2!=null){
                            rst=line_circle_py.callAttr("getAngle",l1,l2);
                            binding.txtLineCalAnswer.setText(rst.toString());
                            rst=null;
                            l1=null;
                            l2=null;
                            current_cal_data=null;
                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else if(current_cal_type==5){
                        if(!current_cal_data.isEmpty()){
                            rst=line_circle_py.callAttr("distanceBetweenPoints",current_cal_data);
                            if(rst.asList().get(0).toBoolean()){
                                binding.txtLineCalAnswer.setText(rst.asList().get(1).toString());
                                rst=null;
                                l1=null;
                                l2=null;
                                current_cal_data=null;
                            }else{
                                binding.txtLineCalAnswer.setText("");
                                Toast.makeText(Lines_Circles.this,rst.asList().get(1).toString(),Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else if(current_cal_type==6){
                        if(l1!=null && !current_cal_data.isEmpty()){
                            rst=line_circle_py.callAttr("isAvailableOnLine",l1,current_cal_data);
                                binding.txtLineCalAnswer.setText(rst.asList().get(1).toString());
                                rst=null;
                                current_cal_data=null;
                                l1=null;
                                l2=null;

                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else if(current_cal_type==7){
                        if(l1!=null && !current_cal_data.isEmpty()){
                            rst=line_circle_py.callAttr("isSameSide",l1,current_cal_data);
                            if(rst.asList().get(0).toBoolean()){
                                binding.txtLineCalAnswer.setText(rst.asList().get(1).toString());
                                rst=null;
                                l1=null;
                                l2=null;
                                current_cal_data=null;
                            }else{
                                binding.txtLineCalAnswer.setText("");
                                Toast.makeText(Lines_Circles.this,rst.asList().get(1).toString(),Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else if(current_cal_type==8){
                        if(l1!=null && l2!=null && current_cal_data!=null){
                            rst=line_circle_py.callAttr("getIntersectionPoint",l1,l2);
                            if (rst.asList().get(0).toBoolean()){
                                rst=simultaneous_py.callAttr("main",rst.asList().get(1),1);
                                if(rst.asList().get(0).toBoolean()){
                                    String points_data=current_cal_data+" "+rst.asList().get(1).asList().get(0).toString()+","+rst.asList().get(1).asList().get(1).toString();
                                    rst=line_circle_py.callAttr("saveLine",points_data,0);
                                    boolean state=rst.asList().get(0).toBoolean();
                                    if (state){
                                        PyObject my_line=rst.asList().get(1);
                                        String show=my_line.callAttr("getShow").toString();
                                        binding.txtLineCalAnswer.setText(show);
                                        rst=null;
                                        current_cal_data=null;
                                        l1=null;
                                        l2=null;
                                    }else{
                                        binding.txtLineCalAnswer.setText("");
                                        Toast.makeText(Lines_Circles.this, rst.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                                    }


                                }
                                else{
                                binding.txtLineCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,rst.asList().get(1).toString(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                binding.txtLineCalAnswer.setText("");
                                Toast.makeText(Lines_Circles.this,rst.asList().get(1).toString(),Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            binding.txtLineCalAnswer.setText("");
                            Toast.makeText(Lines_Circles.this,"Empty Data",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        binding.txtLineCalAnswer.setText("");
                        Toast.makeText(Lines_Circles.this,"Select a Calculation",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        binding.btnLineRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p1,p2,p3,m,n;
                p1=binding.txtLineRatioPoint1.getText().toString();
                p2=binding.txtLineRatioPoint2.getText().toString();
                p3=binding.txtLineRatioPoint3.getText().toString();
                m=binding.txtLineRatioM.getText().toString();
                n=binding.txtLineRatioN.getText().toString();
                if (p1.isEmpty()){
                    p1="None";
                }
                if (p2.isEmpty()){
                    p2="None";
                }
                if (p3.isEmpty()){
                    p3="None";
                }
                if (m.isEmpty()){
                    m="";
                }
                if (n.isEmpty()){
                    n="";
                }
                String points=p1+'x'+p2+'x'+p3;
                PyObject rst=line_circle_py.callAttr("ratioMethod",m,n,points);
                if(rst.asList().get(0).toBoolean()){
                    if(p1=="None"){
                        binding.txtLineRatioPoint1.setText(rst.asList().get(1).toString());
                    }else if(p2=="None"){
                        binding.txtLineRatioPoint2.setText(rst.asList().get(1).toString());
                    }else {
                        binding.txtLineRatioPoint3.setText(rst.asList().get(1).toString());
                    }
                }else{
                    Toast.makeText(Lines_Circles.this,rst.asList().get(1).toString(),Toast.LENGTH_SHORT).show();

                }


            }
        });





        /////////////////////////////////////////////////////
        /////////////////////////////////////////////////////
        //////////////////CIRCLE/////////////////////////////
        ////////////////////////////////////////////////////
        ////////////////////////////////////////////////////



        circles=new HashMap<>();
        NAMES_CIRCLES=new HashMap<>();
        SHOWS_CIRCLES=new HashMap<>();


        adapterCircles=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterCircles.setNotifyOnChange(true);
        binding.autoCompleteTextCircleCalCircle1.setAdapter(adapterCircles);
        binding.autoCompleteTextCircleCalCircle2.setAdapter(adapterCircles);
        binding.autoCompleteTextCircleDetails.setAdapter(adapterCircles);

        adapterCirclesTypes=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterCirclesTypes.setNotifyOnChange(true);
        adapterCirclesTypes.addAll(SAVE_TYPES_CIRCLES);
        binding.autoCompleteTextCircleSaveType.setAdapter(adapterCirclesTypes);


        adapterCirclesCal=new ArrayAdapter<String>(this,R.layout.list_item);
        adapterCirclesCal.setNotifyOnChange(true);
        adapterCirclesCal.addAll(CAL_TYPES_CIRCLES);
        binding.autoCompleteTextCircleCalType.setAdapter(adapterCirclesCal);



        binding.autoCompleteTextCircleSaveType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_save_type_circles= position;
            }
        });
        binding.autoCompleteTextCircleDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_circle_detail= adapterCircles.getItem(position);
            }
        });
        binding.autoCompleteTextCircleCalCircle1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_circle1=adapterCircles.getItem( position);
            }
        });
        binding.autoCompleteTextCircleCalCircle2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_circle2=adapterCircles.getItem( position);
            }
        });
        binding.autoCompleteTextCircleCalLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_circle_line= adapterLines.getItem(position);
            }
        });

        /*"Position of a Point w.r.t the Circle",0              c,p
        "Position of a Circle and a Line",1                     c,l
        "Tangent By a Point on the Circle",2                    c,p
        "Tangent by a Point outside the Circle",3               c,p
        "Length of a Tangent",4                                 c,p
        "Tangent Chord",5                                       c,p
        "Circle Through Intercepts of a Circle and a Line",6    c,l,p
        "Position of 2 Circles",7                               c,c
        "Circle Through Intercepts of 2 Circles",8              c,c,p
        "Is Initially Intercept",9                              c,c
        "Is Bisecting" 10                                       c,c

        */
        binding.autoCompleteTextCircleCalType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_cal_type_circles= position;
                if (position==0 ||position==2 || position==3 || position==4 || position==5){
                    binding.autoCompleteTextCircleCalCircle1.setEnabled(true);
                    binding.autoCompleteTextCircleCalCircle2.setEnabled(false);
                    binding.autoCompleteTextCircleCalLine.setEnabled(false);
                    binding.txtCircleAdditionalPoints.setEnabled(true);
                }else if (position==7 || position==9 || position==10){
                    binding.autoCompleteTextCircleCalCircle1.setEnabled(true);
                    binding.autoCompleteTextCircleCalCircle2.setEnabled(true);
                    binding.autoCompleteTextCircleCalLine.setEnabled(false);
                    binding.txtCircleAdditionalPoints.setEnabled(false);
                }else if (position==1){
                    binding.autoCompleteTextCircleCalCircle1.setEnabled(true);
                    binding.autoCompleteTextCircleCalCircle2.setEnabled(false);
                    binding.autoCompleteTextCircleCalLine.setEnabled(true);
                    binding.txtCircleAdditionalPoints.setEnabled(false);
                }else if (position==6){
                    binding.autoCompleteTextCircleCalCircle1.setEnabled(true);
                    binding.autoCompleteTextCircleCalCircle2.setEnabled(false);
                    binding.autoCompleteTextCircleCalLine.setEnabled(true);
                    binding.txtCircleAdditionalPoints.setEnabled(true);
                }else if(position==8){
                    binding.autoCompleteTextCircleCalCircle1.setEnabled(true);
                    binding.autoCompleteTextCircleCalCircle2.setEnabled(true);
                    binding.autoCompleteTextCircleCalLine.setEnabled(false);
                    binding.txtCircleAdditionalPoints.setEnabled(true);
                }else {
                    binding.autoCompleteTextCircleCalCircle1.setEnabled(false);
                    binding.autoCompleteTextCircleCalCircle2.setEnabled(false);
                    binding.autoCompleteTextCircleCalLine.setEnabled(false);
                    binding.txtCircleAdditionalPoints.setEnabled(false);
                }
            }
        });

        binding.autoCompleteTextCircleCalCircle1.setEnabled(false);
        binding.autoCompleteTextCircleCalCircle2.setEnabled(false);
        binding.autoCompleteTextCircleCalLine.setEnabled(false);
        binding.txtCircleAdditionalPoints.setEnabled(false);






        binding.btnCircleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_save_type_circles==-1){
                    Toast.makeText(Lines_Circles.this, "Select a Type", Toast.LENGTH_SHORT).show();
                }else{
                    String data=binding.txtCircleValues.getText().toString();
                    String name=binding.txtCircleIdentity.getText().toString();
                    if (data.isEmpty() || name.isEmpty()){
                        Toast.makeText(Lines_Circles.this, "Empty data", Toast.LENGTH_SHORT).show();
                    }else{
                        if (NAMES_CIRCLES.containsKey(name)){
                            Toast.makeText(Lines_Circles.this, "This name is already used.", Toast.LENGTH_SHORT).show();
                        }else{

                            PyObject rst=line_circle_py.callAttr("saveCircle",data,current_save_type_circles);
                            boolean state=rst.asList().get(0).toBoolean();
                            if (state){
                                PyObject my_circle=rst.asList().get(1);
                                PyObject show=my_circle.callAttr("getShow");
                                String key=name+" ->"+show.asList().get(1).toString();
                                if (SHOWS_CIRCLES.containsKey(show.asList().get(1).toString())){
                                    Toast.makeText(Lines_Circles.this, "This circle is already saved.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    SHOWS_CIRCLES.put(show.asList().get(1).toString(),"1");
                                    NAMES_CIRCLES.put(name,"1");
                                    adapterCircles.add(key);
                                    circles.put(key,my_circle);
                                    adapterCircles.notifyDataSetChanged();
                                    Toast.makeText(Lines_Circles.this, "Saved", Toast.LENGTH_SHORT).show();
                                    binding.txtCircleIdentity.setText("");
                                    binding.txtCircleValues.setText("");
                                }
                            }else{
                                Toast.makeText(Lines_Circles.this, rst.asList().get(1).toString(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }

            }
        });

        binding.btnCircleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_circle_detail==null){
                    Toast.makeText(Lines_Circles.this,"Select a Circle", Toast.LENGTH_SHORT).show();
                }else{
                    PyObject current_py_circle=circles.get(current_circle_detail);
                    PyObject rst=current_py_circle.callAttr("getDetails");
                    //self.center,self.radius,self.crcl,self.show1,self.show2

                    String center,r,g,f,c,show1,show2,name;
                    center=rst.asList().get(0).toString();
                    r=rst.asList().get(1).toString();
                    g=rst.asList().get(2).asList().get(0).toString();
                    f=rst.asList().get(2).asList().get(1).toString();
                    c=rst.asList().get(2).asList().get(2).toString();
                    show1=rst.asList().get(3).toString();
                    show2=rst.asList().get(4).toString();
                    name=(current_circle_detail.split(" ->"))[0];
                    dialog_binding= VectorsAttrPopupBinding.inflate(getLayoutInflater());
                    Dialog d=new Dialog(Lines_Circles.this);
                    d.setCancelable(false);
                    d.setTitle("Details");
                    d.setContentView(dialog_binding.getRoot());
                    dialog_binding.vectorName.setText(name);
                    dialog_binding.vector.setText(show1+"\n"+show2);

                    String display="Center = "+center+"\nRadius = "+r+"\n\ng = "+g+"\nf = "+f+"\nc = "+c;
                    dialog_binding.vectorMag.setText(display);
                    dialog_binding.vectorPopupCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();
                        }
                    });
                    d.show();
                }

            }
        });

        binding.btnCircleCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_cal_type_circles==-1){
                    Toast.makeText(Lines_Circles.this,"Select a Type", Toast.LENGTH_SHORT).show();
                }else{
                    PyObject rst=null;
                    PyObject c1 = null,c2=null,l=null;
                    current_cal_data_circles=binding.txtCircleAdditionalPoints.getText().toString();
                    if(current_circle1!=null){
                        c1=circles.get(current_circle1);
                    }
                    if(current_circle2!=null){
                        c2=circles.get(current_circle2);
                    }
                    if(current_circle_line!=null){
                        l=lines.get(current_circle_line);
                    }

                    if(current_cal_type_circles!=-1){
                        //circleCalculations(flag,*data)

        /*"Position of a Point w.r.t the Circle",0              c,p
        "Position of a Circle and a Line",1                     c,l
        "Tangent By a Point on the Circle",2                    c,p
        "Tangent by a Point outside the Circle",3               c,p
        "Length of a Tangent",4                                 c,p
        "Tangent Chord",5                                       c,p
        "Circle Through Intercepts of a Circle and a Line",6    c,l,p
        "Position of 2 Circles",7                               c,c
        "Circle Through Intercepts of 2 Circles",8              c,c,p
        "Is Initially Intercept",9                              c,c
        "Is Bisecting" 10                                       c,c

        */
                        PyObject result=null;
                        boolean state=false;
                        String msg="";
                        switch (current_cal_type_circles){
                            case 0:
                                if(current_cal_data_circles.isEmpty() || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need a circle and a point",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",0,c1,current_cal_data_circles);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }
                                break;
                            case 1:
                                if(l==null || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need a circle and a line",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",1,c1,l);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 2:
                                if(current_cal_data_circles.isEmpty() || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need a circle and a point",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",2,c1,current_cal_data_circles);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 3:
                                if(current_cal_data_circles.isEmpty() || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need a circle and a point",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",3,c1,current_cal_data_circles);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 4:
                                if(current_cal_data_circles.isEmpty() || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need a circle and a point",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",4,c1,current_cal_data_circles);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 5:
                                if(current_cal_data_circles.isEmpty() || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need a circle and a point",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",5,c1,current_cal_data_circles);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 6:
                                if(current_cal_data_circles.isEmpty() || c1==null || l==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need a circle, a line and a point",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",6,c1,l,current_cal_data_circles);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 7:
                                if(c2==null || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need 2 circles",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",7,c1,c2);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 8:
                                if(current_cal_data_circles.isEmpty() || c1==null || c2==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need 2 circles and a point",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",8,c1,c2,current_cal_data_circles);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 9:
                                if(c2==null || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need 2 circles",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",9,c1,c2);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            case 10:
                                if(c2==null || c1==null){
                                    binding.txtCircleCalAnswer.setText("");
                                    Toast.makeText(Lines_Circles.this,"Need 2 circles",Toast.LENGTH_SHORT).show();
                                }else{
                                    result=line_circle_py.callAttr("circleCalculations",10,c1,c2);
                                    state=result.asList().get(0).toBoolean();
                                    msg=result.asList().get(1).toString();
                                }break;
                            default:
                                break;

                        }
                        if(state){
                            binding.txtCircleCalAnswer.setText(msg);
                        }else{
                            if(msg!=""){
                                binding.txtCircleCalAnswer.setText("");
                                Toast.makeText(Lines_Circles.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else {
                        binding.txtCircleCalAnswer.setText("");
                        Toast.makeText(Lines_Circles.this,"Select a Calculation",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }


}