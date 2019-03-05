package org.sang.common.enums;

import org.sang.bean.HrvError;

/**
 * Created by Rick on 2019/2/26.
 *
 * @ Description：业务错误信息
 */
public enum BusinessErrorEnum implements IBaseError{
    DATA_VALID(new HrvError("B01","数据非法")),
    SIGN_VALID(new HrvError("B02","签名非法")),
    ;

    private HrvError hrvError;

    BusinessErrorEnum(HrvError hrvError) {
        this.hrvError = hrvError;
    }

    @Override
    public HrvError getError() {
        return hrvError;
    }
}
