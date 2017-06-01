package pl.itto.wifipassword;

import android.graphics.Bitmap;

/**
 * Created by PL_itto on 5/24/2017.
 */

public class AppItem {
    private String name;
    private Bitmap icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
