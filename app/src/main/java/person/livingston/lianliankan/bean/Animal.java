package person.livingston.lianliankan.bean;

import android.graphics.Point;

/**
 * Created by Livingston on 2018/03/31.
 */

public class Animal {

    // 保存动物对象的所对应的图片
    private AnimalImage image;
    // 该方块的左上角的x坐标
    private int beginX;
    // 该方块的左上角的y座标
    private int beginY;
    // 该对象在Animal[][]数组中第一维的索引值
    private int indexX;
    // 该对象在Animal[][]数组中第二维的索引值
    private int indexY;

    // 只设置该Animal对象在数组中的索引值
    public Animal(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public AnimalImage getImage() {
        return image;
    }

    public void setImage(AnimalImage image) {
        this.image = image;
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    // 获取该Animal的中心
    public Point getCenter() {
        return new Point(getImage().getImage().getWidth() / 2
                + getBeginX(), getBeginY()
                + getImage().getImage().getHeight() / 2);
    }

    // 判断两个Animal上的图片是否相同
    public boolean isSameImage(Animal other) {
        if (null == image && null != other.image) {
                return false;
        }
        // 只要Animal封装图片ID相同，即可认为两个Animal相等。
        return image.getImageId() == other.image.getImageId();
    }
}
