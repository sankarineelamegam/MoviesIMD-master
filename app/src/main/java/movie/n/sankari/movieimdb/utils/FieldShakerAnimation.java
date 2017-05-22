package movie.n.sankari.movieimdb.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import movie.n.sankari.movieimdb.R;


public class FieldShakerAnimation {
    Context context;
    static Animation shake;


    public static void startShaking(Context c, TextView field) {
        shake = AnimationUtils.loadAnimation(c, R.anim.shake);
        field.setHintTextColor(c.getResources().getColor(R.color.red));
        field.startAnimation(shake);

    }
}
