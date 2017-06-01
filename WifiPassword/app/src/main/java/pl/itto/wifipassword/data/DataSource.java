package pl.itto.wifipassword.data;

import java.util.List;

/**
 * Created by PL_itto on 5/25/2017.
 */

public interface DataSource {
    interface LoadWifiListCallback {
        void onWifiListLoaded(List<WifiItem> list);
    }

    interface CopyToClipBoardCallback{
        void onCopySuccess();
    }
}
