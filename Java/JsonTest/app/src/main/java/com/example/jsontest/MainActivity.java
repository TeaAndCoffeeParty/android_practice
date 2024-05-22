package com.example.jsontest;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "PreInstall";
    private String preinstallJsonFile = "/storage/emulated/0/preinstall.json";
    private String preinstallApkPackageNameKey = "packageName";
    private String preinstallApkVersionNameKey = "versionName";
    private String preinstallApkVersionCodeKey = "versionCode";

    private String preinstallApkFilePathKey = "apkFilePath";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        JSONArray contentArray = testingJsonFile(preinstallJsonFile);
        ArrayList<JSONObject> apkJSONList = getTestingJsonObjectList();
        for(int i=0;i<apkJSONList.size();i++) {
            JSONObject apkObj = apkJSONList.get(i);
            boolean isPreInstalled = searchAllObject(contentArray, apkObj);
            if (!isPreInstalled) {
                doInstall(apkObj);
                contentArray.put(apkObj);
            }
        }

        writeJsonArrayToFile(contentArray, preinstallJsonFile);
    }

    private ArrayList<JSONObject> getTestingJsonObjectList() {
        ArrayList<JSONObject> apkJSONList = new ArrayList<>();
        JSONObject vulcanApk = getPreInstallJsonObject(
                "com.soonsolid.curemini",
                "1.0.9",
                19,
                "/vendor/preloadApp/Vulcan.apk");
        JSONObject otaApk = getPreInstallJsonObject(
                "com.sprintray.ota",
                "1.0.1",
                11,
                "/vendor/preloadApp/OTA.apk");
        JSONObject resinApk = getPreInstallJsonObject(
                "com.sprintray.roc",
                "1.0.1",
                12,
                "/vendor/preloadApp/Resin.apk");
        JSONObject securityApk = getPreInstallJsonObject(
                "com.sprintray.security",
                "1.0.1.28",
                19,
                "/vendor/preloadApp/Security.apk");
        apkJSONList.add(vulcanApk);
        apkJSONList.add(otaApk);
        apkJSONList.add(resinApk);
        apkJSONList.add(securityApk);
        return apkJSONList;
    }

    private JSONArray testingJsonFile(String filePath) {
        File file = new File(filePath);
        JSONArray contentArray = new JSONArray();
        if(file.exists()) {
            try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                try {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                    String jsonString = sb.toString().trim();
                    contentArray = new JSONArray(jsonString);
                    Log.d(TAG, "The content is a valid JSONArray");
                } catch (JSONException e) {
                    Log.d(TAG, "The content is not a valid JSONArray");
                    deleteJsonFile(file);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e.getMessage());
            }
        } else {
            Log.d(TAG, filePath + " not exited");
        }
        return contentArray;
    }

    private void deleteJsonFile(File file) {
        if(file.delete())
            Log.d(TAG, "delete "+ file.getAbsolutePath());
        else {
            Log.d(TAG, "delete "+ file.getAbsolutePath() + " failed");
        }
    }

    private JSONObject getPreInstallJsonObject(String name, String versionName, int versionCode,
                                               String apkFilePath) {
        JSONObject apkObject = new JSONObject();
        try {
            apkObject.put(preinstallApkPackageNameKey, name);
            apkObject.put(preinstallApkVersionNameKey, versionName);
            apkObject.put(preinstallApkVersionCodeKey, versionCode);
            apkObject.put(preinstallApkFilePathKey, apkFilePath);
        } catch (JSONException e) {
            Log.e(TAG, "create preinstall object failed." + e.getMessage());
        }
        return apkObject;
    }

    private boolean searchAllObject(JSONArray jsonArray, JSONObject apkObject) {
        try {
            String packageName = apkObject.getString(preinstallApkPackageNameKey);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString(preinstallApkPackageNameKey).equals(packageName)) {
                    Log.d(TAG, "Package "+packageName + " already exists. SKipping");
                    return true;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception." + e.getMessage());
        }
        return false;
    }

    private void doInstall(JSONObject apkObj) {
        try {
            Log.d(TAG, "do install " + apkObj.getString(preinstallApkFilePathKey));
        } catch (JSONException e) {
            Log.d(TAG, "get key:" + preinstallApkFilePathKey + " failed");
        }
    }

    private void writeJsonArrayToFile(JSONArray jsonArray, String filePath) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(jsonArray.toString(2));
        } catch (IOException e) {
            Log.e(TAG, "IOException." + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Writer file error." + e.getMessage());
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, "write close error." + e.getMessage());
                }
            }
        }
    }
}