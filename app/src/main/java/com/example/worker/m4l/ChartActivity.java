package com.example.worker.m4l;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener {
    BarChart chart;
    BarChart chart2;

    TextView textViewChart1From;
    TextView textChart1Move;
    TextView textChart1Active;
    TextView text4wMove;
    TextView text4wActive;
    TextView text3mMove;
    TextView text3mActive;

//    TextView textWeek;
//    TextView textMonth;

    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 103;
    private final String nameOfMonth[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", " "};

    private Calendar calendarCurrent, calendarSelectedChart1;
    private int mYear, mMonth, mDay, mWeekOfMonth;
    private int selectedYear, selectedMonth, selectedWeek;

    private int chart2page;
    DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chart =  findViewById(R.id.chart);
        chart2 =  findViewById(R.id.chart2);
        textViewChart1From = findViewById(R.id.textChartFromDate);
        textChart1Move = findViewById(R.id.textChart1Move);
        textChart1Active = findViewById(R.id.textChart1Active);
        text4wMove = findViewById(R.id.text4wMove);
        text4wActive = findViewById(R.id.text4wActive);
        text3mMove = findViewById(R.id.text3mMove);
        text3mActive = findViewById(R.id.text3mActive);

        findViewById(R.id.buttonShare).setOnClickListener(this);
        findViewById(R.id.buttonPrevWeek).setOnClickListener(this);
        findViewById(R.id.buttonNextWeek).setOnClickListener(this);
        findViewById(R.id.buttonPrevMonth).setOnClickListener(this);
        findViewById(R.id.buttonNextMonth).setOnClickListener(this);

        chart2page = 0;
        mydb = new DBHelper(this);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                float x=e.getX();
                gotoSpecialDay((int) x);
            }

            @Override
            public void onNothingSelected()
            {
            }
        });

        chart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                float x=e.getX();
                gotoSpecialWeek((int) x);
            }

            @Override
            public void onNothingSelected()
            {
            }
        });

        initChart();

        // get current day
//        Date current = new Date();
        calendarCurrent = Calendar.getInstance();
//        cal.setTime(current);
        mMonth = calendarCurrent.get(Calendar.MONTH);
        mYear = calendarCurrent.get(Calendar.YEAR);
        mWeekOfMonth = calendarCurrent.get(Calendar.WEEK_OF_MONTH);
        mDay = calendarCurrent.get(Calendar.DAY_OF_MONTH);

        selectedYear = mYear;
        selectedMonth = mMonth;
        selectedWeek = mWeekOfMonth;

        // init Chart1
        calendarSelectedChart1 = Calendar.getInstance();
        setChart();

//        getWeeksOfMonth(selectedMonth, selectedYear);
        setChart2();

//        textMonth = findViewById(R.id.textviewMonth);
//        textWeek = findViewById(R.id.textviewWeek);
//        textWeek.setOnClickListener(this);
//        textMonth.setOnClickListener(this);

    }

    private void initChart(){
        // in this example, a LineChart is initialized from xml

        YAxis y = chart.getAxisLeft();
        y.setAxisMaxValue(400);
        y.setAxisMinValue(0);
        y.setLabelCount(5);
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new MyXAxisValueFormatter(Utils.nameOfWeekday));

        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setFitBars(true);

        // ================================================= //


        // =====================

    }

//    private int getWeekday(Date date){
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        return dayOfWeek;
//    }

    private void setChart(){
        // read week data from database
        calendarSelectedChart1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String saturday = format.format(calendarSelectedChart1.getTime());

        calendarSelectedChart1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String sunday = format.format(calendarSelectedChart1.getTime());

        ArrayList<ModelHistory> histories = new ArrayList<ModelHistory>();

        histories = mydb.historyGetAll(sunday, saturday);

        ArrayList<BarEntry> entries = new ArrayList<>();
        int sumOfMove = 0;
        int sumOfManual= 0;
        int sumOfActive = 0;

        for (int i = 0; i < 7; i++){
            if(i >= histories.size()) {
                entries.add(new BarEntry(i, new float[]{0, 0, 0}));
                continue;
            }
            ModelHistory h = histories.get(i);
            entries.add(new BarEntry( i, new float[]{h.getMove(), h.getManual(), h.getActive()}));
            sumOfMove += h.getMove();
            sumOfManual += h.getManual();
            sumOfActive += h.getActive();
        }

        // drawing
        int mm = calendarSelectedChart1.get(Calendar.MONTH);
        int dd = calendarSelectedChart1.get(Calendar.DAY_OF_MONTH);

        String str =  nameOfMonth[mm] + String.valueOf(dd) + "+";
        textViewChart1From.setText(str);
        str = String.valueOf(sumOfMove) + " Move";
        textChart1Move.setText(str);
        str = String.valueOf(sumOfActive) + ":" + String.valueOf(sumOfActive + sumOfManual);
        textChart1Active.setText(str);

        // set chart data
        BarDataSet set1;
        set1 = new BarDataSet(entries, "");
        set1.setDrawIcons(false);
        set1.setColors(getColors());
        set1.setBarBorderColor(0xff22b14c);
        set1.setBarBorderWidth(0);
        set1.setFormLineWidth(10);
//        set1.setStackLabels(new String[]{"Move", "Manual", "Active"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new StackedValueFormatter(false, "", 1));
        data.setValueTextColor(Color.WHITE);
        data.setBarWidth(0.3f);
        chart.setData(data);
        chart.getLegend().setEnabled(false);   // Hide the legend

        chart.invalidate(); // refresh
    }

//    public static int[] getWeeksOfMonth(int month, int year)
//    {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, year);
//        cal.set(Calendar.MONTH, month);
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        int n = cal.get(Calendar.WEEK_OF_MONTH);
//
//        int ndays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        int weeks[] = new int[ndays];
//        for (int i = 0; i < ndays; i++)
//        {
//            weeks[i] = cal.get(Calendar.WEEK_OF_YEAR);
//            cal.add(Calendar.DATE, 1);
//        }
//        return weeks;
//    }

    private void setChart2(){

        // =========== calculate data ===========

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, -4 * chart2page - 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String today = format.format(cal.getTime());

        cal.add(Calendar.WEEK_OF_MONTH, -4);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String str4W = format.format(cal.getTime());

        cal.add(Calendar.WEEK_OF_MONTH, -9);
        String str3M = format.format(cal.getTime());

        int[] data = mydb.historyGetSum(str4W, today );
        int[] data2 = mydb.historyGetSum(str3M, today);

        text4wMove.setText(String.valueOf(data[0]) + " Move");
        text3mMove.setText(String.valueOf(data2[0]) + " Move");

        String str = String.valueOf(data[2]) + ":" + String.valueOf(data[1] + data[2]);
        String str2 = String.valueOf(data2[2]) + ":" + String.valueOf(data2[1] + data2[2]);
        text4wActive.setText(str);
        text3mActive.setText(str2);
        int maxWeeknumber = 5;

        ArrayList<BarEntry> entriesTemp = new ArrayList<>();
        String[] strXAxisTemp = new String[] {"", "", "", "", "", "", "", "", "", ""};

        cal = Calendar.getInstance();

        // read week data from database
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String saturday0 = format.format(cal.getTime());

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String sunday0 = format.format(cal.getTime());

        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        str = nameOfMonth[mm] + String.valueOf(dd) + "+";
        strXAxisTemp[4] = str;



        cal.add(Calendar.WEEK_OF_MONTH, -4 * chart2page -4);

        for (int i = 3; i >= 0; i--) {

            // read week data from database
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            String saturday = format.format(cal.getTime());

            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            String sunday = format.format(cal.getTime());

            mm = cal.get(Calendar.MONTH);
            dd = cal.get(Calendar.DAY_OF_MONTH);
            str = nameOfMonth[mm] + String.valueOf(dd) + "+";
            strXAxisTemp[3 - i] = str;

            data = mydb.historyGetSum(sunday, saturday);
            entriesTemp.add(new BarEntry( 3 - i, new float[]{data[0], data[1], data[2]}));

            cal.add(Calendar.WEEK_OF_MONTH, 1);
        }

        data = mydb.historyGetSum(sunday0, saturday0);
        entriesTemp.add(new BarEntry( 4, new float[]{data[0], data[1], data[2]}));

        //=========== draw ==========

        XAxis xAxis = chart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        YAxis y = chart2.getAxisLeft();
        y.setAxisMaxValue(400);
        y.setAxisMinValue(0);
        y.setLabelCount(4);
        YAxis rightYAxis = chart2.getAxisRight();
        rightYAxis.setEnabled(false);

        xAxis.setValueFormatter(new MyXAxisValueFormatter(strXAxisTemp));
        xAxis.setLabelCount(maxWeeknumber);

        // set chart data
        BarDataSet set1;
        set1 = new BarDataSet(entriesTemp, "");
        set1.setDrawIcons(false);
        set1.setColors(getColors());
        set1.setBarBorderColor(0xff22b14c);
        set1.setBarBorderWidth(0);
        set1.setFormLineWidth(10);
//        set1.setStackLabels(new String[]{"Move", "Manual", "Active"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData barData = new BarData(dataSets);
        barData.setValueFormatter(new StackedValueFormatter(false, "", 1));
        barData.setValueTextColor(Color.WHITE);
        barData.setBarWidth(0.3f);
        chart2.setData(barData);
        chart2.getLegend().setEnabled(false);   // Hide the legend

        chart2.setScaleEnabled(false);
        chart2.getDescription().setEnabled(false);
        chart2.setFitBars(true);
        chart2.invalidate(); // refresh
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];
        colors[0] = 0xFF22b14c;
        colors[1] = 0xFFdafce4;
        colors[2] = 0xFF3f48cc;
        // System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }

    private void gotoPrevWeek(){
        calendarSelectedChart1.add(Calendar.WEEK_OF_MONTH, -1);
        setChart();
    }

    private void gotoNextWeek(){
        Calendar cal = Calendar.getInstance();
        Calendar calCurrent = Calendar.getInstance();
        int mm = calendarSelectedChart1.get(Calendar.MONTH);
        int yy = calendarSelectedChart1.get(Calendar.YEAR);
        int dd = calendarSelectedChart1.get(Calendar.DAY_OF_MONTH);
        cal.set(yy, mm, dd);
        cal.add(Calendar.WEEK_OF_MONTH, 1);

        if(UtilsCalendar.compare(cal, calCurrent) > 0)
            return;
        calendarSelectedChart1 = cal;
        setChart();
    }


    private void gotoSpecialDay(int n){
        Calendar cal = Calendar.getInstance();
        Calendar calCurrent = Calendar.getInstance();
        int mm = calendarSelectedChart1.get(Calendar.MONTH);
        int yy = calendarSelectedChart1.get(Calendar.YEAR);
        int dd = calendarSelectedChart1.get(Calendar.DAY_OF_MONTH);
        cal.set(yy, mm, dd);
        cal.set(Calendar.DAY_OF_WEEK, n + 1);

        if(UtilsCalendar.compare(cal, calCurrent) > 0)
            return;
        calendarSelectedChart1 = cal;

        mm = calendarSelectedChart1.get(Calendar.MONTH);
        yy = calendarSelectedChart1.get(Calendar.YEAR);
        dd = calendarSelectedChart1.get(Calendar.DAY_OF_MONTH);

        Intent i = new Intent();
        i.putExtra("year", yy);
        i.putExtra("month", mm);
        i.putExtra("day", dd);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void gotoSpecialWeek(int n){
        Calendar cal = Calendar.getInstance();
        Calendar calCurrent = Calendar.getInstance();

        if(n != 4){
            cal.add(Calendar.WEEK_OF_MONTH, n - 4 - 4 * chart2page);
        }

        if(UtilsCalendar.compare(cal, calCurrent) > 0)
            return;
        calendarSelectedChart1 = cal;
        setChart();

    }

    private void gotoPrevMonth(){
        chart2page++;
        setChart2();
    }

    private void gotoNextMonth(){
        if(chart2page <= 0) return;
        chart2page--;
        setChart2();
    }

    public void sendData(File vcfFile){
        Uri imageUri = FileProvider.getUriForFile(
                this,
                "com.example.homefolder.example.provider", //(use your app signature + ".provider" )
                vcfFile);
        int num = 12345;
      //  String fileString = "..."; //put the location of the file here
        Intent mmsIntent = new Intent(Intent.ACTION_SEND);
        mmsIntent.putExtra("sms_body", "input some text");
        mmsIntent.putExtra("address", num);
        mmsIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        mmsIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(mmsIntent, "Send"));

    }

    private void takeScreenshot() {
        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");
        String strNow = format.format(now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + strNow + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            sendData(imageFile);
//            sendSMS(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void share(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            takeScreenshot();
            // Permission has already been granted
        }

    }

    @Override
    public void onClick(View v) {
//        if(v.getId() == R.id.textviewMonth){
//            textMonth.setText(R.string.textMonth);
//            textWeek.setText("Week");
//        }
//        else if(v.getId() == R.id.textviewWeek){
//            textMonth.setText("Month");
//            textWeek.setText(R.string.textWeek);
//        }
        if(v.getId() == R.id.buttonShare){
            share();
        }
        else if(v.getId() == R.id.buttonPrevWeek){
            gotoPrevWeek();
        }
        else if(v.getId() == R.id.buttonNextWeek) {
            gotoNextWeek();
        }
        else if(v.getId() == R.id.buttonPrevMonth) {
            gotoPrevMonth();
        }
        else if(v.getId() == R.id.buttonNextMonth) {
            gotoNextMonth();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeScreenshot();
                }
                return;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

class MyXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;
    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value];
    }

}