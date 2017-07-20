/*
 * @(#) com.sfebiz.logistics.api/ProductDeclareService.java
 * 
 */
package com.sfebiz.supplychain.exposed.sku.api;

import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
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
    Long createDeclareSku(Long skuId, Long portId, String declareMode, Long userId, String operator)
            throws ServiceException;

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
    Long createDeclareSku(Long purchaseOrderId, Long skuId, Long portId, String declareMode, Long userId, String operator)
            throws ServiceException;

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
    void createBatchDeclareSku(List<Long> skuIds, Long portId, String declareMode,
                               Long userId, String operator) throws ServiceException;

    /**
     * 开始收集备案商品信息
     *
     * @param productDeclareId 备案主表ID
     * @param userId           用户ID
     * @param operator         操作人
     * @throws ServiceException
     */
    void startCollect(Long productDeclareId, Long userId, String operator) throws ServiceException;

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
     * 商品备案完成导入
     *
     * @param skuDeclareEntity 商品备案信息
     * @param userId           用户ID
     * @param operator         操作人
     * @return
     * @throws ServiceException
     */
    boolean declarePass(SkuDeclareEntity skuDeclareEntity, Long userId, String operator)
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
     * 备案商品
     *
     * @param skuId  商品ID
     * @param portId 口岸ID
     * @return
     * @throws ServiceException
     */
    boolean declareSku(Long skuId, Long portId) throws ServiceException;

    /**
     * 通过导入，创建备案商品SKU
     *
     * @param skuDeclareEntity
     * @return
     * @throws ServiceException
     */
    boolean createDeclaredSkuByImport(SkuDeclareEntity skuDeclareEntity) throws ServiceException;

    /**
     * 删除备案商品
     *
     * @param skuId  商品ID
     * @param portId 口岸ID
     * @return
     * @throws ServiceException
     */
    boolean removeDeclareSku(Long skuId, Long portId) throws ServiceException;

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
    boolean removeBatchDeclareSku(String productDeclareIds, Long portId, String declareMode,
                                  Long userId, String operator) throws ServiceException;

    /**
     * 更新商品备案价格
     *
     * @param productDeclareId
     * @param price            备案人民币价格 单位:分
     * @throws ServiceException
     */
    void updateProductDeclarePrice(Long productDeclareId, Long price) throws ServiceException;

    /**
     * 更新备案信息
     *
     * @param skuDeclareEntity
     * @throws ServiceException
     */
    void updateProductDeclare(SkuDeclareEntity skuDeclareEntity) throws ServiceException;

    /**
     * 更新商品备案税率
     *
     * @param productDeclareId 备案表ID
     * @param taxRate          最新税率
     * @throws ServiceException
     */
    void updateProductDeclareTaxRate(Long productDeclareId, String taxRate) throws ServiceException;


    /**
     * 更新商品备案税率(税改后)
     *
     * @param productDeclareId 备案表ID
     * @param consumptionDutyRate       最新消费税率
     * @param addedValueTaxRate         最新增值税率
     * @param tariffRate                最新关税
     * @throws ServiceException
     */
    void updateProductDeclareRate(Long productDeclareId, String consumptionDutyRate, String addedValueTaxRate, String tariffRate) throws ServiceException;

    /**
     * 获取商品备案信息
     *
     * @param skuId 商品ID
     * @return
     * @throws ServiceException
     */
    List<SkuDeclareEntity> getSkuDeclareInfo(Long skuId) throws ServiceException;

    /**
     * 创建空口岸的备案记录
     *
     * @param skuId
     * @param taxRate
     * @return
     */
    BaseResult createDeclarePassSkuOfEmptyPort(Long skuId, String taxRate) throws ServiceException;

    /**
     * 创建一条备案通过的记录
     *
     * @param skuId
     * @param portId
     * @param declareMode
     * @param operator
     * @param taxRate
     * @return
     */
    BaseResult createDeclarePassSku(Long skuId, Long portId, String declareMode, String taxRate, String operator);

    /**
     * 批量更新备案状态为备案中
     *
     * @param skuIds      资料收集完毕状态的商品
     * @param portId      口岸ID
     * @param declareMode 备案模式
     * @param userId      用户ID
     * @param operator    操作人
     * @throws ServiceException
     */
    void declaring(List<Long> skuIds, Long portId, String declareMode,
                   Long userId, String operator) throws ServiceException;


    /**
     * 刷新商品毛重
     *
     * @param skuId
     * @param grossWeitht
     * @param userName
     * @throws ServiceException
     */

    BaseResult updateSkuGrossWeight(Long skuId, int grossWeitht, String userName) throws ServiceException;
    /**
     * 重新计算单个sku税费
     * @param skuId
     * @param lineId
     * @param supplyCost
     * @throws ServiceException
     */
    BaseResult recalculateSkuTax(Long skuId, Long lineId, Integer supplyCost) throws ServiceException;

}
