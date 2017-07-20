/*
 * @(#) com.sfebiz.supplychain.persistence.base.sku.manager/ProductDeclareNbDirectmailManager.java
 * 
 */
package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareHsCodeDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareHsCodeDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 创建日期: 2015-07-07
 *
 * @author jackiehff
 */
@Component("productDeclareHsCodeManager")
public class ProductDeclareHsCodeManager extends BaseManager<ProductDeclareHsCodeDO> {

    @Resource
    private ProductDeclareHsCodeDao productDeclareHsCodeDao;

    @Override
    public BaseDao<ProductDeclareHsCodeDO> getDao() {
        return productDeclareHsCodeDao;
    }

    public ProductDeclareHsCodeDO getByHsCode(String hsCode) {
        return productDeclareHsCodeDao.getByHsCode(hsCode);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/D:/house/logistics/logistics-persistence/src/main/resources/sqlmap/product-declare-hs-code-sqlmap.xml",
                ProductDeclareHsCodeDao.class, ProductDeclareHsCodeDO.class,
                "sc_product_declare_hs_code");
    }

}
