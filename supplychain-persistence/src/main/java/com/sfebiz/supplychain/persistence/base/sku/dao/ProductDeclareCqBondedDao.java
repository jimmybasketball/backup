package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareCqBondedDO;

/**
 * 
 * 
 * @author matt
 * @version $Id: ProductDeclareJnDirectmailDao.java, v 0.1 2016年6月2日 下午2:58:06 matt Exp $
 */
public interface ProductDeclareCqBondedDao extends BaseDao<ProductDeclareCqBondedDO> {

	ProductDeclareCqBondedDO getByProductDeclareId(Long productDeclareId);
}
