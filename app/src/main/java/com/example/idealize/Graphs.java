package com.example.idealize;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.TableOrder;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYSeries;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.idealize.databinding.ActivityGraphsBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;


public class Graphs extends AppCompatActivity {

    private ActivityGraphsBinding binding;
    private PyObject graph_python;
    private boolean is_new=true;
    private final int max_count=2;
    private int graph_count=0;
    private Double[] minX=new Double[max_count];
    private Double[] minY=new Double[max_count];
    private Double[] maxX=new Double[max_count];
    private Double[] maxY=new Double[max_count];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding=ActivityGraphsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
        Python py=Python.getInstance();
        graph_python=py.getModule("graphs");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");

            }
        });


        if(is_new){
            binding.btnSameGraph.setEnabled(false);
        }
        binding.btnSameGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_new){
                    plot();
                    binding.plotGraph.redraw();
                    graph_count++;
                    if (graph_count==max_count){
                        binding.btnSameGraph.setEnabled(false);
                    }
                }

            }
        });
        binding.btnNewGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph_count=0;
                minX=new Double[max_count];
                minY=new Double[max_count];
                maxX=new Double[max_count];
                maxY=new Double[max_count];
                binding.plotGraph.clear();
                plot();
                binding.plotGraph.redraw();
                is_new=true;
                binding.btnSameGraph.setEnabled(true);
                graph_count++;

            }
        });

    }
    /*public void plot(){
        String input=binding.txtGraphEquation.getText().toString();
        String start=binding.txtGraphStart.getText().toString();
        String end=binding.txtGraphEnd.getText().toString();

        PyObject result=graph_python.callAttr("graphCreator",input,start,end);
        boolean state=result.asList().get(0).toBoolean();
        if (state){
            PyObject table=result.asList().get(1);
            int size=table.asList().size();
            String tbl="X\t\t\t\t\tY";
            for (int i=0;i<size;i++){
                PyObject x=table.asList().get(i).asList().get(0);
                PyObject y=table.asList().get(i).asList().get(1);
                if ((x!=null) && (y!=null)){
                    tbl=tbl+"\n"+x.toString()+"\t\t\t\t\t"+y.toString();
                }else if ((x==null)){
                    tbl=tbl+"\n"+"---"+"\t\t\t\t\t"+y.toString();
                }else if ((y==null)){
                    tbl=tbl+"\n"+x.toString()+"\t\t\t\t\t"+"---";
                }else{
                    tbl=tbl+"\n"+"---"+"\t\t\t\t\t"+"---";
                }

            }
            binding.txtGraphTable.setText(tbl);

            // PointsGraphSeries<DataPoint> xy_values=new PointsGraphSeries<>();

            List<PyObject> dom= result.asList().get(2).asList().get(0).asList();
            List<PyObject> ran= result.asList().get(2).asList().get(1).asList();

            DataPoint[] dp=new DataPoint[dom.size()];
            for(int i=0;i<dom.size();i++){
                try {
                    Float.parseFloat(ran.get(i).toString());
                    if (ran.get(i)!=null){
                        dp[i]=(new DataPoint(dom.get(i).toFloat(),ran.get(i).toFloat()));
                    }
                }catch (Exception e){

                }

            }


            LineGraphSeries<DataPoint> series=new LineGraphSeries<DataPoint>(dp);
            series.setTitle(input);
            int[] colors={Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW};
            series.setColor(colors[graph_count]);
            // after adding data to our line graph series.
            // on below line we are setting
            // title for our graph view.
            binding.plotGraph.setTitle("Graphs");
            binding.plotGraph.setTitleColor(Color.RED);
            binding.plotGraph.setTitleTextSize(5);

            binding.plotGraph.getLegendRenderer().setVisible(true);

            // on below line we are setting
            // text color to our graph view.
            binding.plotGraph.setTitleColor(R.color.purple_200);

            // on below line we are setting
            // our title text size.
            binding.plotGraph.setTitleTextSize(18);


            binding.plotGraph.getViewport().setScalable(true);
            binding.plotGraph.getViewport().setScalableY(true);
            binding.plotGraph.getViewport().setScrollableY(true);
            binding.plotGraph.getViewport().setScrollable(true);

            // on below line we are adding
            // data series to our graph view.
            binding.plotGraph.addSeries(series);




        }else{
            String msg=result.asList().get(2).toString();

            Toast.makeText(Graphs.this,msg,Toast.LENGTH_SHORT).show();
        }*/

    public void plot(){
        String input=binding.txtGraphEquation.getText().toString();
        String start=binding.txtGraphStart.getText().toString();
        String end=binding.txtGraphEnd.getText().toString();

        PyObject result=graph_python.callAttr("graphCreator",input,start,end);
        boolean state=result.asList().get(0).toBoolean();
        if (state){
            PyObject table=result.asList().get(1);
            int size=table.asList().size();
            String tbl="X\t\t\t\t\tY";
            Double[] domainLabels=new Double[size];
            Double[] rangeLabels=new Double[size];
            for (int i=0;i<size;i++){
                PyObject x=table.asList().get(i).asList().get(0);
                PyObject y=table.asList().get(i).asList().get(1);
                if ((x!=null) && (y!=null)){
                    tbl=tbl+"\n"+x.toString()+"\t\t\t\t\t"+y.toString();
                    domainLabels[i]=Double.valueOf(x.toString());
                    rangeLabels[i]=Double.valueOf(y.toString());
                }else if ((x==null)){
                    tbl=tbl+"\n"+"---"+"\t\t\t\t\t"+y.toString();
                }else if ((y==null)){
                    tbl=tbl+"\n"+x.toString()+"\t\t\t\t\t"+"---";
                }else{
                    tbl=tbl+"\n"+"---"+"\t\t\t\t\t"+"---";
                }

            }
            binding.txtGraphTable.setText(tbl);

            minX[graph_count]=Double.parseDouble(start);
            maxX[graph_count]=Double.parseDouble(end);

            Double maxDomain=max(maxX);
            Double minDomain=min(minX);

            binding.plotGraph.getBounds().setMinX(minDomain);
            binding.plotGraph.getBounds().setMaxX(maxDomain);
            // PointsGraphSeries<DataPoint> xy_values=new PointsGraphSeries<>();



            /*List<PyObject> ran= result.asList().get(2).asList().get(1).asList();

            Double[] rangeLabelsForMaxMin=new Double[ran.size()];

            for(int i=0;i<ran.size();i++){
                rangeLabelsForMaxMin[i]=Double.valueOf(String.valueOf(ran.get(i)));
            }*/

            minY[graph_count]=min(rangeLabels);
            maxY[graph_count]=max(rangeLabels);

            Double maxRange=max(maxY);
            Double minRange=min(minY);

            binding.plotGraph.getBounds().setMinY(minRange);
            binding.plotGraph.getBounds().setMaxY(maxRange);



            XYSeries series = new SimpleXYSeries(
                    Arrays.asList(rangeLabels), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, input);


            int[] colors={Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW};

            LineAndPointFormatter seriesFormat = new LineAndPointFormatter(colors[graph_count], Color.GREEN, null, null);
            seriesFormat.setInterpolationParams(
                    new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Uniform));
            binding.plotGraph.addSeries(series, seriesFormat);


            binding.plotGraph.getBounds().setMaxX(Float.valueOf( end));
            binding.plotGraph.getBounds().setMinX(Float.valueOf( start));


           binding.plotGraph.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
                @Override
                public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                    int i = Math.round(((Number) obj).floatValue());
                    return toAppendTo.append(domainLabels[i]);
                }
                @Override
                public Object parseObject(String source, ParsePosition pos) {
                    return null;
                }
            });

           binding.plotGraph.getLegend().setTableModel(new DynamicTableModel(2,2, TableOrder.ROW_MAJOR));





        }else{
            String msg=result.asList().get(2).toString();

            Toast.makeText(Graphs.this,msg,Toast.LENGTH_SHORT).show();
        }

    }
    public Double max(Double[] set){
        Double m=set[0];
        int i=1;
        while(set[i]!=null && i<graph_count){
            if (m>set[i]){
                m=set[i];
            }
        }
        return m;
    }
    public Double min(Double[] set){
        Double m=set[0];
        int i=1;
        while(set[i]!=null && i<graph_count){
            if (m<set[i]){
                m=set[i];
            }
        }
        return m;
    }
    }






