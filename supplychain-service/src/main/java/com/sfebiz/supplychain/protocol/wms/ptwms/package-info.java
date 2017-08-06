/**
 * Created by TT on 2016/11/10.
 */
@XmlSchema(
        xmlns={
                @XmlNs(prefix="SOAP-ENV", namespaceURI="http://schemas.xmlsoap.org/soap/envelope/"),
                @XmlNs(prefix="ns1", namespaceURI= "http://www.example.org/Ec/")
        }
)
package com.sfebiz.supplychain.protocol.wms.ptwms;


import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;