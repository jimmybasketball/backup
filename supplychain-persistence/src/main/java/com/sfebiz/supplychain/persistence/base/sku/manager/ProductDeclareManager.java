
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.sku.enums.SkuDeclareStateType;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/13
 * Time: 下午7:33
 */
@Component("productDeclareManager")
public class ProductDeclareManager extends BaseManager<ProductDeclareDO> {

    @Resource
    private ProductDeclareDao productDeclareDao;

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/leno/Documents/work_at_alibaba/sf/haitao-b2c-supplychain/logistics-persistence/src/main/resources/sqlmap/product-declare-sqlmap.xml",
                ProductDeclareDao.class, ProductDeclareDO.class, "sc_product_declare", true);
    }

    @Override
    public BaseDao<ProductDeclareDO> getDao() {
        return productDeclareDao;
    }

    /**
     * 查询SKUID在所有口岸下的商品备案信息
     *
     * @param skuId
     * @return
     */
    public List<ProductDeclareDO> queryDeclaredSku(long skuId) {
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setSkuId(skuId);
        productDeclareDO.setState(SkuDeclareStateType.DECLARE_PASS.getValue());
        List<ProductDeclareDO> productDeclareDOs = productDeclareDao.query(BaseQuery.getInstance(productDeclareDO));
        if (productDeclareDOs != null && productDeclareDOs.size() > 0) {
            return productDeclareDOs;
        } else {
            return null;
        }
    }

    /**
     * 查询SKUID在指定口岸下的商品备案信息
     *
     * @param skuId
     * @param portId
     * @return
     */
    public ProductDeclareDO queryDeclaredSku(long skuId, long portId) {
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setSkuId(skuId);
        productDeclareDO.setPortId(portId);
        List<ProductDeclareDO> productDeclareDOs = productDeclareDao.query(BaseQuery.getInstance(productDeclareDO));
        if (productDeclareDOs != null && productDeclareDOs.size() > 0) {
            return productDeclareDOs.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询指定SKU下增值税、消费税存在的备案记录
     * @param skuId
     * @return
     */
    public ProductDeclareDO queryDeclareSkuTaxRateExist(long skuId) {
        ProductDeclareDO productDeclareDO = productDeclareDao.queryDeclareSkuTaxRateExist(skuId);
        return productDeclareDO;
    }

    /**
     * 查询指定SKU下增值税、消费税存在的备案记录
     * @param skuId
     * @return
     */
    public ProductDeclareDO queryDeclareSkuTaxRateBySkuIdAndLineId(long skuId,long lineId) {
        Map<String,Object> map = new HashedMap();
        map.put("skuId",skuId);
        map.put("lineId",lineId);
        List<ProductDeclareDO> list = productDeclareDao.queryDeclareSkuTaxRateBySkuIdAndLineId(map);
        if (list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询SKUID在指定口岸下指定备案模式的商品备案信息
     *
     * @param skuId
     * @param portId
     * @param portId
     * @return
     */
    public ProductDeclareDO queryDeclaredSku(Long skuId, Long portId, String declareMode) {
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setSkuId(skuId);
        productDeclareDO.setPortId(portId);
        productDeclareDO.setDeclareMode(declareMode);
        List<ProductDeclareDO> declareList = productDeclareDao.query(BaseQuery.getInstance(productDeclareDO));
        return CollectionUtils.isEmpty(declareList) ? null : declareList.get(0);
    }

    public ProductDeclareDO getTaxAndUnitInfoBySkuId(Long skuId) {
        return productDeclareDao.getTaxAndUnitInfoBySkuId(skuId);
    }

    public List<ProductDeclareDO> getWaitDeclareSkuInfo(List<Long> skuIdList, Long portId, String declareMode) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("skuIdList", skuIdList);
        condition.put("portId", portId);
        condition.put("declareMode", declareMode);
        condition.put("state", SkuDeclareStateType.DECLARE_WAIT.getValue());
        return productDeclareDao.getWaitDeclareSkuInfo(condition);
    }

    public List<ProductDeclareDO> getDOByDeclareNameLike(String declareName, Long portId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("declareName", declareName);
        condition.put("portId", portId);
        return productDeclareDao.getDOByDeclareNameLike(condition);
    }

    /**
     * 删除SKUID在所有口岸下的商品备案信息
     *
     * @param skuId
     * @return
     */
    public void deleteBySkuId(long skuId) {
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setSkuId(skuId);
        productDeclareDO.setState(SkuDeclareStateType.DECLARE_PASS.getValue());
        productDeclareDao.delete(BaseQuery.getInstance(productDeclareDO));
    }

    public List<Long> getProductDeclareBySkuId(Long skuId) {
        return productDeclareDao.getProductDeclareBySkuId(skuId);
    }
}
