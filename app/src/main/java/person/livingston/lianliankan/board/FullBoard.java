package person.livingston.lianliankan.board;

import java.util.ArrayList;
import java.util.List;

import person.livingston.lianliankan.bean.Animal;
import person.livingston.lianliankan.bean.GameConf;

/**
 * Created by Livingston on 2018/03/31.
 */

public class FullBoard extends BaseBoard {

    @Override
    protected List<Animal> createAnimals(GameConf config, Animal[][] animals) {
        // 创建一个Animal集合, 该集合里面存放初始化游戏时所需的Animal对象
        List<Animal> animalList = new ArrayList<>();
        for (int i = 1; i < animals.length - 1; i++) {
            for (int j = 1; j < animals[i].length - 1; j++) {
                // 先构造一个Animal对象, 只设置它在Animal[][]数组中的索引值，
                // 所需要的Image由其父类负责设置。
                Animal animal = new Animal(i, j);
                // 添加到Animal集合中
                animalList.add(animal);
            }
        }
        return animalList;
    }
}
