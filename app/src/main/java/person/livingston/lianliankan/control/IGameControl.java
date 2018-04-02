package person.livingston.lianliankan.control;

import person.livingston.lianliankan.bean.Animal;
import person.livingston.lianliankan.bean.LinkInfo;

/**
 * Created by Livingston on 2018/03/31.
 */

public interface IGameControl {

    /**
     * 控制游戏开始的方法
     */
    void start();

    /**
     * 定义一个接口方法, 用于返回一个二维数组
     *
     * @return 存放方块对象的二维数组
     */
    Animal[][] getAnimals();

    /**
     * 判断参数Animal[][]数组中是否还存在非空的Animal对象
     *
     * @return 如果还剩Animal对象返回true, 没有返回false
     */
    boolean hasAnimals();

    /**
     * 根据鼠标的x座标和y座标, 查找出一个Animal对象
     *
     * @param touchX
     *            鼠标点击的x座标
     * @param touchY
     *            鼠标点击的y座标
     * @return 返回对应的Piece对象, 没有返回null
     */
    Animal findAnimal(float touchX, float touchY);

    /**
     * 判断两个Animal是否可以相连, 可以连接, 返回LinkInfo对象
     *
     * @param animal1
     *            第一个Animal对象
     * @param animal2
     *            第二个Animal对象
     * @return 如果可以相连，返回LinkInfo对象, 如果两个Animal不可以连接, 返回null
     */
    LinkInfo link(Animal animal1, Animal animal2);
}
