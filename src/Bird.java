import java.awt.*;

public class Bird {
    int x;
    int y;
    int width;
    int height;
    Image img;

    Bird(Image img, int birdX, int birdY, int birdWidth, int birdHeight) {
        this.img = img;
        this.x = birdX;
        this.y = birdY;
        this.width = birdWidth;
        this.height = birdHeight;
    }
}
