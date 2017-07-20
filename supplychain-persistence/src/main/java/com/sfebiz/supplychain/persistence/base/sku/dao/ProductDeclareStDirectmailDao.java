package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareStDirectmailDO;

/**
 * 
 * 
 * @author matt
 * @version $Id: ProductDeclarePtDirectmailDao.java, v 0.1 2016年6月2日 下午2:58:06 matt Exp $
 */
public interface ProductDeclareStDirectmailDao extends BaseDao<ProductDeclareStDirectmailDO> {

	ProductDeclareStDirectmailDO getByProductDeclareId(Long productDeclareId);
}
