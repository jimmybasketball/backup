package com.sfebiz.supplychain.service.warehouse.model;

/**
 * 
 * <p>仓库联系人信息业务实体</p>
 * @author matt
 * @Date 2017年7月24日 下午5:39:03
 */
public class WarehouseContactBO {

    /** 仓库联系人姓名 */
    private String contactName;

    /** 仓库联系人邮箱 */
    private String contactEmail;

    /** 仓库联系人手机 */
    private String contactCellphone;

    /** 仓库联系人电话 */
    private String contactTelephone;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactCellphone() {
        return contactCellphone;
    }

    public void setContactCellphone(String contactCellphone) {
        this.contactCellphone = contactCellphone;
    }

    public String getContactTelephone() {
        return contactTelephone;
    }

    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

}
