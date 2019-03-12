package com.miracle.formatter;

import org.hashids.Hashids;

/**
 * hashids编码和解码
 *
 */
public class HashidsUtils {

    public static final HashidsUtils instance = new HashidsUtils();

    public String salt;

    /**
     * 编码
     *
     * @param id
     * @return
     */
    public String encode(int id) {
        Hashids hashids = new Hashids(salt, 8);
        return hashids.encode(id);
    }

    /**
     * 解码
     *
     * @param hasdids
     * @return
     */
    public int decode(String hasdids) {
       try {
           Hashids hashids = new Hashids(salt, 8);
           return (int) hashids.decode(hasdids)[0];
       } catch (Exception ex) {
           System.out.println("invalid id is " + hasdids);
           throw new IllegalArgumentException("invalid id is " + hasdids);
       }
    }

}
