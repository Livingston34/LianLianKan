package person.livingston.lianliankan.activity;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ScreenUtils;

import java.util.Timer;
import java.util.TimerTask;

import person.livingston.lianliankan.LivingstonApplication;
import person.livingston.lianliankan.R;
import person.livingston.lianliankan.bean.Animal;
import person.livingston.lianliankan.bean.GameConf;
import person.livingston.lianliankan.bean.LinkInfo;
import person.livingston.lianliankan.common.Constant;
import person.livingston.lianliankan.control.GameControl;
import person.livingston.lianliankan.widget.GameView;
import person.livingston.lianliankan.widget.OperateDialog;

public class GameActivity extends AppCompatActivity implements SoundPool.OnLoadCompleteListener {

    /**
     * 游戏配置对象
     */
    private GameConf config;
    /**
     * 游戏业务逻辑接口
     */
    private GameControl gameControl;
    /**
     * 游戏界面
     */
    private GameView gameView;
    /**
     * 开始按钮
     */
    private ImageView startButton;
    /**
     * 记录剩余时间的进度条
     */
    private ProgressBar timeProgessBar;
    /**
     * 失败后弹出的对话框
     */
    private AlertDialog.Builder lostDialog;
    /**
     * 游戏胜利后的对话框
     */
    private AlertDialog.Builder successDialog;
    /**
     * 定时器
     */
    private Timer timer = new Timer();
    /**
     * 记录游戏的剩余时间
     */
    private int gameTime;
    /**
     * 记录是否处于游戏状态
     */
    private boolean isPlaying;
    /**
     * 游戏时间
     */
    private int time;
    /**
     * 振动处理类
     */
    private Vibrator vibrator;
    private int music_bg;
    private int music_bg_stream;
    private int read_go;
    private int clean;
    private int win;
    private int win_stream;
    private int lose;
    private int lose_stream;
    private SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 8);
    /**
     * 记录已经选中的方块
     */
    private Animal selectedAnimal;
    /**
     * Handler类，异步处理
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    if (isPause) return;
                    timeProgessBar.setProgress(gameTime * 100 / time);
                    gameTime--; // 游戏剩余时间减少
                    // 时间小于0, 游戏失败
                    if (gameTime < 0) {
                        // 停止计时
                        stopTimer();
                        // 更改游戏的状态
                        isPlaying = false;
                        boolean flag = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
                        if (flag) {
                            lose_stream = soundPool.play(lose, 1, 1, 1, 0, 1);
                        }
                        // 失败后弹出对话框
                        lostDialog.show();
                        return;
                    }
                    break;
            }
        }
    };
    /**
     * 菜单操作弹框
     */
    private OperateDialog operateDialog;
    /**
     * 游戏背景音乐开关
     */
    private boolean gameBgMusicSwitch;
    /**
     * 游戏暂停符
     */
    private boolean isPause;
    /**
     * 游戏重新排列组合次数
     */
    private int shuffleNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // 初始化界面
        init();
    }

    /**
     * 初始化游戏的方法
     */
    private void init() {
        time = getIntent().getIntExtra("time", GameConf.DEFAULT_TIME);
        GameConf.ANIMAL_WIDTH = ScreenUtils.getScreenWidth() / 7;
        GameConf.ANIMAL_HEIGHT = GameConf.ANIMAL_WIDTH;
        config = new GameConf(this, 7, 10, 0, 0, time);
        // 得到游戏区域对象
        gameView = (GameView) findViewById(R.id.play_game_gv);
        // 获取显示剩余时间的进度条
        timeProgessBar = (ProgressBar) findViewById(R.id.leave_time_pb);
        // 获取开始按钮
        startButton = (ImageView) this.findViewById(R.id.start_game_btn);
        // 获取振动器
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        music_bg = soundPool.load(this, R.raw.music_bg, 1);
        clean = soundPool.load(this, R.raw.sound_clean, 2);
        read_go = soundPool.load(this, R.raw.sound_readygo, 3);
        win = soundPool.load(this, R.raw.sound_win, 3);
        lose = soundPool.load(this, R.raw.sound_lose, 3);
        soundPool.setOnLoadCompleteListener(this);
        // 初始化游戏业务逻辑接口
        gameControl = new GameControl(this.config);
        // 设置游戏逻辑的实现类
        gameView.setGameControl(gameControl);
        // 为开始按钮的单击事件绑定事件监听器
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                isPause = true;
                operateDialog.show();
                gameView.setVisibility(View.GONE);
            }
        });
        // 为游戏区域的触碰事件绑定监听器
        this.gameView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    gameViewTouchDown(e);
                }
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    gameViewTouchUp(e);
                }
                return true;
            }
        });
        // 初始化游戏失败的对话框
        lostDialog = createDialog("游戏提示", "游戏失败！")
                .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (0 != lose_stream) soundPool.pause(lose_stream);
                        startGamePre();
                    }
                });
        // 初始化游戏胜利的对话框
        successDialog = createDialog("游戏提示", "恭喜过关！")
                .setPositiveButton("下一关",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (0 != win_stream) soundPool.pause(win_stream);
                                if (GameConf.DEFAULT_TIME == time) {
                                    time = GameConf.GENERAL_TIME;
                                    startGamePre();
                                } else if (GameConf.GENERAL_TIME == time) {
                                    time = GameConf.DIFFICULTY_TIME;
                                    startGamePre();
                                }
                            }
                        });

        operateDialog = new OperateDialog(this).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.game_continue_iv:
                        isPause = false;
                        break;
                    case R.id.game_home_iv:
                        finish();
                        break;
                    case R.id.game_help_iv:
                        isPause = false;
                        if (0 == shuffleNumber) return;
                        shuffleNumber -= 1;
                        gameControl.shuffle();
                        if (gameTime - 10 < 0)
                            gameTime = 0;
                        else
                            gameTime -= 10;
                        gameView.postInvalidate();
                        break;
                    case R.id.game_reset_iv:
                        startGamePre();
                        break;
                    case R.id.game_music_rl:
                        boolean bflag = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_BG_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
                        LivingstonApplication.spUtils.put(Constant.Key.GAME_BG_MUSIC_SWITCH, !bflag);
                        if (gameBgMusicSwitch) {
                            if (bflag) {
                                soundPool.pause(music_bg_stream);
                            } else {
                                soundPool.resume(music_bg_stream);
                            }
                        } else {
                            music_bg_stream = soundPool.play(music_bg, 1, 1, 0, -1, 1);
                            gameBgMusicSwitch = true;
                        }
                        operateDialog.setMusicPauseToggle(bflag);
                        break;
                    case R.id.game_clean_music_rl:
                        boolean cflag = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
                        LivingstonApplication.spUtils.put(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, !cflag);
                        operateDialog.setCleanMusicPauseToggle(cflag);
                        break;
                }
            }
        });
        operateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                gameView.setVisibility(View.VISIBLE);
            }
        });
        operateDialog.setMusicPauseToggle(!LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_BG_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN));
        operateDialog.setCleanMusicPauseToggle(!LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPlaying) {
            isPause = true;
            operateDialog.show();
            gameView.setVisibility(View.GONE);
            return;
        }
        soundPool.autoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundPool.autoResume();
    }

    /**
     * 触碰游戏区域的处理方法
     *
     * @param event
     */
    private void gameViewTouchDown(MotionEvent event) {
        // 获取GameControl中的Animal[][]数组
        Animal[][] animals = gameControl.getAnimals();
        if (null == animals) return;
        // 获取用户点击的x座标
        float touchX = event.getX();
        // 获取用户点击的y座标
        float touchY = event.getY();
        // 根据用户触碰的座标得到对应的Animal对象
        Animal currentAnimal = gameControl.findAnimal(touchX, touchY);
        // 如果没有选中任何Animal对象(即鼠标点击的地方没有图片), 不再往下执行
        if (currentAnimal == null)
            return;
        // 将gameView中的选中方块设为当前方块
        this.gameView.setSelectedAnimal(currentAnimal);
        // 表示之前没有选中任何一个Animal
        if (this.selectedAnimal == null) {
            // 将当前方块设为已选中的方块, 重新将GamePanel绘制, 并不再往下执行
            this.selectedAnimal = currentAnimal;
            this.gameView.postInvalidate();
            return;
        }
        // 表示之前已经选择了一个
        if (this.gameView != null) {
            // 在这里就要对currentAnimal和preAnimal进行判断并进行连接
            LinkInfo linkInfo = this.gameControl.link(this.selectedAnimal,
                    currentAnimal);
            // 两个Piece不可连, linkInfo为null
            if (linkInfo == null) {
                // 如果连接不成功, 将当前方块设为选中方块
                this.selectedAnimal = currentAnimal;
                this.gameView.postInvalidate();
            } else {
                // 处理成功连接
                handleSuccessLink(linkInfo, this.selectedAnimal, currentAnimal,
                        animals);
            }
        }
    }

    /**
     * 触碰游戏区域的处理方法
     *
     * @param e
     */
    private void gameViewTouchUp(MotionEvent e) {
        this.gameView.postInvalidate();
    }

    /**
     * 以gameTime作为剩余时间开始或恢复游戏
     *
     * @param gameTime 剩余时间
     */
    private void startGame(int gameTime) {
        // 如果之前的timer还未取消，取消timer
        if (this.timer != null) {
            stopTimer();
        }
        // 重新设置游戏时间
        this.gameTime = gameTime;
        // 如果游戏剩余时间与总游戏时间相等，即为重新开始新游戏
        if (gameTime == time) {
            // 开始新的游戏游戏
            gameView.startGame();
            gameView.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 500);
        }
        isPlaying = true;
        this.timer = new Timer();
        // 启动计时器 ， 每隔1秒发送一次消息
        this.timer.schedule(new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 1000);
        // 将选中方块设为null。
        this.selectedAnimal = null;
    }

    /**
     * 成功连接后处理
     *
     * @param linkInfo      连接信息
     * @param preAnimal     前一个选中方块
     * @param currentAnimal 当前选择方块
     * @param animals       系统中还剩的全部方块
     */
    private void handleSuccessLink(LinkInfo linkInfo, Animal preAnimal,
                                   Animal currentAnimal, Animal[][] animals) {
        // 它们可以相连, 让GamePanel处理LinkInfo
        this.gameView.setLinkInfo(linkInfo);
        // 将gameView中的选中方块设为null
        this.gameView.setSelectedAnimal(null);
        this.gameView.postInvalidate();
        // 将两个Animal对象从数组中删除
        animals[preAnimal.getIndexX()][preAnimal.getIndexY()] = null;
        animals[currentAnimal.getIndexX()][currentAnimal.getIndexY()] = null;
        // 将选中的方块设置null。
        this.selectedAnimal = null;
        // 手机振动(100毫秒)
        this.vibrator.vibrate(100);
        boolean flag = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_CLEAN_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
        if (flag) {
            soundPool.play(clean, 1, 1, 1, 0, 1);
        }
        // 判断是否还有剩下的方块, 如果没有, 游戏胜利
        if (!this.gameControl.hasAnimals()) {
            if (GameConf.DIFFICULTY_TIME == time) {
                this.successDialog
                        .setMessage("亲，你已通关，请返回！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (0 != win_stream) soundPool.pause(win_stream);
                                finish();
                            }
                        });
            }
            if (flag) {
                win_stream = soundPool.play(win, 1, 1, 1, 0, 1);
            }
            // 游戏胜利
            this.successDialog.show();
            // 停止定时器
            stopTimer();
            // 更改游戏状态
            isPlaying = false;
        }
    }

    /**
     * 创建对话框的工具方法
     *
     * @param title   标题
     * @param message 内容
     * @return
     */
    private AlertDialog.Builder createDialog(String title, String message) {
        return new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message);
    }

    /**
     * 停止计时
     */
    private void stopTimer() {
        if (null == this.timer) return;
        // 停止定时器
        this.timer.cancel();
        this.timer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        stopTimer();
        if (null != operateDialog) {
            operateDialog.dismiss();
            operateDialog = null;
        }
        if (null != successDialog) {
            successDialog = null;
        }
        if (null != lostDialog) {
            lostDialog = null;
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (read_go == sampleId) {
            startGamePre();
        }
    }

    private void startGamePre() {
        boolean flag = LivingstonApplication.spUtils.getBoolean(Constant.Key.GAME_BG_MUSIC_SWITCH, Constant.Value.DEFALUT_BOOLEAN);
        gameBgMusicSwitch = flag;
        isPause = false;
        gameControl.clearAnimals();
        gameView.postInvalidate();
        if (flag) {
            music_bg_stream = soundPool.play(music_bg, 1, 1, 0, -1, 1);
            soundPool.play(read_go, 1, 1, 1, 0, 1);
            gameView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGame(time);
                }
            }, 2000);
        } else {
            startGame(time);
        }
    }

    @Override
    public void onBackPressed() {
        if (isPlaying) {
            isPause = true;
            operateDialog.show();
            gameView.setVisibility(View.GONE);
            return;
        }
        finish();
    }
}
