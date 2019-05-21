package org.sang.jobHandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

/**
 * Created by user on 2019/5/21.
 */
@Component
@JobHandler(value="testJobHandler")
public class TestJobHandler extends IJobHandler{
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        System.out.println("测试测试测试测试测试测试");
        return null;
    }
}
