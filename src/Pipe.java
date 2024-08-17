import java.awt.*;

public class Pipe {
    int x;
    int y;
    int height;
    int width;
    Image img;
    boolean passed = false;

    Pipe(Image img, int pipeX, int pipeY, int pipeWidth, int pipeHeight) {
        this.img = img;
        this.x = pipeX;
        this.y = pipeY;
        this.width = pipeWidth;
        this.height = pipeHeight;
    }
}
