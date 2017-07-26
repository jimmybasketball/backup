package com.sfebiz.supplychain.exposed.sku.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import net.pocrd.entity.ServiceException;

import java.util.List;

/**
 * 基础商品服务
 *
 * @author tanzx [liujunchi@ifunq.com]
 * @date 2017/7/12 10:04
 */
public interface SkuService {

    /**
     * 创建基础商品
     *
     * @param operator       操作人
     * @param skuEntity      基础商品实体
     * @return 基础商品id
     */
    public CommonRet<Long> createSku(String operator, SkuEntity skuEntity);

    /**
     * 修改基础商品
     *
     * @param operator       操作人
     * @param skuEntity      基础商品实体
     * @return
     */
    public CommonRet<Void> updateSku(String operator, SkuEntity skuEntity);

    /**
     * 根据商品条码获得基本商品信息
     *
     * @param barcode       操作人
     * @return 基础商品
     */
    public CommonRet<SkuEntity> selectSkuByBarcode(String barcode);

    /**
     * 根据skuId获得基本商品信息
     *
     * @param skuId         skuId
     * @return 基础商品
     */
    public CommonRet<SkuEntity> selectSkuBySkuId(Long skuId);

    /**
     * 根据商品条码获得skuId
     *
     * @param barcode         商品条码
     * @return 基础商品
     */
    public CommonRet<Long> selectSkuIdByBarcode(String barcode);

    /**
     * 根据skuId获得商品条码
     *
     * @param skuId         skuId
     * @return 基础商品
     */
    public CommonRet<List<SkuEntity>> selectBarcodeBySkuId(Long skuId);

    /**
     * 根据商品ID查找商品信息,只查出sku信息，不查效期方案等
     */
    SkuEntity getSkuOnlySkuInfo(long id) throws ServiceException;

}
