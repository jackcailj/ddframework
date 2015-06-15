package com.dangdang.ddframework.util;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class ResourceLoader {

		/**
		 * 如果配置文件与ResourceLoader类在同一个包中用此方法
		 * 
		 * @param PropertiesName
		 * @return
		 */
        public static Properties loadPropertyFile(String PropertiesName) {

              Properties props = new Properties();

              try
              {
                  InputStream in = ResourceLoader.class.getResourceAsStream(PropertiesName);
                 if (in != null)
                 {
                    props.load(in);
                 } 
                 else
                 {
                    throw new IllegalArgumentException("No file exists for" + PropertiesName);
                 }
              }
              catch (Exception e)
              {
                 e.printStackTrace();
              }

              return props;
        }
        
        /**
         * 如果配置文件在obj包中，用此方法
         * @param obj
         * @param PropertiesName
         * @return
         */
        public static Properties loadPropertyFile(Class obj,  String PropertiesName) {

              Properties props = new Properties();

              try
              {
                  InputStream in = obj.getResourceAsStream(PropertiesName);
                 if (in != null)
                 {
                    props.load(in);
                 } 
                 else
                 {
                    throw new IllegalArgumentException("No file exists for" + PropertiesName);
                 }
              }
              catch (Exception e)
              {
                 e.printStackTrace();
              }

              return props;
        }
        
        /**
         * base name is file name if file located at class path root directory
         * base name is directory + file full name if file at some folder inside class path list.
         * eg: myPro.properties
         * project structure
         * pro--src
         *  |    |---com.mytest.pkg
         *  |    |               |-----Hello.java
         *  |    |               |-----test.properties
         *  |---myPro.properties
         *  baseName= com/mytest/pkg/test.properties if load test.properties
         *  baseName= myPro.properties if load myPro.properties
         *
         * @param baseName
         * @return
         */
        public static Properties loadCurrentPropertyFile(String baseName) {
            Properties props = new Properties();

            try
            {
                InputStream in = ResourceLoader.class.getClassLoader().getResourceAsStream(baseName);
               if (in != null)
               {
                  props.load(in);
               } 
               else
               {
                  throw new IllegalArgumentException("No file exists for" + baseName);
               }
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }

            return props;
        }
        
        public static Properties loadSystemPropertyFile(String name, ClassLoader loader) {
            Properties props = new Properties();

            try
            {
            	if(loader == null)
            		loader = ClassLoader.getSystemClassLoader();
                InputStream in = loader.getResourceAsStream(name);
               if (in != null)
               {
                  props.load(in);
               } 
               else
               {
                  throw new IllegalArgumentException("No file exists for" + name);
               }
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }

            return props;
        }
        
        /**
         * resourceURL is file name if file located at class path root directory
         * resourceURL is directory + file full name if file at some folder inside class path list.
         * eg: myPro.properties
         * project structure
         * pro--src
         *  |    |---com.mytest.pkg
         *  |    |               |-----Hello.java
         *  |    |               |-----test.properties
         *  |---myPro.properties
         *  resourceURL= com.mytest.pkg.test.properties if load test.properties
         *  resourceURL= myPro.properties if load myPro.properties
         *
         * @param baseName
         * @return
         */
        public static ResourceBundle loadResourceBundle(String resourceURL) {

                Locale locale = Locale.ENGLISH;
                ResourceBundle bundle = null;

                try {
                        bundle = ResourceBundle.getBundle(resourceURL, locale);
                        if (bundle == null) {
                                throw new IllegalArgumentException("No file exists for" + resourceURL);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                return bundle;
        }

}