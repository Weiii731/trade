package com.weiyang.trade.goods;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis generator
 */
public class GeneratorSqlMap {
    public static void main(String[] args) throws Exception {
        try {
            GeneratorSqlMap generatorSqlMap = new GeneratorSqlMap();
            generatorSqlMap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * mybatis-generator-config
     *
     * @throws Exception
     */

    public void generator() throws Exception {
        File configFile = new File("trade-goods/src/main/resources/mybatis-generator-config.xml");
        List<String> warningInfos = new ArrayList<>();

        DefaultShellCallback callback = new DefaultShellCallback(true);
        ConfigurationParser configurationParser = new ConfigurationParser(warningInfos);
        Configuration config = configurationParser.parseConfiguration(configFile);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warningInfos);
        myBatisGenerator.generate(null);

        for (String warning : warningInfos) {
            System.out.println(warning);
        }
    }
}
