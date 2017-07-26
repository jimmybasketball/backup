/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareHzBondedManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareHzBondedDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareHzBondedDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareHzBondedManager")
public class ProductDeclareHzBondedManager extends BaseManager<ProductDeclareHzBondedDO> {

    @Resource
    private ProductDeclareHzBondedDao productDeclareHzBondedDao;

    @Override public BaseDao<ProductDeclareHzBondedDO> getDao() {
        return productDeclareHzBondedDao;
    }

    public ProductDeclareHzBondedDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareHzBondedDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/leno/Documents/work_at_alibaba/sf/haitao-b2c-supplychain/logistics-persistence/src/main/resources/sqlmap/product-declare-hz-bonded-sqlmap.xml",
                ProductDeclareHzBondedDao.class, ProductDeclareHzBondedDO.class,
                "sc_product_declare_hz_bonded");
    }

}
