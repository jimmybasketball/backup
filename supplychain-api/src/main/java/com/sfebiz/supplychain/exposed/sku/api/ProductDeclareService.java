/*
 * @(#) com.sfebiz.logistics.api/ProductDeclareService.java
 * 
 */
package com.sfebiz.supplychain.exposed.sku.api;

import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.sku.entity.SkuDeclareEntity;
import net.pocrd.entity.ServiceException;

import java.util.List;

/**
 * 创建日期: 2015-07-10
 *
 * @author jackiehff
 */
public interface ProductDeclareService {

    /**
     * 创建备案商品
     *
     * @param skuId       商品ID
     * @param portId      口岸ID
     * @param declareMode 备案模式
     * @param userId      用户ID
     * @param operator    操作人
     * @return
     * @throws ServiceException
     */
    CommonRet<Long> createDeclareSku(Long skuId, Long portId, String declareMode, Long userId, String operator);

    /**
     * 批量创建待备案商品
     *
     * @param skuIds      待备案商品
     * @param portId      口岸ID
     * @param declareMode 备案模式
     * @param userId      用户ID
     * @param operator    操作人
     * @throws ServiceException
     */
    CommonRet<Void> createBatchDeclareSku(List<Long> skuIds, Long portId, String declareMode, Long userId, String operator);

    /**
     * 创建备案商品
     *
     * @param purchaseOrderId 采购单ID
     * @param skuId           商品ID
     * @param portId          口岸ID
     * @param declareMode     备案模式
     * @param userId          用户ID
     * @param operator        操作人
     * @return
     * @throws ServiceException
     */
    CommonRet<Long> createDeclareSku(Long purchaseOrderId, Long skuId, Long portId, String declareMode, Long userId, String operator);

    /**
     * 商品备案信息收集完毕导入
     *
     * @param skuDeclareEntity 商品备案信息
     * @param userId           用户ID
     * @param operator         操作人
     * @return
     * @throws ServiceException
     */
    boolean finishCollect(SkuDeclareEntity skuDeclareEntity, Long userId, String operator)
            throws ServiceException;

    /**
     * 商品备案不通过
     *
     * @param productDeclareId 商品备案主表ID
     * @param reason           备案不通过原因
     * @param userId           用户ID
     * @param operator         操作人
     * @return
     * @throws ServiceException
     */
    boolean declareNotPass(Long productDeclareId, String reason, Long userId, String operator)
            throws ServiceException;

    /**
     * 批量删除备案商品(删除待备案或是资料收集中的商品，不删除商品备案数据，只删除广州、杭州、宁波备案数据)
     *
     * @param productDeclareIds 商品备案表IDs
     * @param portId            口岸ID
     * @param declareMode       备案模式
     * @param userId            用户ID
     * @param operator          用户名
     * @return
     * @throws ServiceException
     */
    boolean removeBatchDeclareSku(String productDeclareIds, Long portId, String declareMode, Long userId, String operator) throws ServiceException;

    /**
     * 创建空口岸的备案记录
     *
     * @param skuId
     * @param taxRate
     * @return
     */
    BaseResult createDeclarePassSkuOfEmptyPort(Long skuId, String taxRate) throws ServiceException;

    /**
     * 更新备案信息
     *
     * @param skuDeclareEntity
     * @throws ServiceException
     */
    void updateProductDeclare(SkuDeclareEntity skuDeclareEntity) throws ServiceException;
}
