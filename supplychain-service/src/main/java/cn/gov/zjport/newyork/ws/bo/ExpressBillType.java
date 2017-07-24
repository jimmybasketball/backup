package cn.gov.zjport.newyork.ws.bo;

/**
 * 面单类型
 * @author Administrator
 *
 */
public enum ExpressBillType {
	
	FQHT("FQHT", "丰趣海淘面单"),
	NEUTER("NEUTER", "中性面单");

    private String type;

    private String description;

    ExpressBillType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return type;
    }
}
