package com.gsafety.starscream.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.CollectionUtils;

/**
 * @desc xml、map转换工具类
 * @author wangwei
 * @date 2019-2-18
 */
public class XmlMapUtil {
	 /**
     * xml字符串转map
     * @param xml
     * @return
     */
    public static Map<String, Object> xml2map(String xml)
    {
        Document doc = null;
        try
        {
            //将xml字符串转为document对象
            doc = DocumentHelper.parseText(xml);
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
        {
            return map;
        }
        else
        {
            //获取根节点
            Element rootElement = doc.getRootElement();
            element2map(rootElement, map);
        }
        return map;
    }
    
    /**
     * map转xml字符串
     * @param map
     * @return
     */
    public static String map2xml(Map<String, Object> map, String rootElement)
    {
        StringBuffer xmlStr = new StringBuffer("");
        if (CollectionUtils.isEmpty(map))
            return xmlStr.toString();
        xmlStr.append("<" + rootElement + ">");
        map2Element(map, xmlStr);
        xmlStr.append("</" + rootElement + ">");
        return xmlStr.toString();
    }
    
    /**
     * 递归原则 : 一个map对应一个xml节点,key为当前xml节点名,value为当前xml节点子集
     * 如果xml节点没有子节点(叶子节点),那么map的key为xml节点名,value为xml节点文本内容
     * 如果xml节点有一个子节点,那么map的key为xml节点名,value为xml节点子集
     * 如果xml节点有多个子节点,对应map的key不存在(每一次),map的key为xml节点名,value为xml节点子集
     * 如果xml节点有多个子节点,对应map的key已存在,且value为map类型(第二次),map的key为xml节点名,值从map类型转为list,而list中添加2份当前xml节点子集
     * 如果xml节点有多个子节点,对应map的key已存在,且value为list类型(第三/多次),那么直接加到list中去.
     * @param elmt
     * @param map
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private static void element2map(Element elmt, Map<String, Object> map)
    {
        if (null == elmt)
        {
            return;
        }
        String name = elmt.getName();
        
        //判断当前节点的内容是否为文本（最里面一层节点）
        if (elmt.isTextOnly())
        {
            map.put(name, elmt.getText());
        }
        else
        {
            Map<String, Object> mapSub = new HashMap<String, Object>();
            
            //获取当前节点的所有子节点
            List<Element> elements = (List<Element>)elmt.elements();
            
            //利用递归获取节点值
            for (Element elmtSub : elements)
            {
                element2map(elmtSub, mapSub);
            }
            Object first = map.get(name);
            
            //判断
            if (null == first)
            {
                map.put(name, mapSub);
            }
            else
            {
                if (first instanceof List<?>)
                {
                    ((List<Map<String, Object>>)first).add(mapSub);
                }
                else
                {
                    List<Object> listSub = new ArrayList<Object>();
                    listSub.add(first);
                    listSub.add(mapSub);
                    map.put(name, listSub);
                }
            }
        }
    }
    
    /**
     * 递归map转xml字符串
     * @param map
     * @param sb
     */
    @SuppressWarnings("unchecked")
    private static void map2Element(Map<String, Object> map, StringBuffer sb)
    {
        Set<String> set = map.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext();)
        {
            String key = (String)it.next();
            Object value = map.get(key);
            if (null == value)
                value = "";
            if (value instanceof List<?>)
            {
                List<Object> list = (List<Object>)map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++)
                {
                    Map<String, Object> hm = (Map<String, Object>)list.get(i);
                    map2Element(hm, sb);
                }
                sb.append("</" + key + ">");
            }
            else
            {
                if (value instanceof Map<?, ?>)
                {
                    sb.append("<" + key + ">");
                    map2Element((Map<String, Object>)value, sb);
                    sb.append("</" + key + ">");
                }
                else
                {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }
                
            }
        }
    }
    
    public static void main(String[] args) {
    	Map map = new HashMap();
    	map.put("userid", "admin");
    	map.put("psswd", "adminpass");
		String str = map2xml(map, "request");
		System.out.println("+++++++++++"+str);
		Map xmlMap = xml2map(str);
		Map requertMap = (Map)xmlMap.get("request");
		System.out.println("+++++++++++"+requertMap.get("userid").toString());
	}
}
