package com.example.bilalqureshi.memorycleaner;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.IPackageStatsObserver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;


public class Main22Activity extends AppCompatActivity {
    public void toastmsg(String ss){
        Toast.makeText(this,ss, Toast.LENGTH_SHORT).show();
    }
    public  long TotalMemory()
    {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long   Total  = ( (long) statFs.getBlockCountLong() * (long) statFs.getBlockSizeLong());

        return Total;

    }

    public long FreeMemory()
    {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long   Free   = (statFs.getAvailableBlocksLong() * (long) statFs.getBlockSizeLong());
        return Free;
    }

    public long BusyMemory()
    {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long   Total  = ((long) statFs.getBlockCountLong() * (long) statFs.getBlockSizeLong());
        long   Free   = (statFs.getAvailableBlocksLong()   * (long) statFs.getBlockSizeLong());
        long   Busy   = Total - Free;
        return Busy;
    }



    public static String bytesToHuman (long size)
    {
        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return (floatForm((double)size / Kb)) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

        return "---";
    }
    public static String floatForm (double d)
    {

        d= (double)Math.round(d * 1000d) / 1000d;

        String total2 = String.valueOf(d);
        return total2;


    }
    long totalSize;
    private long getTotalSize() {


        PackageManager packageManager = getApplicationContext().getPackageManager();

        List<PackageInfo> packs = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            Method getPackageSizeInfo;
            try {
                getPackageSizeInfo = packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                getPackageSizeInfo.invoke(packageManager, p.packageName, new IPackageStatsObserver.Stub() {
                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        totalSize = (long) (totalSize + pStats.cacheSize);
                        Log.d("return size", totalSize + "");

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("return size", totalSize + "");



        return totalSize;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
       // clearCachen();
long cc;
        cc=getTotalSize();
        long b = FreeMemory();







        TextView textView = (TextView) findViewById(R.id.textView6);
        String cd=bytesToHuman(b);
        textView.setText("Internal Storage left " + cd);

        TextView textViewi  = (TextView) findViewById(R.id.textView3);

        toastmsg("Junk Memory Cleared");

       // String cbb;
        //cbb = bytesToHuman(xx[0]);

//        textViewi.setText(cbb);


    }






}
