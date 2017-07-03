package com.sfebiz.supplychain.api;

import com.sfebiz.supplychain.entity.CommonRet;
import com.sfebiz.supplychain.entity.DemoEntity;
import com.sfebiz.supplychain.entity.Void;

import java.util.List;

/**
 * 测试用的
 */
public interface DemoApi {

    public CommonRet<Long> addEntity(DemoEntity entity);

    public CommonRet<Void> updateEntity(Long id, DemoEntity entity);

    public CommonRet<Void> deleteEntityById(Long id);

    public CommonRet<DemoEntity> getEntityById(Long id);

    public CommonRet<List<DemoEntity>> getEntityList();

    public CommonRet<Void> validTest(Long id, String name, DemoEntity demoEntity);

}
