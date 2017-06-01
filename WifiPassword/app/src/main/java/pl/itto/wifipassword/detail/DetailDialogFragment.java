package pl.itto.wifipassword.detail;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import pl.itto.wifipassword.R;
import pl.itto.wifipassword.data.WifiItem;

/**
 * Created by PL_itto on 5/30/2017.
 */

public class DetailDialogFragment extends DialogFragment {

    private static final String TAG="PL_itto.DetailDialogFragment";
    public static final String EXTRA_SSID = "ssid";
    public static final String EXTRA_PSK = "psk";
    public static final String EXTRA_SECURITY = "security";
    public static final String EXTRA_PRIORITY = "priority";

    TextView mTxtSSID, mTxtPsk, mTxtSec, mTxtPri;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_detail, null);

        mTxtSSID = (TextView) v.findViewById(R.id.txt_ssid);
        mTxtPsk = (TextView) v.findViewById(R.id.txt_psk);
        mTxtSec = (TextView) v.findViewById(R.id.txt_security);
        mTxtPri = (TextView) v.findViewById(R.id.txt_priority);
        String ssid = getArguments().getString(EXTRA_SSID);
        String psk = getArguments().getString(EXTRA_PSK);
        String sec = getArguments().getString(EXTRA_SECURITY);
        String pri = getArguments().getString(EXTRA_PRIORITY);

        mTxtSSID.setText(ssid);
        mTxtSec.setText(sec);
        mTxtPsk.setText(psk);
        mTxtPri.setText(pri);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("OK", null)
                .create();
    }

    public static DetailDialogFragment newInstance(WifiItem item) {
        Bundle bundle = new Bundle();
        Log.i(TAG,"item: "+item.toString());
        bundle.putString(EXTRA_SSID, item.getSSID());
        bundle.putString(EXTRA_PSK, item.getPsk());
        bundle.putString(EXTRA_SECURITY, item.getLockTypeString());
        bundle.putString(EXTRA_PRIORITY, item.getPriority());
        DetailDialogFragment fragment = new DetailDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
