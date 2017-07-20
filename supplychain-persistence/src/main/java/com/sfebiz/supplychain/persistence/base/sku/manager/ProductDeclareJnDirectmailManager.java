package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareJnDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareJnDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author matt
 * @version $Id: ProductDeclareJnDirectmailManager.java, v 0.1 2016年6月2日 下午2:52:54 matt Exp $
 */
@Component("productDeclareJnDirectmailManager")
public class ProductDeclareJnDirectmailManager extends BaseManager<ProductDeclareJnDirectmailDO> {

    @Resource
    private ProductDeclareJnDirectmailDao productDeclareJnDirectmailDao;

    @Override
    public BaseDao<ProductDeclareJnDirectmailDO> getDao() {
        return productDeclareJnDirectmailDao;
    }

    public ProductDeclareJnDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareJnDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "C:/Users/timmaorz/Documents/SFhaitao_project/code/gitlab-codes/logistics/logistics-persistence/src/main/resources/sqlmap/product-declare-jn-directmail-sqlmap.xml",
                ProductDeclareJnDirectmailDao.class, ProductDeclareJnDirectmailDO.class,
                "sc_product_declare_jn_directmail");
    }

}
