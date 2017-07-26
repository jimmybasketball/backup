/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareNbBondedManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareNbBondedDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareNbBondedDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareNbBondedManager")
public class ProductDeclareNbBondedManager extends BaseManager<ProductDeclareNbBondedDO> {

    @Resource
    private ProductDeclareNbBondedDao productDeclareNbBondedDao;

    @Override
    public BaseDao<ProductDeclareNbBondedDO> getDao() {
        return productDeclareNbBondedDao;
    }

    public ProductDeclareNbBondedDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareNbBondedDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/leno/Documents/work_at_alibaba/sf/haitao-b2c-supplychain/logistics-persistence/src/main/resources/sqlmap/product-declare-nb-bonded-sqlmap.xml",
                ProductDeclareNbBondedDao.class, ProductDeclareNbBondedDO.class,
                "sc_product_declare_nb_bonded");
    }

}
