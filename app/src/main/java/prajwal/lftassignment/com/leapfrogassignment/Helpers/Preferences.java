package prajwal.lftassignment.com.leapfrogassignment.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by prajwal on 7/15/2017.
 */

public class Preferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static String PREFERENCE_NAME = "demo";
    private static int PREFERENCE_MODE = Context.MODE_PRIVATE;

    private final String PERMISSION_STRICTLY_DENIED = "permission_denied";

    public Preferences(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, PREFERENCE_MODE);
        editor = preferences.edit();
    }

    public void setPERMISSION_STRICTLY_DENIED(boolean status) {
        editor.putBoolean(PERMISSION_STRICTLY_DENIED, status);
        editor.commit();
    }

    public Boolean getPermissionStatus() {
        return preferences.getBoolean(PERMISSION_STRICTLY_DENIED, false);
    }
}
