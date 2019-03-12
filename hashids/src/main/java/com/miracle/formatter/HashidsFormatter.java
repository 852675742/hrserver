package com.miracle.formatter;

import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Rick on 2019/3/12.
 *
 */
@Slf4j
public class HashidsFormatter implements Formatter<Integer>{

    public String salt;

    @Override
    public Integer parse(String s, Locale locale) throws ParseException {
        try {
            Hashids hashids = new Hashids(salt, 8);
            return (int) hashids.decode(s)[0];
        } catch (Exception ex) {
            log.error("invalid id is " + s);
            throw new IllegalArgumentException("invalid id is " + s);
        }
    }

    @Override
    public String print(Integer id, Locale locale) {
        Hashids hashids = new Hashids(salt, 8);
        return hashids.encode(id);
    }
}
