package org.sang.service;

import org.sang.bean.Department;
import org.sang.common.annotation.QCache;
import org.sang.config.RedisUtil;
import org.sang.mapper.DepartmentMapper;
import org.sang.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * Created by sang on 2018/1/7.
 */
@Service
@Transactional
public class DepartmentService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    DepartmentMapper departmentMapper;
    public int addDep(Department department) {
        department.setEnabled(true);
        departmentMapper.addDep(department);
        return department.getResult();
    }

    public int deleteDep(Long did) {
        Department department = new Department();
        department.setId(did);
        departmentMapper.deleteDep(department);
        return department.getResult();
    }


    @Cacheable(value = "department", key = "#pid", sync = true)
    public List<Department> getDepByPid(Long pid) {
        List<Department> departments = departmentMapper.getDepByPid(pid);
        System.out.println("数据" + pid + "已缓存");
        System.out.println("数据:" + departments);
        return departments;
    }

    public List<Department> getAllDeps() {
        return departmentMapper.getAllDeps();
    }

    /**
     * value：缓存key的前缀。
     * key：缓存key的后缀。
     * sync：同步，设置如果缓存过期是不是只放一个请求去请求数据库，其他请求阻塞，默认是false。
     */
    //@Cacheable(value = "department", key = "#id", sync = true)
    @QCache(hash = "department",expression="#id")
    public Department getDepById(Long id) {
        Department department = departmentMapper.getDepById(id);
        System.out.println("数据" + id + "已缓存get");
        System.out.println("数据:" + department);
        return department;
    }

    @CachePut(value = "department")
    public Department update (Long id) {
        Department department = departmentMapper.getDepById(id);
        department.setName("测试部门" + new Random().nextInt(10));
        System.out.println("数据" + id + "已缓存update");
        departmentMapper.updateDep(department);
        return department;
    }


    @Autowired
    private RedisTemplate redisTemplate;

    //@CacheEvict(value = "department")
    public void remove(Long id) {
        System.out.println("删除了id为" + id + "key为department的数据缓存");
        //用于手动触发删除缓存
        String name = StringUtils.null2EmptyWithTrimNew(redisTemplate.opsForValue().get("name"));
        System.out.println("name:" + name);

        redisTemplate.opsForValue().set("name","zcl123");

        /*
        redisUtil.set("name","rick123");

        String name2 = StringUtils.null2EmptyWithTrimNew(redisUtil.get("name"));
        System.out.println("name2:" + name2);
        */

        String name3 = StringUtils.null2EmptyWithTrimNew(redisTemplate.opsForValue().get("name"));

        System.out.println("name3:" + name3);
    }



}
