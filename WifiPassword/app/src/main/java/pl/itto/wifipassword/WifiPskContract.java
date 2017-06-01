package pl.itto.wifipassword;

import java.util.List;

import pl.itto.wifipassword.base.BasePresenter;
import pl.itto.wifipassword.base.BaseView;
import pl.itto.wifipassword.data.DataSource;
import pl.itto.wifipassword.data.WifiItem;

/**
 * Created by PL_itto on 5/24/2017.
 */

public interface WifiPskContract {
    interface View extends BaseView<PresenterToView> {
        void showRefreshing(boolean isRefreshed);
        void notifyDataChanged(List<WifiItem> list);
        void showMsg(String msg);

    }

    interface PresenterToView extends BasePresenter {
        void loadWifiList();
        void copyToClipBoard(DataSource.CopyToClipBoardCallback callback,String psk);
    }
}
