package person.livingston.lianliankan.bean;

import android.content.Context;

/**
 * Created by Livingston on 2018/03/31.
 */

public class GameConf {

    /**
     * 应用上下文
     */
    private Context context;
    /**
     * 连连看的每个方块的图片的宽
     */
    public static final int ANIMAL_WIDTH = 160;
    /**
     * 连连看的每个方块的图片的高
     */
    public static final int ANIMAL_HEIGHT = 160;
    /**
     * 记录游戏的总事件（100秒）.
     */
    public static int DEFAULT_TIME = 100;
    /**
     * Animal[][]数组第一维的长度
     */
    private int xSize;
    /**
     * Animal[][]数组第二维的长度
     */
    private int ySize;
    /**
     * Board中第一张图片出现的x座标
     */
    private int beginImageX;
    /**
     * Board中第一张图片出现的y座标
     */
    private int beginImageY;
    /**
     * 记录游戏的总时间, 单位是秒
     */
    private long gameTime;

    /**
     * 提供一个参数构造器
     *
     * @param context     应用上下文
     * @param xSize       Piece[][]数组第一维长度
     * @param ySize       Piece[][]数组第二维长度
     * @param beginImageX Board中第一张图片出现的x座标
     * @param beginImageY Board中第一张图片出现的y座标
     * @param gameTime    设置每局的时间, 单位是豪秒
     */
    public GameConf(Context context, int xSize, int ySize, int beginImageX, int beginImageY,
                    long gameTime) {
        this.context = context;
        this.xSize = xSize;
        this.ySize = ySize;
        this.beginImageX = beginImageX;
        this.beginImageY = beginImageY;
        this.gameTime = gameTime;
    }

    /**
     * @return 应用上下文
     */
    public Context getContext() {
        return context;
    }

    /**
     * @return 游戏的总时间
     */
    public long getGameTime() {
        return gameTime;
    }

    /**
     * @return Piece[][]数组第一维的长度
     */
    public int getXSize() {
        return xSize;
    }

    /**
     * @return Piece[][]数组第二维的长度
     */
    public int getYSize() {
        return ySize;
    }

    /**
     * @return Board中第一张图片出现的x座标
     */
    public int getBeginImageX() {
        return beginImageX;
    }

    /**
     * @return Board中第一张图片出现的y座标
     */
    public int getBeginImageY() {
        return beginImageY;
    }
}
