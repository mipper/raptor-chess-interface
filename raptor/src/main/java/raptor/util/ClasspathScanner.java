package raptor.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;

import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClasspathScanner {

    private final ClassLoader _classLoader;

    public ClasspathScanner(final ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    public List<URL> scan(final String directory) {
        return scan(directory, Pattern.compile(".*"));
    }

    public List<URL> scan(final String directory, final String pattern) {
        return scan(directory, Pattern.compile(pattern));
    }

    private List<URL> scan(final String directory, final Pattern pattern) {
        return new ClassGraph().addClassLoader(_classLoader)
                               .acceptPaths(directory)
                               .scan()
                               .getResourcesMatchingPattern(pattern)
                               .stream()
                               .map(Resource::getURL)
                               .collect(Collectors.toList());
    }

}