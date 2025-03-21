package org.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;
import java.io.File;

/** an implementation of {@link org.reflections.vfs.Vfs.Dir} for directory {@link java.io.File} */
public class SystemDir implements Vfs.Dir {
    private final File file;

    public SystemDir(File file) {
        if (file == null || !file.exists() || !file.isDirectory() || !file.canRead()) {
            throw new RuntimeException("cannot use dir " + file);
        }

        this.file = file;
    }

    public String getPath() {
        return file.getPath().replace("\\", "/");
    }

    public Iterable<Vfs.File> getFiles() {
        return new Iterable<Vfs.File>() {
            public Iterator<Vfs.File> iterator() {
                return new AbstractIterator<Vfs.File>() {
                    final Stack<File> stack = new Stack<File>();
                    {stack.addAll(listFiles(file));}

                    protected Vfs.File computeNext() {
                        while (!stack.isEmpty()) {
                            final File file = stack.pop();
                            if (file.isDirectory()) {
                                stack.addAll(listFiles(file));
                            } else {
                                return new SystemFile(SystemDir.this, file);
                            }
                        }

                        return endOfData();
                    }
                };
            }
        };
    }

    private static List<File> listFiles(final File file) {
        File[] files = file.listFiles();

        if (files != null)
            return Lists.newArrayList(files);
        else
            return Lists.newArrayList();
    }

    public void close() {
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
