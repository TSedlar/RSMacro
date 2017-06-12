import rs.macro.api.util.fx.CannyEdgeModel;
import rs.macro.api.util.fx.PolyTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

/**
 * @author Tyler Sedlar
 * @since 6/11/17
 */
public class Test {

    public static void main(String[] args) throws Exception {


        File inputA = new File("./test/models/air-rune.emf");
        File inputB = new File("./test/models/air-rune.emf");

        Optional<Polygon> polyOptA = CannyEdgeModel.loadPolyEMF(inputA);
        Optional<Polygon> polyOptB = CannyEdgeModel.loadPolyEMF(inputB);

        if (!polyOptA.isPresent()) {
            throw new IllegalStateException(inputA.getName() + " is corrupt");
        }

        if (!polyOptB.isPresent()) {
            throw new IllegalStateException(inputB.getName() + " is corrupt");
        }

        Polygon polyA = polyOptA.get();
        Polygon polyB = polyOptB.get();

        long start = System.nanoTime();
        double similarity = PolyTool.similarity(polyA, polyB);
        long end = System.nanoTime();
        System.out.printf("found poly similarity to be %s in %.02f seconds\n", similarity, (end - start) / 1e9);


        BufferedImage imgA = PolyTool.toImage(polyA);
        BufferedImage imgB = PolyTool.toImage(polyB);

        ImageIO.write(imgA, "jpg", new File(
                inputA.getAbsolutePath().replace("models", "images").replace(".emf", ".jpg"))
        );
        ImageIO.write(imgB, "jpg", new File(
                inputB.getAbsolutePath().replace("models", "images").replace(".emf", ".jpg"))
        );
    }
}
