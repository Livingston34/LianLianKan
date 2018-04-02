package person.livingston.lianliankan.board;

import java.util.ArrayList;
import java.util.List;

import person.livingston.lianliankan.bean.Animal;
import person.livingston.lianliankan.bean.GameConf;

/**
 * Created by Livingston on 2018/03/31.
 */

public class HorizontalBoard extends BaseBoard {
    @Override
    protected List<Animal> createAnimals(GameConf config, Animal[][] animals) {
        // 创建一个Animal集合, 该集合里面存放初始化游戏时所需的Animal对象
        List<Animal> animalList = new ArrayList<>();
        for (int i = 0; i < animals.length; i++) {
            for (int j = 0; j < animals[i].length; j++) {
                // 加入判断, 符合一定条件才去构造Animal对象, 并加到集合中
                if (j % 2 == 0) {
                    // 如果x能被2整除, 即单数行不会创建方块
                    // 先构造一个Animal对象, 只设置它在Animal[][]数组中的索引值，
                    // 所需要的AnimalImage由其父类负责设置。
                    Animal Animal = new Animal(i, j);
                    // 添加到Animal集合中
                    animalList.add(Animal);
                }
            }
        }
        return animalList;
    }
}
