/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareGzBondedManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareGzBondedDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareGzBondedDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareGzBondedManager")
public class ProductDeclareGzBondedManager extends BaseManager<ProductDeclareGzBondedDO> {

    @Resource
    private ProductDeclareGzBondedDao productDeclareGzBondedDao;

    @Override
    public BaseDao<ProductDeclareGzBondedDO> getDao() {
        return productDeclareGzBondedDao;
    }

    public ProductDeclareGzBondedDO getByProductDeclareId(Long productDeclareId) {
        List<ProductDeclareGzBondedDO> gzBondedList = productDeclareGzBondedDao.getByProductDeclareId(productDeclareId);
        return CollectionUtils.isEmpty(gzBondedList) ? null : gzBondedList.get(0);

    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/leno/Documents/work_at_alibaba/sf/haitao-b2c-supplychain/logistics-persistence/src/main/resources/sqlmap/product-declare-gz-bonded-sqlmap.xml",
                ProductDeclareGzBondedDao.class, ProductDeclareGzBondedDO.class,
                "sc_product_declare_gz_bonded");
    }
}
