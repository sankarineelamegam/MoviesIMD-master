package movie.n.sankari.movieimdb.utils;

import android.app.ProgressDialog;
import android.content.Context;

import movie.n.sankari.movieimdb.R;


public class Progress {
    private static ProgressDialog progressDialog = null;

    public Progress(Context context) {
        progressDialog = new ProgressDialog(context);
    }

    public static void show(Context context) {
        if (progressDialog == null) {
            new Progress(context);
        }
        if (context != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(context.getString(R.string.loading_text));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void hide() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}