/* 
 * Copyright (C) 2018 CNRS - JMMC project ( http://www.jmmc.fr )
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/** *****************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ***************************************************************************** */
package fr.jmmc.oitools.util.test;

import fr.jmmc.oitools.JUnitBaseTest;
import static fr.jmmc.oitools.JUnitBaseTest.TEST_DIR_OIFITS;
import fr.jmmc.oitools.model.OIFitsLoader;
import fr.jmmc.oitools.OIFitsProcessor;
import fr.nom.tam.fits.FitsException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jammetv
 */
public class TestMergeUtilsFilesKO extends JUnitBaseTest {

    /**
     * Test exception with not enough parameters
     *
     * @throws IOException
     * @throws MalformedURLException
     * @throws FitsException
     */
    @Test
    public void testNoFiles() throws IOException, MalformedURLException, FitsException {

        try {
            OIFitsProcessor.mergeOIFitsFiles();
            Assert.fail("Merge with no parameters should raise an exception");
        } catch (IllegalArgumentException iae) {
            Assert.assertTrue(
                    String.format("Message should contains 'Not enough files as parameters' (is '%s')", iae.getMessage()),
                    iae.getMessage().contains("Not enough files as parameters"));
        }

        try {
            OIFitsProcessor.mergeOIFitsFiles(OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "NGC5128_2005.oifits"));
            Assert.fail("Merge with only one parameters should raise an exception");
        } catch (IllegalArgumentException iae) {
            Assert.assertTrue(
                    String.format("Message should contains 'Not enough files as parameters' (is '%s')", iae.getMessage()),
                    iae.getMessage().contains("Not enough files as parameters"));
        }

    }

    /**
     * Test exception with a bad filename
     *
     * @throws IOException
     * @throws MalformedURLException
     * @throws FitsException
     */
    @Test
    public void testBadFilename() throws IOException, MalformedURLException, FitsException {

        try {
            OIFitsProcessor.mergeOIFitsFile(
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "inexistingFile.oifits"),
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "NGC5128_2005.oifits"));
            Assert.fail("Merge with an inexisting file should raise an exception");
        } catch (IOException ioe) {
            Assert.assertTrue(
                    String.format("Message should contains 'File not found' (is '%s')", ioe.getMessage()),
                    ioe.getMessage().contains("File not found"));
        }

        try {
            OIFitsProcessor.mergeOIFitsFile(
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "NGC5128_2005.oifits"),
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "inexistingFile.oifits"));
            Assert.fail("Merge with an inexisting file should raise an exception");
        } catch (IOException ioe) {
            Assert.assertTrue(
                    String.format("Message should contains 'File not found' (is '%s')", ioe.getMessage()),
                    ioe.getMessage().contains("File not found"));
        }

    }

    /**
     * Test exception with 2 files having different targets
     *
     * @throws IOException
     * @throws MalformedURLException
     * @throws FitsException
     */
    @Test
    public void testDifferentTargets() throws IOException, MalformedURLException, FitsException {

        try {
            OIFitsProcessor.mergeOIFitsFile(
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS
                            + "A-CLUSTER__2T3T__1-PHASEREF__SIMPLE_nsr0.05__20160812_193521_1.image-oi.oifits"),
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "NGC5128_2005.oifits"));
            Assert.fail("Merge of two files with different target should raise an exception");
        } catch (IllegalArgumentException iae) {
            Assert.assertTrue(
                    String.format("Message should contains 'not the same target' (is '%s')", iae.getMessage()),
                    iae.getMessage().contains("not the same target"));
        }

        try {
            OIFitsProcessor.mergeOIFitsFiles(
                    OIFitsLoader.loadOIFits(
                            TEST_DIR_OIFITS + "A-CLUSTER__2T3T__1-PHASEREF__SIMPLE_nsr0.05__20160812_193521_1.image-oi.oifits"),
                    OIFitsLoader.loadOIFits(
                            TEST_DIR_OIFITS + "A-CLUSTER__2T3T__1-PHASEREF__SIMPLE_nsr0.05__20160812_193521_1.oifits"),
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "NGC5128_2005.oifits"));
            Assert.fail("Merge of two files with different target should raise an exception");
        } catch (IllegalArgumentException iae) {
            Assert.assertTrue(
                    String.format("Message should contains 'not the same target' (is '%s')", iae.getMessage()),
                    iae.getMessage().contains("not the same target"));
        }

    }

    /**
     * Test exception with an empty file
     *
     * @throws IOException
     * @throws MalformedURLException
     * @throws FitsException
     */
    @Test
    public void testEmptyFiles() throws IOException, MalformedURLException, FitsException {

        try {
            OIFitsProcessor.mergeOIFitsFile(
                    OIFitsLoader.loadOIFits(TEST_DIR_FITS + "SG_surface2.fits"),
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "NGC5128_2005.oifits"));
            Assert.fail("Merge an empty file should raise an exception");
        } catch (FitsException fe) {
            Assert.assertTrue(
                    String.format("Message should contains 'Invalid OIFits format (no OI table found)' (is '%s')", fe.getMessage()),
                    fe.getMessage().contains("Invalid OIFits format (no OI table found)"));
        }

        try {
            OIFitsProcessor.mergeOIFitsFile(
                    OIFitsLoader.loadOIFits(TEST_DIR_OIFITS + "NGC5128_2005.oifits"),
                    OIFitsLoader.loadOIFits(TEST_DIR_FITS + "SG_surface2.fits"));
            Assert.fail("Merge an empty file should raise an exception");
        } catch (FitsException fe) {
            Assert.assertTrue(
                    String.format("Message should contains 'Invalid OIFits format (no OI table found)' (is '%s')", fe.getMessage()),
                    fe.getMessage().contains("Invalid OIFits format (no OI table found)"));
        }

    }

}
