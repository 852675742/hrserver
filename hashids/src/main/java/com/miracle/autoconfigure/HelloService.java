package com.miracle.autoconfigure;

/**
 * Created by Rick on 2019/3/12.
 *
 */
public class HelloService {

    private HashidsProperties hashidsProperties;


    public HelloService(HashidsProperties hashidsProperties) {
          this.hashidsProperties = hashidsProperties;
    }

    public HashidsProperties getHashidsProperties() {
        return hashidsProperties;
    }

}
