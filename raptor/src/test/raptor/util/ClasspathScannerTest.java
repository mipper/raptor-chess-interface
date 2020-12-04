package raptor.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class ClasspathScannerTest {

    private static final Logger _logger = LoggerFactory.getLogger(ClasspathScannerTest.class);

    @Test
    public void scan()
            throws IOException {
        final List<URL> urlList = new ClasspathScanner(getClass().getClassLoader()).scan("resources/themes/",
                                                                                         ".*\\.properties");
        _logger.debug("Themes: " + urlList);
        assertEquals(9, urlList.size());
    }

}