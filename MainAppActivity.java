package com.example.bilalqureshi.memorycleaner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.nfc.Tag;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.IPackageStatsObserver;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainAppActivity extends AppCompatActivity {

    final ArrayList<String> arrayList = new ArrayList<>();

    public void clearBut(View view) {
        Intent intent = new Intent(getApplicationContext(), Main22Activity.class);
        startActivity(intent);

    }

    public String getCacheSize() throws PackageManager.NameNotFoundException {
        File cacheDir;
        long size = 0;
        String cacheSize = null;
        Context mContext = createPackageContext("com.twitter.android", CONTEXT_IGNORE_SECURITY);
        cacheDir = mContext.getCacheDir();
        if (cacheDir.exists()) {
            File[] files = cacheDir.listFiles();
            for (File f : files) {
                size += f.length();
            }
            cacheSize = String.valueOf(size / 1024 / 1024) + "M";
            Toast.makeText(this, cacheSize, Toast.LENGTH_SHORT).show();
        }


        return cacheSize;
    }

    long totalSize = 0;


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
public void cacheSize(){
    long size = 0;
    PackageManager packageManager = getPackageManager();
    List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

    for (ApplicationInfo packageInfo : packages) {
        Context mContext = null;
        try {
            mContext = createPackageContext(packageInfo.packageName, CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();

        }
        File cacheDirectory = mContext.getCacheDir();
        File[] files = cacheDirectory.listFiles();
        for (File f : files) {
            size = size + f.length();

        }
        Log.i("Spot", "CacheSize: " + size);
    }
}*/

    public long getSize() {
        PackageManager packageManager = getApplicationContext().getPackageManager();

        List<PackageInfo> packs = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

        for (int i = 0; i < packs.size(); i++) {
            final PackageInfo p = packs.get(i);
            Method getPackageSizeInfo;
            try {
                getPackageSizeInfo = packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                getPackageSizeInfo.invoke(packageManager, p.packageName, new IPackageStatsObserver.Stub() {
                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        totalSize = (long) (totalSize + pStats.cacheSize);
                        arrayList.add(p.packageName);
                        Log.d("return size", pStats.cacheSize + "");

                        Log.w("Package  ", p.packageName);
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
        setContentView(R.layout.activity_main_app);

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final ListView listView = (ListView) findViewById(R.id.ListView1);
TextView textView  = (TextView) findViewById(R.id.textView3);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        PackageManager packageManager = getApplicationContext().getPackageManager();

        List<PackageInfo> packs = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        final long[] xx = {0};

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



                            String cd = bytesToHuman(pStats.externalDataSize);
                            String ss = strPackageName + "       " + cd;
                            arrayList.add(ss);
                            arrayAdapter.notifyDataSetChanged();

                            Log.i("Spot", "Data Size: " + pStats.externalDataSize);

             xx[0] = pStats.externalDataSize+ xx[0];
                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();

                }

                arrayList.add(strPackageName);
                final String title = (String) ((info != null) ? getBaseContext().getPackageManager().getApplicationLabel(info.activityInfo.applicationInfo) : "???");

            }






/*
        PackageManager pm = getPackageManager();


        Method getPackageSizeInfo = null;
        try {
            getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            getPackageSizeInfo.invoke(pm, "com.careem.acma", new IPackageStatsObserver.Stub() {

                @Override
                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                        throws RemoteException {


                     Log.i("Spot", "CacheSize: " + pStats.cacheSize);
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/


        }




String cpp = bytesToHuman(xx[0]);

        textView.setText(cpp);


        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);
    }


}

