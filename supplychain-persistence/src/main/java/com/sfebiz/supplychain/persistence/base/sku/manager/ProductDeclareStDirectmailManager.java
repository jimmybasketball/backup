package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareStDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareStDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author matt
 * @version $Id: ProductDeclarePtDirectmailManager.java, v 0.1 2016年6月2日 下午2:52:54 matt Exp $
 */
@Component("productDeclareStDirectmailManager")
public class ProductDeclareStDirectmailManager extends BaseManager<ProductDeclareStDirectmailDO> {

    @Resource
    private ProductDeclareStDirectmailDao productDeclareStDirectmailDao;

    @Override
    public BaseDao<ProductDeclareStDirectmailDO> getDao() {
        return productDeclareStDirectmailDao;
    }

    public ProductDeclareStDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareStDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/D:/house/logistics/logistics-persistence/src/main/resources/sqlmap/product-declare-st-directmail-sqlmap.xml",
                ProductDeclareStDirectmailDao.class, ProductDeclareStDirectmailDO.class,
                "sc_product_declare_st_directmail");
    }

}
