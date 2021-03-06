package person.livingston.lianliankan.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

import person.livingston.lianliankan.bean.Animal;
import person.livingston.lianliankan.bean.LinkInfo;
import person.livingston.lianliankan.control.GameControl;
import person.livingston.lianliankan.utils.ImageUtils;

/**
 * Created by Livingston on 2018/03/31.
 */

public class GameView extends View {

    /**
     * 游戏逻辑的实现类
     */
    private GameControl gameControl;
    /**
     * 保存当前已经被选中的方块
     */
    private Animal selectedAnimal;
    /**
     * 连接信息对象
     */
    private LinkInfo linkInfo;
    /**
     * 画笔Paint
     */
    private Paint paint;
    /**
     * 选中标识的图片对象
     */
    private Bitmap selectImage;

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        // 设置连接线的颜色
        this.paint.setColor(Color.RED);
        // 设置连接线的粗细
        this.paint.setStrokeWidth(3);
        // 初始化被选中的图片
        this.selectImage = ImageUtils.getSelectImage(context);
    }

    /**
     * 设置连接信息对象
     *
     * @param linkInfo
     */
    public void setLinkInfo(LinkInfo linkInfo) {
        this.linkInfo = linkInfo;
    }

    /**
     * 设置当前选中方块
     *
     * @param animal
     */
    public void setSelectedAnimal(Animal animal) {
        this.selectedAnimal = animal;
    }

    /**
     * 设置游戏逻辑的实现类
     *
     * @param gameControl
     */
    public void setGameControl(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    /**
     * 绘制图形的方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.gameControl == null)
            return;
        Animal[][] animals = gameControl.getAnimals();
        if (animals != null) {
            // 遍历animals二维数组
            for (int i = 0; i < animals.length; i++) {
                for (int j = 0; j < animals[i].length; j++) {
                    // 如果二维数组中该元素不为空（即有方块），将这个方块的图片画出来
                    if (animals[i][j] != null) {
                        // 得到这个Animal对象
                        Animal animal = animals[i][j];
                        // 根据方块左上角X、Y座标绘制方块
                        canvas.drawBitmap(animal.getImage().getImage(),
                                animal.getBeginX(), animal.getBeginY(), null);
                    }
                }
            }
        }
        // 如果当前对象中有linkInfo对象, 即连接信息
        if (this.linkInfo != null) {
            // 绘制连接线
            drawLine(this.linkInfo, canvas);
            // 处理完后清空linkInfo对象
            this.linkInfo = null;
        }
        // 画选中标识的图片
        if (this.selectedAnimal != null) {
            canvas.drawBitmap(this.selectImage, this.selectedAnimal.getBeginX(),
                    this.selectedAnimal.getBeginY(), null);
        }
    }

    /**
     * 根据LinkInfo绘制连接线的方法。
     *
     * @param linkInfo 连接信息对象
     * @param canvas   画布
     */
    private void drawLine(LinkInfo linkInfo, Canvas canvas) {
        // 获取LinkInfo中封装的所有连接点
        List<Point> points = linkInfo.getLinkPoints();
        // 依次遍历linkInfo中的每个连接点
        for (int i = 0; i < points.size() - 1; i++) {
            // 获取当前连接点与下一个连接点
            Point currentPoint = points.get(i);
            Point nextPoint = points.get(i + 1);
            // 绘制连线
            canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x,
                    nextPoint.y, this.paint);
        }
    }

    // 开始游戏方法
    public void startGame() {
        this.gameControl.start();
        this.postInvalidate();
    }
}
