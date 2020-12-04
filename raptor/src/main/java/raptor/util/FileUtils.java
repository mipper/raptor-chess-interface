/**
 * New BSD License
 * http://www.opensource.org/licenses/bsd-license.php
 * Copyright 2009-2011 RaptorProject (http://code.google.com/p/raptor-chess-interface/)
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the RaptorProject nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package raptor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raptor.Raptor;

public class FileUtils {

    private static final Logger _logger = LoggerFactory.getLogger(FileUtils.class);
    /**
     * Everything below this directory in the root dir of the Raptor.jar will be
     * recursively extracted during program startup (i.e. the "install" phase)
     */
    private final static String SRC_RESOURCE_DIR = "resources/";

    /**
     * This function will recursively extract files or directories from "/resources" in the application jar to
     * specified destination.
     *
     * @param dest
     *            -- A File object that represents the destination for the copy.
     */
    public static void installFiles(String dest) {
        //say("installFiles() -- dest=\"" + dest + "\"");
        try {
            //
            // First attempt to create destination directory (if not existing yet)
            //
            File df = new File(dest);
            if (!df.exists()) {
                if (!df.mkdirs()) {
                    throw new IOException("installFiles: Could not create directory: " + df.getAbsolutePath() + ".");
                }
                _logger.debug("Created directory {}", df.getAbsolutePath());
            }
            //
            // Get contents of application jar
            //
            ClassLoader cl = FileUtils.class.getClassLoader();
            final List<URL> urlList = new ClasspathScanner(cl).scan(SRC_RESOURCE_DIR);
            for (URL url : urlList) {
                copyFile(url, dest);
            }
        }
        catch (Exception ex) {
            Raptor.getInstance().onError("Error installing system resources", ex);
        }
    }

    private static void copyFile(final URL url, final String dest)
            throws IOException {
        String en = url.getPath().replaceFirst(".*/" + SRC_RESOURCE_DIR, "");
        Path of = Paths.get(dest, en);
        if (!of.getParent().toFile().exists()) {
            //say("installFiles() -- creating \"" + of.getParentFile().getAbsolutePath() + "\" ...");
            Files.createDirectories(of.getParent());
            _logger.debug("Created {}", of.getParent());
        }
        try (final InputStream in = url.openStream()) {
            Files.copy(in, of, StandardCopyOption.REPLACE_EXISTING);
        }
        _logger.debug("Installed {} to {}", en, of);
    }

    public static void makeEmptyFile(String filnam)
            throws IOException {
        File f = new File(filnam);
        String dirnam = f.getParent();
        File df = new File(dirnam);
        if (!df.exists()) {
            if (!df.mkdirs()) {
                throw new IOException("installFiles: Could not create directory: "
                                              + df.getAbsolutePath() + ".");
            }
            _logger.debug("Created new directory {}", df.getAbsolutePath());
        }
        f.createNewFile();
        _logger.debug("Created new empty file {}", f.getAbsolutePath());
    }

    /**
     * Deletes all files and subdirectories under "dir".
     *
     * @param dir
     *            Directory to be deleted
     * @return boolean Returns "true" if all deletions were successful. If a
     *         deletion fails, the method stops attempting to delete and returns
     *         "false".
     */
    public static boolean deleteDir(File dir) {

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    _logger.error("Error trying to delete file: {}", dir.getAbsolutePath());
                }
            }
        }

        // The directory is now empty so now it can be smoked
        return dir.delete();
    }

    /**
     * Returns the contents of the specified file as a string.
     *
     * @param fileName
     *            The fully qualified file name.
     * @return The contents of the file as a string. Returns null if there was
     *         an error reading the file.
     */
    public static String fileAsString(String fileName) {
        File f = null;
        f = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            StringBuilder result = new StringBuilder(10000);
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString();
        }
        catch (IOException e) {
            Raptor.getInstance().onError("Error reading file: " + f.getAbsolutePath(), e);
            return null;
        }
    }

}
