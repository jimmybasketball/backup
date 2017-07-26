package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareXmDirectmailDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareXmDirectmailDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author matt
 * @version $Id: ProductDeclareXmDirectmailManager.java, v 0.1 2016年6月2日 下午2:52:54 matt Exp $
 */
@Component("productDeclareXmDirectmailManager")
public class ProductDeclareXmDirectmailManager extends BaseManager<ProductDeclareXmDirectmailDO> {

    @Resource
    private ProductDeclareXmDirectmailDao productDeclareXmDirectmailDao;

    @Override
    public BaseDao<ProductDeclareXmDirectmailDO> getDao() {
        return productDeclareXmDirectmailDao;
    }

    public ProductDeclareXmDirectmailDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareXmDirectmailDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "C:/Users/timmaorz/Documents/SFhaitao_project/code/gitlab-codes/logistics/logistics-persistence/src/main/resources/sqlmap/product-declare-xm-directmail-sqlmap.xml",
                ProductDeclareXmDirectmailDao.class, ProductDeclareXmDirectmailDO.class,
                "sc_product_declare_xm_directmail");
    }

}
