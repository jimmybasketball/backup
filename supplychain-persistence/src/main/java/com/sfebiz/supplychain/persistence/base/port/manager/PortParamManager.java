package com.sfebiz.supplychain.persistence.base.port.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.port.dao.PortParamDao;
import com.sfebiz.supplychain.persistence.base.port.domain.PortParamDO;
import org.slf4j.LoggerFactory;
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
@Component("portParamManager")
public class PortParamManager extends BaseManager<PortParamDO> {

    private Map<String, Map<String, PortParamDO>> portParams = new LinkedHashMap<String, Map<String, PortParamDO>>();

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PortParamManager.class);

    @Resource
    private PortParamDao portParamDao;

    @Override
    public BaseDao<PortParamDO> getDao() {
        return portParamDao;
    }

    public List<PortParamDO> getPortParams(long portId,int type){
        PortParamDO d = new PortParamDO();
        d.setPortId(portId);
        d.setType(type);
        BaseQuery<PortParamDO> q = BaseQuery.getInstance(d);
        return this.query(q);
    }

    public String getPortParamCode(Long portId, int type, String value,
                                   boolean usingDefault) {
        String key = portId + "_" + type;
        if (portParams.containsKey(key)) {
        } else {
            List<PortParamDO> list = getPortParams(portId, type);
            Map<String, PortParamDO> map = new LinkedHashMap<String, PortParamDO>();
            for (PortParamDO dto : list) {
                map.put(dto.getValue(), dto);
            }
            portParams.put(key, map);
        }
        if (portParams.containsKey(key)
                && portParams.get(key).containsKey(value)) {
            return portParams.get(key).get(value).getCode();
        }
        if (usingDefault) {
            return getDefaultPortParam(portId, type, value);
        }
        return null;
    }

    public String getDefaultPortParam(Long portId, int type, String value) {
        if (portId == 1 && type == 0) {
            return "011";
        } else if (portId == 1 && type == 1) {
            return "701";
        }
        return value;
    }

    public boolean clearPortParams(){
        portParams.clear();
        return true;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/port/sc_port_param_sqlmap.xml",
                PortParamDao.class,
                PortParamDO.class, "sc_port_param");
    }
}
