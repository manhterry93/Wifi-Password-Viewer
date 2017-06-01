package pl.itto.wifipassword.util;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import pl.itto.wifipassword.data.DataSource.LoadWifiListCallback;
import pl.itto.wifipassword.data.WifiItem;

import static pl.itto.wifipassword.data.ConstantParams.WifiLockType;

/**
 * Created by PL_itto on 5/25/2017.
 */

public class Utils {
    private static final String WIFI_PWD_PATH = "/data/misc/wifi/wpa_supplicant.conf";
    //private static final String WIFI_PWD_PATH = "/sdcard/Download/test.txt";
    private static final String TAG = "PL_itto.Utils";

    private static final String SSID = "ssid";
    private static final String PSK = "psk";
    private static final String KEY_MGMT = "key_mgmt";
    private static final String PRIORITY = "priority";

    public static void loadSavedWifi(Context context,final LoadWifiListCallback callback) {
        new LoadListAsync(context,callback).execute();
    }

    private static class LoadListAsync extends AsyncTask<Void, Void, List<WifiItem>> {
        LoadWifiListCallback mCallback;
        Context mContext;
        public LoadListAsync(Context context,LoadWifiListCallback callback) {
            this.mCallback = callback;
            this.mContext=context;
        }

        @Override
        protected List<WifiItem> doInBackground(Void... params) {
            List<WifiItem> list = new ArrayList<>();
            try {
                Process process = Runtime.getRuntime().exec("su");
                OutputStream stdin = process.getOutputStream();
                InputStream stderr = process.getErrorStream();
                InputStream stdout = process.getInputStream();

                stdin.write(("cat " + WIFI_PWD_PATH + "\n").getBytes());
                stdin.flush();
                stdin.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                String line;
                WifiItem item = null;
                boolean newItem = false;
                while ((line = br.readLine()) != null) {
                    if (line.contains("{")) {
                        newItem = true;
                        item = new WifiItem(mContext);
                    } else if (line.contains("}")) {
                        newItem = false;
                        if (item != null) {
                            list.add(item);
                        }
                    } else {
                        if (newItem) {
                            String[] temp = line.split("=");
                            if (temp != null && temp.length > 1) {
                                setValue(temp, item);
                            }
                        }
                    }

                }
                br.close();

                br = new BufferedReader(new InputStreamReader(stderr));
                while ((line = br.readLine()) != null) {
                }
                br.close();
                process.waitFor();//wait for process to finish
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return list;
        }

        @Override
        protected void onPostExecute(List<WifiItem> wifiItems) {
            super.onPostExecute(wifiItems);
            mCallback.onWifiListLoaded(wifiItems);
        }
    }
//
//    public static void loadSavedWifis(LoadWifiListCallback callback) {
//        try {
//            Runtime.getRuntime().exec("su");//root
//            Runtime.getRuntime().exec("system/bin/mount -o rw,remount -t rootfs /data");
//            Runtime.getRuntime().exec("system/bin/chmod 777 /data/misc/wifi/wpa_supplicant.conf");
//        } catch (IOException e) {
//            Log.e(TAG, "error: " + e.toString());
//            e.printStackTrace();
//        }
//        File file = new File(WIFI_PWD_PATH);
//        List<WifiItem> list = new ArrayList<>();
//        if (!file.exists()) {
//            Log.e(TAG, "File not found: " + WIFI_PWD_PATH);
//        } else {
//            try {
//                FileInputStream fis = new FileInputStream(file);
//                InputStreamReader reader = new InputStreamReader(fis);
//                BufferedReader bf = new BufferedReader(reader);
//                String line;
//                WifiItem item = null;
//                boolean newItem = false;
//                while ((line = bf.readLine()) != null) {
//                    if (line.contains("{")) {
//                        newItem = true;
//                        item = new WifiItem();
//                    } else if (line.contains("}")) {
//                        newItem = false;
//                        if (item != null) {
//                            list.add(item);
//                            Log.i(TAG, "item add: " + item.getSSID());
//                        }
//                    } else {
//                        if (newItem) {
//                            Log.i(TAG, "temP: " + line);
//                            String[] temp = line.split("=");
//                            if (temp != null && temp.length > 1) {
//                                setValue(temp, item);
//                            }
//                        }
//                    }
//                }
//                bf.close();
//                reader.close();
//                fis.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//
//            }
//        }
//        callback.onWifiListLoaded(list);
//    }

    public static void setValue(String data[], WifiItem item) {

        switch (data[0].trim()) {
            case SSID:
                data[1]=format(data[1]);
                item.setSSID(data[1]);
                break;

            case PSK:
                data[1]=format(data[1]);
                item.setPsk(data[1]);
                break;
            case PRIORITY:
                item.setPriority(data[1]);
                break;
            case KEY_MGMT:
                item.setLockType(getType(data[1]));
                item.setLocked(item.getLockType() == WifiLockType.NONE ? false : true);
                break;
        }
    }

    public static WifiLockType getType(String type) {
        switch (type) {
            case "NONE":
                return WifiLockType.NONE;
            case "WPA-PSK":
                return WifiLockType.WPA;
            case "WEP":
                return WifiLockType.WEP;
            default:
                return WifiLockType.OTHER;

        }
    }

    private static String format(String in){
        if(in!=null && in.length()>=3){
            in=in.substring(1,in.length()-1);
        }
        return in;
    }
}
