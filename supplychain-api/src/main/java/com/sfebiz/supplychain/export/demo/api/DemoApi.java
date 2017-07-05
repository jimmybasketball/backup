package com.sfebiz.supplychain.export.demo.api;

import com.sfebiz.supplychain.export.common.entity.CommonRet;
import com.sfebiz.supplychain.export.demo.entity.DemoEntity;
import com.sfebiz.supplychain.export.common.entity.Void;

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
