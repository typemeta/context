package org.typemeta.context.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.joining;

abstract class SqlUtils {

    private static final String SQL_DIR = "/sql/";
    private static final String SQL_SEP_TOKEN = ";;";

    static String loadSingleResource(String path) {
        return Optional.ofNullable(DatabaseExtractorTest.class.getResourceAsStream(SQL_DIR + path))
                .map(InputStreamReader::new)
                .map(is -> {
                    try (BufferedReader br = new BufferedReader(is)) {
                        return br.lines().collect(joining("\n"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }).orElseThrow(() -> new RuntimeException("Resource '" + path + "' not found"));
    }

    static Stream<String> loadMultiResource(String path) {
        final String text =
                Optional.ofNullable(DatabaseExtractorTest.class.getResourceAsStream(SQL_DIR + path))
                        .map(InputStreamReader::new)
                        .map(is -> {
                            try (BufferedReader br = new BufferedReader(is)) {
                                return br.lines().collect(joining("\n"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }).orElseThrow(() -> new RuntimeException("Resource '" + path + "' not found"));

        final Iterator<String> iter = new Iterator<String>() {
            int s = 0;
            int e = nextIndex();
            String next = text.substring(0, e);

            private int nextIndex() {
                int e = text.indexOf(SQL_SEP_TOKEN, s);
                return (e == -1) ? text.length() : e;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public String next() {
                final String result = next;
                s = e + SQL_SEP_TOKEN.length();
                if (s > text.length()) {
                    next = null;
                } else {
                    e = nextIndex();
                    next = (e == -1 ? text.substring(s) : text.substring(s, e)).trim();
                    if (next.isEmpty()) {
                        next = null;
                    }
                }
                return result;
            }
        };

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iter,
                        Spliterator.ORDERED | Spliterator.NONNULL
                ), false
        );
    }
}
