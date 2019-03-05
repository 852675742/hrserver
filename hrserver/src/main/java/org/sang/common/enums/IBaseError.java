package org.sang.common.enums;

import org.sang.bean.HrvError;

/**
 * Created by Rick on 2019/2/26.
 *
 * @ Description：错误接口
 */
public interface IBaseError {

    HrvError getError();

    default String getCode() {
        return getError().getCode();
    }

    default String getMsg() {
        return getError().getMsg();
    }

}
