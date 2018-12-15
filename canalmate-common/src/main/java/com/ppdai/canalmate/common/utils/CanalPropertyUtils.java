package com.ppdai.canalmate.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/*
 * 读取canal的properties信息
 */
public class CanalPropertyUtils {


  public static String getPropertyValueByKey(String configProperty, String key) {
    // P.p(configProperty);
    // P.p("key:"+key);
    InputStream inStream =
        new ByteArrayInputStream(configProperty.getBytes(StandardCharsets.UTF_8));
    Properties prop = new Properties();
    try {
      prop.load(inStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String value = prop.getProperty(key);

    return value;

  }

}
