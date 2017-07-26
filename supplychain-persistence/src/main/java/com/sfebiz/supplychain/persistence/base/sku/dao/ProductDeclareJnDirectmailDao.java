package com.sfebiz.supplychain.persistence.base.sku.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareJnDirectmailDO;

/**
 * 
 * 
 * @author matt
 * @version $Id: ProductDeclareJnDirectmailDao.java, v 0.1 2016年6月2日 下午2:58:06 matt Exp $
 */
public interface ProductDeclareJnDirectmailDao extends BaseDao<ProductDeclareJnDirectmailDO> {

	ProductDeclareJnDirectmailDO getByProductDeclareId(Long productDeclareId);
}
