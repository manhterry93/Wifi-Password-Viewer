package pl.itto.wifipassword;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

import pl.itto.wifipassword.data.DataSource;
import pl.itto.wifipassword.data.WifiItem;
import pl.itto.wifipassword.util.Utils;

/**
 * Created by PL_itto on 5/24/2017.
 */

public class WifiPskPresenter implements WifiPskContract.PresenterToView {
    WifiPskContract.View mView;
    Context mContext;

    public WifiPskPresenter(Context context, WifiPskContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadWifiList();
    }

    @Override
    public void loadWifiList() {
        mView.showRefreshing(true);
        Utils.loadSavedWifi(mContext, new DataSource.LoadWifiListCallback() {
            @Override
            public void onWifiListLoaded(List<WifiItem> list) {

                mView.showRefreshing(false);
                mView.notifyDataChanged(list);
            }
        });
    }

    @Override
    public void copyToClipBoard(DataSource.CopyToClipBoardCallback callback, String psk) {
//        ClipboardManager clipboardManager= (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) mContext
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            final android.content.ClipData clipData = android.content.ClipData
                    .newPlainText("text label", psk);
            clipboardManager.setPrimaryClip(clipData);
        } else {
            final android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) mContext
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(psk);
        }
        callback.onCopySuccess();


    }
}
