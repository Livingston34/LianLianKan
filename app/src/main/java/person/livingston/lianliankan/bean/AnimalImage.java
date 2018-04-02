package person.livingston.lianliankan.bean;

import android.graphics.Bitmap;

/**
 * Created by Livingston on 2018/03/31.
 */

public class AnimalImage {

    private Bitmap image;
    private int imageId;

    public AnimalImage(Bitmap image, int imageId) {
        this.image = image;
        this.imageId = imageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
