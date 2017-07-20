/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareNbDirectmailManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareNbDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareNbDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareNbDirectmailManager")
public class ProductDeclareNbDirectmailManager extends BaseManager<ProductDeclareNbDirectmailDO> {

    @Resource
    private ProductDeclareNbDirectmailDao productDeclareNbDirectmailDao;

    @Override
    public BaseDao<ProductDeclareNbDirectmailDO> getDao() {
        return productDeclareNbDirectmailDao;
    }

    public ProductDeclareNbDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareNbDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/Users/jackiehff/sfhaitao/codeproject/logistics/logistics-persistence/src/main/resources/sqlmap/product-declare-nb-directmail-sqlmap.xml",
                ProductDeclareNbDirectmailDao.class, ProductDeclareNbDirectmailDO.class,
                "sc_product_declare_nb_directmail");
    }

}
