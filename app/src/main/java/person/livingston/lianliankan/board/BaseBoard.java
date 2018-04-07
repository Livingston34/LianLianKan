package person.livingston.lianliankan.board;

import android.util.Log;

import java.util.List;

import person.livingston.lianliankan.bean.Animal;
import person.livingston.lianliankan.bean.AnimalImage;
import person.livingston.lianliankan.bean.GameConf;
import person.livingston.lianliankan.utils.ImageUtils;

/**
 * Created by Livingston on 2018/03/31.
 */

public abstract class BaseBoard {

    // 定义一个抽象方法, 让子类去实现

    /**
     * 创建一个List集合，该集合里面存放初始化游戏时所需的Animal对象
     */
    protected abstract List<Animal> createAnimals(GameConf config,
                                                 Animal[][] animals);

    /**
     * 返回Animal数组
     *
     * @param config
     * @return
     */
    public Animal[][] create(GameConf config) {
        // 创建Animal[][]数组
        Animal[][] animals = new Animal[config.getXSize()][config.getYSize()];
        // 返回非空的Animal集合, 该集合由子类去创建
        List<Animal> animalList = createAnimals(config, animals);
        // 根据非空Animal对象的集合的大小来取图片
        List<AnimalImage> playImages = ImageUtils.getPlayImages(
                config.getContext(), animalList.size());
        // 所有图片的宽、高都是相同的
        int imageWidth = GameConf.ANIMAL_WIDTH;
        int imageHeight = GameConf.ANIMAL_HEIGHT;
        // 遍历非空的Animal集合
        for (int i = 0; i < animalList.size(); i++) {
            // 依次获取每个Piece对象
            Animal animal = animalList.get(i);
            animal.setImage(playImages.get(i));
            // 计算每个方块左上角的X、Y座标
            animal.setBeginX(animal.getIndexX() * imageWidth
                    + config.getBeginImageX());
            animal.setBeginY(animal.getIndexY() * imageHeight
                    + config.getBeginImageY());
            // 将该方块对象放入方块数组的相应位置处
            animals[animal.getIndexX()][animal.getIndexY()] = animal;
        }
        return animals;
    }
}
