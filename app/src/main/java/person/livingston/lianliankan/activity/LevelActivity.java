package person.livingston.lianliankan.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import person.livingston.lianliankan.R;
import person.livingston.lianliankan.bean.GameConf;

public class LevelActivity extends AppCompatActivity implements View.OnClickListener {


    private Button simple_game_btn;
    private Button general_game_btn;
    private Button difficulty_game_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        simple_game_btn = (Button) findViewById(R.id.simple_game_btn);
        general_game_btn = (Button) findViewById(R.id.general_game_btn);
        difficulty_game_btn = (Button) findViewById(R.id.difficulty_game_btn);
        simple_game_btn.setOnClickListener(this);
        general_game_btn.setOnClickListener(this);
        difficulty_game_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        switch (v.getId()) {
            case R.id.simple_game_btn:
                intent.putExtra("time", GameConf.DEFAULT_TIME);
                startActivity(intent);
                break;
            case R.id.general_game_btn:
                intent.putExtra("time", GameConf.GENERAL_TIME);
                startActivity(intent);
                break;
            case R.id.difficulty_game_btn:
                intent.putExtra("time", GameConf.DIFFICULTY_TIME);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
