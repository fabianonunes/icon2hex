package tc.fab.icon2hex;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.apache.commons.codec.binary.Hex;

public class Icon2Hex {

	public static void main(String[] args) throws IOException {

		OptionSet options = parseArgs(args);
		
		File input = (File) options.valueOf("i");

		BufferedImage image = ImageIO.read(input);

		BufferedImage bi = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		ColorConvertOp xformOp = new ColorConvertOp(null);
		xformOp.filter(image, bi);

		WritableRaster raster = bi.getRaster();
		DataBufferInt data = (DataBufferInt) raster.getDataBuffer();

		int[] intData = data.getData();

		ByteBuffer byteBuffer = ByteBuffer.allocate(intData.length * 4);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(intData);

		byte[] array = byteBuffer.array();

		System.out.println(Hex.encodeHex(array));

	}

	private static OptionSet parseArgs(String... args) throws IOException {

		OptionParser parser = new OptionParser() {
			{

				accepts("i", "input image file").withRequiredArg()
						.ofType(File.class).describedAs("input").required();

				accepts("help").forHelp();

			}
		};

		try {
			return parser.parse(args);
		} catch (OptionException e) {
			parser.printHelpOn(System.out);
			System.exit(1);
		}

		return null;

	}

}
