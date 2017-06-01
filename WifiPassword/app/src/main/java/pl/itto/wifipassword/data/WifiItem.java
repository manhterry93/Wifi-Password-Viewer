package pl.itto.wifipassword.data;

import android.content.Context;

import pl.itto.wifipassword.R;
import pl.itto.wifipassword.data.ConstantParams.WifiLockType;

/**
 * Created by PL_itto on 5/24/2017.
 */

public class WifiItem {
    private String mSSID;
    private String mPsk;
    private WifiLockType mLockType;
    private String mPriority;
    private boolean mLocked;
    private static final int RES_WIFI_LOCK = R.drawable.ic_wifi_lock;
    private static final int RES_WIFI_OPEN = R.drawable.ic_wifi_no_psk;

    public WifiItem(Context context) {
        mPsk = context.getString(R.string.title_wifi_open);
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String SSID) {
        mSSID = SSID;
    }

    public String getPsk() {
        return mPsk;
    }

    public void setPsk(String psk) {
        mPsk = psk;
    }

    public WifiLockType getLockType() {
        return mLockType;
    }

    public void setLockType(WifiLockType lockType) {
        mLockType = lockType;
    }

    public boolean isLocked() {
        return mLocked;
    }

    public void setLocked(boolean locked) {
        mLocked = locked;
    }

    public String getPriority() {
        return mPriority;
    }

    public void setPriority(String priority) {
        mPriority = priority;
    }

    public int getResWifi() {
        if (getLockType() != WifiLockType.NONE) {
            return RES_WIFI_LOCK;
        }
        return RES_WIFI_OPEN;
    }

    public String getLockTypeString() {
        switch (getLockType()) {
            case WPA:
                return "WPA-PSK";
            case WEP:
                return "WEP";
            case NONE:
                return "NONE";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    public String toString() {
        return "Wifi: "+mSSID+" psk: "+mPsk+" security: "+getLockTypeString()+" priority: "+mPriority;
    }
}
