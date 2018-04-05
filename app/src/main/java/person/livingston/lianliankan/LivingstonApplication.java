package person.livingston.lianliankan;

import android.app.Application;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;

/**
 * Created by livingston on 2018/4/5.
 */

public class LivingstonApplication extends Application {

    public static LivingstonApplication app;
    public static SPUtils spUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        Utils.init(this);
        spUtils = SPUtils.getInstance(this.getPackageName());
    }
}
