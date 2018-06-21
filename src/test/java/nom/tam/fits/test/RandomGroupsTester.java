package nom.tam.fits.test;

import nom.tam.fits.BasicHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsUtil;
import nom.tam.fits.Header;
import nom.tam.util.ArrayFuncs;
import nom.tam.util.BufferedFile;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/** Test random groups formats in FITS data.
 *    Write and read random groups data
 */
public class RandomGroupsTester {

  @Test
  public void test() throws Exception {

    float[][] fa = new float[20][20];
    float[] pa = new float[3];

    BufferedFile bf = new BufferedFile("rg1.fits", "rw");

    Object[][] data = new Object[1][2];
    data[0][0] = pa;
    data[0][1] = fa;

    // First lets write out the file painfully group by group.
    BasicHDU hdu = Fits.makeHDU(data);
    Header hdr = hdu.getHeader();
    // Change the number of groups
    hdr.addValue("GCOUNT", 20, "Number of groups");
    hdr.write(bf);

    for (int i = 0; i < 20; i += 1) {

      for (int j = 0; j < pa.length; j += 1) {
        pa[j] = i + j;
      }
      for (int j = 0; j < fa.length; j += 1) {
        fa[j][j] = i * j;
      }
      // Write a group
      bf.writeArray(data);
    }

    byte[] padding = new byte[FitsUtil.padding(20 * ArrayFuncs.computeLSize(data))];
    bf.write(padding);

    bf.flush();
    bf.close();

    // Read back the data.
    Fits f = new Fits("rg1.fits");
    BasicHDU[] hdus = f.read();

    data = (Object[][]) hdus[0].getKernel();

    for (int i = 0; i < data.length; i += 1) {

      pa = (float[]) data[i][0];
      fa = (float[][]) data[i][1];
      for (int j = 0; j < pa.length; j += 1) {
        assertEquals("paramTest:" + i + " " + j, (float) (i + j), pa[j]);
      }
      for (int j = 0; j < fa.length; j += 1) {
        assertEquals("dataTest:" + i + " " + j, (float) (i * j), fa[j][j]);
      }
    }

    // Now do it in one fell swoop -- but we have to have
    // all the data in place first.
    f = new Fits();

    // Generate a FITS HDU from the kernel.
    f.addHDU(Fits.makeHDU(data));
    bf = new BufferedFile("rg2.fits", "rw");
    f.write(bf);

    bf.flush();
    bf.close();

    f = new Fits("rg2.fits");
    data = (Object[][]) f.read()[0].getKernel();
    for (int i = 0; i < data.length; i += 1) {

      pa = (float[]) data[i][0];
      fa = (float[][]) data[i][1];
      for (int j = 0; j < pa.length; j += 1) {
        assertEquals("paramTest:" + i + " " + j, (float) (i + j), pa[j]);
      }
      for (int j = 0; j < fa.length; j += 1) {
        assertEquals("dataTest:" + i + " " + j, (float) (i * j), fa[j][j]);
      }
    }
  }
}
