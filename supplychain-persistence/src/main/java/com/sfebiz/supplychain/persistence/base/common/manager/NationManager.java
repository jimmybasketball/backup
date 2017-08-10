package com.sfebiz.supplychain.persistence.base.common.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.common.dao.NationDao;
import com.sfebiz.supplychain.persistence.base.common.domain.NationDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * User: 心远
 * Date: 14/12/15
 * Time: 下午4:41
 */
@Component("nationManager")
public class NationManager extends BaseManager<NationDO> {

    @Resource
    private NationDao nationDao;

    private Map<String, String> nationMapping = new LinkedHashMap<String, String>();
	
    @Override
    public BaseDao<NationDO> getDao() {
        return nationDao;
    }

    String getNationCode(String nation){
    	NationDO d = new NationDO();
    	d.setNation(nation);
    	BaseQuery<NationDO> q = BaseQuery.getInstance(d);
    	List<NationDO> list = this.query(q);
    	if (list != null && list.size() > 0){
    		return list.get(0).getCode();
    	}
    	return null;
    }

	public String getNationCaCode(String nation){
		NationDO d = new NationDO();
		d.setNation(nation);
		BaseQuery<NationDO> q = BaseQuery.getInstance(d);
		List<NationDO> list = this.query(q);
		if (list != null && list.size() > 0){
			return list.get(0).getCaCode();
		}
		return null;
	}
    
    public String getNationInterdna(String nation) {
		if (nationMapping.containsKey(nation)) {
			return nationMapping.get(nation);
		}
		String code = getNationCode(nation);
		if (code != null) {
			nationMapping.put(nation, code);
		}
		return code;
	}

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("D:\\work\\develop\\logistics\\logistics-persistence\\src\\main\\resources\\sqlmap/nation-sqlmap.xml", NationDao.class, NationDO.class, "sc_nation");
    }
}
