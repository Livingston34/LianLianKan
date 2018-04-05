package person.livingston.lianliankan.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import person.livingston.lianliankan.R;

/**
 * Created by livingston on 2018/4/5.
 */

public class OperateDialog extends Dialog {

    private View layout;
    private ImageView game_continue_iv;
    private ImageView game_home_iv;
    private ImageView game_help_iv;
    private ImageView game_reset_iv;
    private RelativeLayout game_music_rl;
    private ImageView game_music_close_iv;
    private RelativeLayout game_clean_music_rl;
    private ImageView game_clean_music_close_iv;
    private View.OnClickListener onClickListener;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.game_continue_iv:
                    cancel();
                    break;
                case R.id.game_home_iv:
                    cancel();
                    break;
                case R.id.game_help_iv:
                    cancel();
                    break;
                case R.id.game_reset_iv:
                    cancel();
                    break;
                case R.id.game_music_rl:
                    break;
                case R.id.game_clean_music_rl:
                    break;
            }
            if (null != onClickListener) onClickListener.onClick(v);
        }
    };

    public OperateDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.dialog_have_break, null);
        setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        if (null == game_continue_iv) {
            game_continue_iv = layout.findViewById(R.id.game_continue_iv);
            game_home_iv = layout.findViewById(R.id.game_home_iv);
            game_help_iv = layout.findViewById(R.id.game_help_iv);
            game_reset_iv = layout.findViewById(R.id.game_reset_iv);
            game_music_rl = layout.findViewById(R.id.game_music_rl);
            game_music_close_iv = layout.findViewById(R.id.game_music_close_iv);
            game_clean_music_rl = layout.findViewById(R.id.game_clean_music_rl);
            game_clean_music_close_iv = layout.findViewById(R.id.game_clean_music_close_iv);
        }
        game_continue_iv.setOnClickListener(listener);
        game_home_iv.setOnClickListener(listener);
        game_help_iv.setOnClickListener(listener);
        game_reset_iv.setOnClickListener(listener);
        game_music_rl.setOnClickListener(listener);
        game_clean_music_rl.setOnClickListener(listener);
    }


    public OperateDialog setOnClickListener(View.OnClickListener clickListener) {
        this.onClickListener = clickListener;
        return this;
    }

    public void setMusicPauseToggle(boolean flag) {
        game_music_close_iv.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    public void setCleanMusicPauseToggle(boolean flag) {
        game_clean_music_close_iv.setVisibility(flag ? View.VISIBLE : View.GONE);

    }

}
