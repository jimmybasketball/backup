package com.sfebiz.supplychain.protocol.wms.ptwms;

/**
 * Created by TT on 2016/7/6.
 */
public class FeeDetail {

    /**
     *总费用
     */
    private float totalFee;
    /**
     *运输费
     */
    private float SHIPPING;
    /**
     *操作费用
     */
    private float OPF;
    /**
     *燃油附加费
     */
    private float FSC;
    /**
     *关税
     */
    private float DT;
    /**
     *挂号
     */
    private float RSF;
    /**
     *仓租
     */
    private float WHF;
    /**
     *其它费用
     */
    private float OTF;

    public float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(float totalFee) {
        this.totalFee = totalFee;
    }

    public float getSHIPPING() {
        return SHIPPING;
    }

    public void setSHIPPING(float SHIPPING) {
        this.SHIPPING = SHIPPING;
    }

    public float getOPF() {
        return OPF;
    }

    public void setOPF(float OPF) {
        this.OPF = OPF;
    }

    public float getFSC() {
        return FSC;
    }

    public void setFSC(float FSC) {
        this.FSC = FSC;
    }

    public float getDT() {
        return DT;
    }

    public void setDT(float DT) {
        this.DT = DT;
    }

    public float getRSF() {
        return RSF;
    }

    public void setRSF(float RSF) {
        this.RSF = RSF;
    }

    public float getWHF() {
        return WHF;
    }

    public void setWHF(float WHF) {
        this.WHF = WHF;
    }

    public float getOTF() {
        return OTF;
    }

    public void setOTF(float OTF) {
        this.OTF = OTF;
    }

    @Override
    public String toString() {
        return "FeeDetail{" +
                "totalFee=" + totalFee +
                ", SHIPPING=" + SHIPPING +
                ", OPF=" + OPF +
                ", FSC=" + FSC +
                ", DT=" + DT +
                ", RSF=" + RSF +
                ", WHF=" + WHF +
                ", OTF=" + OTF +
                '}';
    }
}
