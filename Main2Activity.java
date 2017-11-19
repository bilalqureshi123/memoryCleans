package com.example.bilalqureshi.memorycleaner;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2Activity extends AppCompatActivity {


    public void butClick(View view) {
    /*
    final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    final List pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);
    for (Object object : pkgAppsList)
    {
        ResolveInfo info = (ResolveInfo) object;
        Drawable icon    = getBaseContext().getPackageManager().getApplicationIcon(info.activityInfo.applicationInfo);
        String strAppName  	= info.activityInfo.applicationInfo.publicSourceDir.toString();
        String strPackageName  = info.activityInfo.applicationInfo.packageName.toString();
        Toast.makeText(this,strPackageName,Toast.LENGTH_SHORT).show();
        final String title 	= (String)((info != null) ? getBaseContext().getPackageManager().getApplicationLabel(info.activityInfo.applicationInfo) : "???");
    }
    */

        Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
        startActivity(intent);

    }

    public long TotalMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Total = ((long) statFs.getBlockCountLong() * (long) statFs.getBlockSizeLong());

        return Total;

    }

    public long FreeMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Free = (statFs.getAvailableBlocksLong() * (long) statFs.getBlockSizeLong());
        return Free;
    }

    public long BusyMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Total = ((long) statFs.getBlockCountLong() * (long) statFs.getBlockSizeLong());
        long Free = (statFs.getAvailableBlocksLong() * (long) statFs.getBlockSizeLong());
        long Busy = Total - Free;
        return Busy;
    }


    public static String bytesToHuman(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return (floatForm((double) size / Kb)) + " Kb";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " Mb";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " Gb";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " Tb";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " Pb";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";

        return "---";
    }

    public static String floatForm(double d) {

        d= (double)Math.round(d * 1000d) / 1000d;

        String total2 = String.valueOf(d);
        return total2;


    }
/*
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }

    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView textView = (TextView) findViewById(R.id.textView5);
        PackageManager packageManager = getApplicationContext().getPackageManager();

        List<PackageInfo> packs = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final long[] totalsize = {0};
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        for (Object object : pkgAppsList) {
            ResolveInfo info = (ResolveInfo) object;
            Drawable icon = getBaseContext().getPackageManager().getApplicationIcon(info.activityInfo.applicationInfo);
            String strAppName = info.activityInfo.applicationInfo.publicSourceDir.toString();
            final String strPackageName = info.activityInfo.applicationInfo.packageName.toString();

            {
                PackageManager pm = getPackageManager();


                Method getPackageSizeInfo = null;
                try {
                    getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                try {
                    getPackageSizeInfo.invoke(pm, strPackageName, new IPackageStatsObserver.Stub() {

                        @Override
                        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                throws RemoteException {


                           // Log.i("Spot", "CacheSize: " + pStats.cacheSize);
                            totalsize[0] = pStats.externalDataSize+ totalsize[0];



                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();

                }


                final String title = (String) ((info != null) ? getBaseContext().getPackageManager().getApplicationLabel(info.activityInfo.applicationInfo) : "???");

            }
String cd ="Total Junk files are :            " + bytesToHuman(totalsize[0]);

            textView.setText(cd);
        }
    }
}
