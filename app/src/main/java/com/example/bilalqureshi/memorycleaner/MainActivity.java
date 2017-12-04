package com.example.bilalqureshi.memorycleaner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Applications List   ";
    private Context context;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE


    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );
        }
    }

    //space

    public void butoncl(View view) {
    /*
   Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

    Uri uri = Uri.fromParts("package", getPackageName(), null);
    intent.setData(uri);

    startActivity(intent);

*/

    /*
    final PackageManager pm = getPackageManager();
//get a list of installed apps.
    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

    for (ApplicationInfo packageInfo : packages) {
        Log.d(TAG, "Installed package :" + packageInfo.packageName);
        Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
        Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
    }
    */


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
        final String title 	= (String)((info != null) ? getBaseContext().getPackageManager().getApplicationLabel(info.activityInfo.applicationInfo) : "???");
    }
    */


        Intent intent = new Intent(this, splashScreen.class);

        startActivity(intent);


    }

    public void freeMem() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

    }

    public long TotalMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long Total = ((long) statFs.getBlockCountLong() * (long) statFs.getBlockSizeLong());

        return Total;

    }

    public long FreeMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long Free = (statFs.getAvailableBlocksLong() * (long) statFs.getBlockSizeLong());
        return Free;
    }

    public long BusyMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
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
        d = (double) Math.round(d * 1000d) / 1000d;

        String total2 = String.valueOf(d);
        return total2;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        TextView textView1 = (TextView) findViewById(R.id.memoryview);
        TextView textView2 = (TextView) findViewById(R.id.memoryview2);

        long a = TotalMemory();
        long b = FreeMemory();



        String bj = bytesToHuman(a);
        String cd = bytesToHuman(b);
        textView1.setText(bj);
        textView2.setText(cd);
/*
        String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.twitter.android/cache/";
        File cacheDirectory = getCacheDir();
        Log.d("Files", "Path: " + cacheDirectory.getParent());
        File directory = new File(path);

        File[] files = directory.listFiles();
*/



      //  deleteDir(directory);
    }
    /*
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
*/
}



