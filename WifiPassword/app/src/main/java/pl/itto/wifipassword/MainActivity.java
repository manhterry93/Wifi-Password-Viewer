package pl.itto.wifipassword;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {


    @Override
    Fragment getFragment() {
        WifiPwdFragment fragment = WifiPwdFragment.newInstance();
        WifiPskPresenter presenter = new WifiPskPresenter(this, fragment);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
