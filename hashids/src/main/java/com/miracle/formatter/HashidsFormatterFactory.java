package com.miracle.formatter;

import com.miracle.annotation.Hashids;
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rick on 2019/3/12.
 */
public class HashidsFormatterFactory extends EmbeddedValueResolutionSupport implements AnnotationFormatterFactory<Hashids> {

    private static final Set<Class<?>> FIELD_TYPES = new HashSet(2){{
        add(Integer.class);
        add(int.class);
    }};

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<?> getPrinter(Hashids hashids, Class<?> aClass) {
        HashidsFormatter formatter = new HashidsFormatter();
        return formatter;
    }

    @Override
    public Parser<?> getParser(Hashids hashids, Class<?> aClass) {
        HashidsFormatter formatter = new HashidsFormatter();
        return formatter;
    }
}
