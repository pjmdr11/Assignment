package prajwal.lftassignment.com.leapfrogassignment.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by prajwal on 7/17/2017.
 */

public class Utils {


    public static boolean validateEmail(EditText et) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        if (et.getText().toString().equals("")) {
            et.setError("Please do not leave this field empty");
            return false;
        } else {
            if (EMAIL_ADDRESS_PATTERN.matcher(et.getText().toString()).matches()) {
                et.setError(null);
                return true;
            } else {
                et.setError("Email is not valid");
                return false;
            }

        }
    }

    public static boolean validateInput(EditText et) {
        if (et.getText().toString().equals("")) {
            et.setError("Please do not leave this field empty");
            return false;
        } else {
            return true;
        }
    }


    public  static String getRealPathFromURI(Context mContext, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
