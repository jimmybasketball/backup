package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclarePtDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclarePtDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author matt
 * @version $Id: ProductDeclarePtDirectmailManager.java, v 0.1 2016年6月2日 下午2:52:54 matt Exp $
 */
@Component("productDeclarePtDirectmailManager")
public class ProductDeclarePtDirectmailManager extends BaseManager<ProductDeclarePtDirectmailDO> {

    @Resource
    private ProductDeclarePtDirectmailDao productDeclarePtDirectmailDao;

    @Override
    public BaseDao<ProductDeclarePtDirectmailDO> getDao() {
        return productDeclarePtDirectmailDao;
    }

    public ProductDeclarePtDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclarePtDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "/D:/house/logistics/logistics-persistence/src/main/resources/sqlmap/product-declare-pt-directmail-sqlmap.xml",
                ProductDeclarePtDirectmailDao.class, ProductDeclarePtDirectmailDO.class,
                "sc_product_declare_pt_directmail");
    }

}
