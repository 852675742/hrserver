package org.sang.controller;

import com.miracle.annotation.Hashids;
import org.sang.bean.MemberVo;
import org.sang.bean.RespBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    private List<MemberVo> memberVos;

    @PostConstruct
    public void init() {
        memberVos = new ArrayList<>();
        MemberVo zhangsan = new MemberVo();
        zhangsan.setMemberId(1);
        zhangsan.setLoginName("zhangsan");
        zhangsan.setNickName("张三");
        zhangsan.setPassword("123456");
        memberVos.add(zhangsan);

        MemberVo lisi = new MemberVo();
        lisi.setMemberId(2);
        lisi.setLoginName("lisi");
        lisi.setNickName("李四");
        lisi.setPassword("123456");
        memberVos.add(lisi);
    }

    @GetMapping
        public RespBean findMemberAll() {
        logger.info("查询会员列表");
        List<MemberVo> memberVos = this.memberVos;
        return new RespBean("success", memberVos);
    }

    @PostMapping
    public RespBean save(@RequestBody MemberVo memberVo) {
        logger.info("添加会员 memberId=" + memberVo.getMemberId());
        print(memberVo);
        return new RespBean("success", "success");
    }

    @GetMapping("/query")
    public RespBean query(MemberVo memberVo) {
        logger.info("查询会员, memberId=" + memberVo.getMemberId());
        for (MemberVo item: memberVos) {
            if (item.getMemberId() == memberVo.getMemberId()) {
                return new RespBean("success", item);
            }
        }
        return new RespBean("success", null);
    }

    @GetMapping("/{memberId}")
    public RespBean getByMemberId(@Hashids @PathVariable Integer memberId) {
        logger.info("查询会员 by getByMemberId, memberId=" + memberId);
        for (MemberVo item: memberVos) {
            if (item.getMemberId() == memberId) {
                return new RespBean("success", item);
            }
        }
        return new RespBean("success", null);
    }

    @GetMapping("/get")
    public RespBean get(@Hashids @RequestParam Integer memberId) {
        logger.info("查询会员 by get, memberId=" + memberId);
        for (MemberVo item: memberVos) {
            if (item.getMemberId() == memberId) {
                return new RespBean("success", item);
            }
        }
        return new RespBean("success", null);
    }

    private void print(MemberVo memberVo) {
        logger.info("member info\n" + memberVo.toString());
    }
}