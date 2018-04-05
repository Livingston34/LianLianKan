package person.livingston.lianliankan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import person.livingston.lianliankan.LivingstonApplication;
import person.livingston.lianliankan.R;
import person.livingston.lianliankan.common.Constant;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button game_model_btn;
    private Button game_rule_btn;
    private Button about_game_btn;
    private RelativeLayout game_music_rl;
    private ImageView game_music_close_iv;
    private RelativeLayout game_clean_music_rl;
    private ImageView game_clean_music_close_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_model_btn = (Button) findViewById(R.id.game_model_btn);
        game_rule_btn = (Button) findViewById(R.id.game_rule_btn);
        about_game_btn = (Button) findViewById(R.id.about_game_btn);
        game_music_rl = (RelativeLayout) findViewById(R.id.game_music_rl);
        game_music_close_iv = (ImageView) findViewById(R.id.game_music_close_iv);
        game_clean_music_rl = (RelativeLayout) findViewById(R.id.game_clean_music_rl);
        game_clean_music_close_iv = (ImageView) findViewById(R.id.game_clean_music_close_iv);
        game_model_btn.setOnClickListener(this);
        game_rule_btn.setOnClickListener(this);
        about_game_btn.setOnClickListener(this);
        game_music_rl.setOnClickListener(this);
        game_clean_music_rl.setOnClickListener(this);

        boolean bgSwitch = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_BG_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
        boolean cleanSwitch = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
        game_music_close_iv.setVisibility(!bgSwitch ? View.VISIBLE : View.GONE);
        game_clean_music_close_iv.setVisibility(!cleanSwitch ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_model_btn:
                startActivity(new Intent(this, LevelActivity.class));
                break;
            case R.id.game_rule_btn:
                startActivity(new Intent(this, RuleActivity.class));
                break;
            case R.id.about_game_btn:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.game_music_rl:
                boolean bflag = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_BG_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
                LivingstonApplication.spUtils.put(Constant.Key.GAME_BG_MUSIC_SWITCH, !bflag);
                game_music_close_iv.setVisibility(bflag ? View.VISIBLE : View.GONE);
                break;
            case R.id.game_clean_music_rl:
                boolean cflag = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
                LivingstonApplication.spUtils.put(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, !cflag);
                game_clean_music_close_iv.setVisibility(cflag ? View.VISIBLE : View.GONE);
                break;
            default:
                break;
        }
    }
}
