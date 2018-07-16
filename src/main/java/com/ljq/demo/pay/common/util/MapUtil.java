package com.ljq.demo.pay.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: java bean 与 map 互相转换工具类
 * @Author: junqiang.lu
 * @Date: 2018/5/14
 */
public final class MapUtil {

    private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);

    private MapUtil(){}

    /**
     * 将 xml 转换为 map
     *
     * @param xmlStr xml 格式字符串
     * @return map 对象
     * @throws DocumentException
     */
    public static Map<String, String> xml2Map(String xmlStr) throws DocumentException {
        Map<String,String> map = new HashMap<String, String>();
        Document doc = DocumentHelper.parseText(xmlStr);

        if(doc == null)
            return map;
        Element root = doc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
            Element e = (Element) iterator.next();
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    /**
     * map 转 xml (仅微信支付xml格式)
     *
     * @param dataMap map 数据
     * @return
     */
    public static String map2Xml(Map<String, String> dataMap){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : dataMap.keySet()) {
            String value = "<![CDATA[" + dataMap.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
        }
        sb.append("</xml>");

        return sb.toString();
    }

    /**
     * 判断 map 对象是否为空
     * 当 map != null 且 map 中有值时,返回 true,否则返回 false
     *
     * @param map map 对象
     * @return map 对象为空的判断结果
     */
    public static boolean isEmpty(Map map){
        if(map == null || map.isEmpty()){
            return true;
        }
        return false;
    }




}
