
package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;

import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/13
 * Time: 下午7:31
 */
public interface ProductDeclareDao extends BaseDao<ProductDeclareDO> {

    /**
     * 根据SKU ID获取其税号,税率,计量单位等信息
     * <p/>
     * 注:由于商品在各口岸备案的税率是一样的,所以只取一条记录
     *
     * @param skuId
     * @return
     */
    ProductDeclareDO getTaxAndUnitInfoBySkuId(Long skuId);

    /**
     * 获取待备案状态的SKU ID列表
     *
     * @param condition 查询条件
     * @return
     */
    List<ProductDeclareDO> getWaitDeclareSkuInfo(Map<String, Object> condition);

    List<ProductDeclareDO> getDOByDeclareNameLike(Map<String, Object> condition);

    /**
     * 根据SKU查询消费税、增值税存在的记录
     *
     * @return
     */
    ProductDeclareDO queryDeclareSkuTaxRateExist(Long skuId);


    /**
     * 根据SKU和线路信息查询消费税、增值税存在的记录
     *
     * @return
     */
    List<ProductDeclareDO> queryDeclareSkuTaxRateBySkuIdAndLineId(Map<String, Object> condition);
/**
* 海尚汇插入时用来判断相同的skuId是否之前已经插入过
* */
    List<Long> getProductDeclareBySkuId(Long skuId);

}
