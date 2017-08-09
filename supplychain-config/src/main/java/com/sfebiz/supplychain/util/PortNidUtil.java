package com.sfebiz.supplychain.util;

/**
 * Created by TT on 2016/9/6.
 */
public class PortNidUtil {

    public static String getPortNid(Integer portId) {
        String portNid = "";
         switch (portId){
             case 1:
                 portNid="HZPORT";
             break;

             case 2:
                 portNid="GZPORT";
                 break;
             case 3:
                 portNid="NBPORT";
                 break;
             case 5:
                 portNid="JNPORT";
                 break;
             case 6:
                 portNid="XMPORT";
                 break;
             case 7:
                 portNid="PTPORT";
                 break;
             case 9:
                 portNid="CQPORT";
                 break;
             case 10:
                 portNid="QDPORT";
                 break;
             case 0:
                 portNid="";
                 break;
         }

         return portNid;
    }
}
