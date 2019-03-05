package org.sang;

import org.sang.bean.HrvError;
import org.sang.common.enums.BusinessErrorEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Rick on 2019/2/26.
 *
 * @ Description：${description}
 */
public class EnumTest {

    public static void main(String...args) {
        List<HrvError> list = Stream.of(BusinessErrorEnum.values()).map(BusinessErrorEnum::getError).collect(Collectors.toList());
        System.out.println(list);
        String msg = Arrays.stream(BusinessErrorEnum.values()).filter(p -> p.getCode().equals("B02")).findFirst().map(BusinessErrorEnum::getMsg).orElse(null);
        System.out.println(msg);
    }

}
