package person.livingston.lianliankan;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import person.livingston.lianliankan.bean.Animal;
import person.livingston.lianliankan.bean.GameConf;
import person.livingston.lianliankan.bean.LinkInfo;
import person.livingston.lianliankan.control.GameControl;
import person.livingston.lianliankan.widget.GameView;

public class MainActivity extends AppCompatActivity {

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
    private Button startButton;
    /**
     * 记录剩余时间的TextView
     */
    private TextView timeTextView;
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
     * 振动处理类
     */
    private Vibrator vibrator;
    int dis;
    SoundPool soundPool = new SoundPool(2
            , AudioManager.STREAM_SYSTEM , 8);
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
                    timeTextView.setText("剩余时间： " + gameTime);
                    gameTime--; // 游戏剩余时间减少
                    // 时间小于0, 游戏失败
                    if (gameTime < 0) {
                        // 停止计时
                        stopTimer();
                        // 更改游戏的状态
                        isPlaying = false;
                        // 失败后弹出对话框
                        lostDialog.show();
                        return;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化界面
        init();
    }

    /**
     * 初始化游戏的方法
     */
    private void init() {
        config = new GameConf(this, 7, 10, 0, 0, GameConf.DEFAULT_TIME);
        // 得到游戏区域对象
        gameView = (GameView) findViewById(R.id.play_game_gv);
        // 获取显示剩余时间的文本框
        timeTextView = (TextView) findViewById(R.id.leave_time_tv);
        // 获取开始按钮
        startButton = (Button) this.findViewById(R.id.start_game_btn);
        // 获取振动器
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        dis = soundPool.load(this , R.raw.dis , 1);
        // 初始化游戏业务逻辑接口
        gameControl = new GameControl(this.config);
        // 设置游戏逻辑的实现类
        gameView.setGameControl(gameControl);
        // 为开始按钮的单击事件绑定事件监听器
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                startGame(GameConf.DEFAULT_TIME);
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
        lostDialog = createDialog("Lost", "游戏失败！ 重新开始", R.mipmap.ic_launcher)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startGame(GameConf.DEFAULT_TIME);
                    }
                });
        // 初始化游戏胜利的对话框
        successDialog = createDialog("Success", "游戏胜利！ 重新开始",
                R.mipmap.ic_launcher).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startGame(GameConf.DEFAULT_TIME);
                    }
                });
    }

    @Override
    protected void onPause() {
        // 暂停游戏
        stopTimer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // 如果处于游戏状态中
        if (isPlaying) {
            // 以剩余时间重新开始游戏
            startGame(gameTime);
        }
        super.onResume();
    }

    /**
     * 触碰游戏区域的处理方法
     *
     * @param event
     */
    private void gameViewTouchDown(MotionEvent event) {
        // 获取GameControl中的Animal[][]数组
        Animal[][] animals = gameControl.getAnimals();
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
        // 表示之前没有选中任何一个Piece
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
        if (gameTime == GameConf.DEFAULT_TIME) {
            // 开始新的游戏游戏
            gameView.startGame();
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
     * @param linkInfo     连接信息
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
        soundPool.play(dis, 1, 1, 0, 0, 1);
        // 判断是否还有剩下的方块, 如果没有, 游戏胜利
        if (!this.gameControl.hasAnimals()) {
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
     * @param title         标题
     * @param message       内容
     * @param imageResource 图片
     * @return
     */
    private AlertDialog.Builder createDialog(String title, String message,
                                             int imageResource) {
        return new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message).setIcon(imageResource);
    }

    /**
     * 停止计时
     */
    private void stopTimer() {
        // 停止定时器
        this.timer.cancel();
        this.timer = null;
    }
}
