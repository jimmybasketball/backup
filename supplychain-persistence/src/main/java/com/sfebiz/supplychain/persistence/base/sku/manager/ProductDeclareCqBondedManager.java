package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.ProductDeclareCqBondedDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareCqBondedDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author zyj
 * @version $Id: ProductDeclareCqBondedManager.java, v 0.1 2016年6月2日 下午2:52:54 zyj Exp $
 */
@Component("productDeclareCqBondedManager")
public class ProductDeclareCqBondedManager extends BaseManager<ProductDeclareCqBondedDO> {

    @Resource
    private ProductDeclareCqBondedDao productDeclareCqBondedDao;

    @Override
    public BaseDao<ProductDeclareCqBondedDO> getDao() {
        return productDeclareCqBondedDao;
    }

    public ProductDeclareCqBondedDO getByProductDeclareId(Long productDeclareId) {
        return productDeclareCqBondedDao.getByProductDeclareId(productDeclareId);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
                "D:\\work\\develop\\logistics/logistics-persistence/src/main/resources/sqlmap/product-declare-cq-bonded-sqlmap.xml",
                ProductDeclareCqBondedDao.class, ProductDeclareCqBondedDO.class,
                "sc_product_declare_cq_bonded");
    }

}
