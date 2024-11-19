package com.ry.generat.util;

import com.ry.generat.model.Column;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class GeneratorUtil {

    /**
     * 生成代码
     */
    public static void generatPackage(boolean mapperInterface, boolean model, boolean service, boolean controller,
        boolean xml, boolean queryDto,boolean editDto,boolean modelVo) {

        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();

        VelocityContext ctx = new VelocityContext();

        PropertyUtil instance = PropertyUtil.getInstance();
        String moduleProviderPath = instance.get("module.provider.path");
        String moduleApiPath = instance.get("module.api.path");

        String moduleBasePackage = instance.get("module.base.package");

        String className = instance.get("appointClassName");
        String author = instance.get("model.author");

        String tableSchema = PropertyUtil.getInstance().get("table.schema");
        String tablePreDel = PropertyUtil.getInstance().get("table.pre.del");
        String tableName = PropertyUtil.getInstance().get("table.name");

        if (StringUtils.isBlank(className)) {
            if (StringUtils.isNotBlank(tablePreDel)) {
                int i = tableName.indexOf(tablePreDel);
                if (i == 0) {
                    className = tableName.replace(tablePreDel, "");
                }
            } else {
                className = tableName;
            }
            className = Underline2Camel.underline2Camel(className, false);
        }

        List<Column> list = DBUtil.db(tableSchema, tableName);
        if (list.isEmpty()) {
            System.out.println("[****** 数据库链接失败 *******]");
            return;
        }
        String tableComment = DBUtil.tableComment(tableSchema, tableName);
        // 设置变量
        ctx.put("tableComment", tableComment);// 表注释
        ctx.put("model", className);// 实体类名
        ctx.put("model_", Underline2Camel.lowerCase(className));// 实体类名首字符小写
        ctx.put("tableName", tableName);// 表名
        ctx.put("tableColumns", list);// 表字段相关信息
        ctx.put("basePackage", moduleBasePackage);// 基础包
        ctx.put("types", TypeConverter.getTypes());// jdbcType和javaType对应设置
        ctx.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 代码生成时间
        ctx.put("author", author);// 代码作者

        String srcMainJavaPath = "/src/main/java";
        String srcMainResourcesPath = "/src/main/resources";
        String moduleBasePackagePath = "/" + StringUtils.replaceAll(moduleBasePackage, "\\.", "/");
        if (xml) {
            String xmlPath = moduleProviderPath + srcMainResourcesPath + "/mapper/";
            createPath(xmlPath, className + "Mapper.xml");
            generator(engine, ctx, "vm/xml.vm", xmlPath, className + "Mapper.xml");
        }
        if (editDto) {
            String editDtoPath = moduleApiPath + srcMainJavaPath + moduleBasePackagePath + "/model/dto/";
            createPath(editDtoPath, className + "EditDTO.java");
            generator(engine, ctx, "vm/editDTO.vm", editDtoPath, className + "EditDTO.java");
        }
        if (queryDto) {
            String queryDtoPath = moduleApiPath + srcMainJavaPath + moduleBasePackagePath + "/model/dto/";
            createPath(queryDtoPath, className + "QueryDTO.java");
            generator(engine, ctx, "vm/queryDTO.vm", queryDtoPath, className + "QueryDTO.java");
        }
        if (modelVo) {
            String modelVoPath = moduleApiPath + srcMainJavaPath + moduleBasePackagePath + "/model/vo/";
            createPath(modelVoPath, className + "VO.java");
            generator(engine, ctx, "vm/modelVO.vm", modelVoPath, className + "VO.java");
        }
        if (model) {
            String modelPath = moduleProviderPath + srcMainJavaPath + moduleBasePackagePath + "/model/po/";
            createPath(modelPath, className + ".java");
            generator(engine, ctx, "vm/model.vm", modelPath, className + ".java");
        }
        if (mapperInterface) {
            String mapperInterfacePath = moduleProviderPath + srcMainJavaPath + moduleBasePackagePath + "/mapper/";
            createPath(mapperInterfacePath, className + "Mapper.java");
            generator(engine, ctx, "vm/mapper.vm", mapperInterfacePath, className + "Mapper.java");
        }
        if (service) {
            String pre = "I";
            // if (className.indexOf(pre) == 0) {
            //     pre = "";
            // }
            String servicePath = moduleProviderPath + srcMainJavaPath + moduleBasePackagePath + "/service/";
            createPath(servicePath, "I" + className + "Service.java");
            generator(engine, ctx, "vm/service.vm", servicePath, pre + className + "Service.java");

            String serviceImplPath = moduleProviderPath + srcMainJavaPath + moduleBasePackagePath + "/service/impl/";
            createPath(serviceImplPath, className + "ServiceImpl.java");
            generator(engine, ctx, "vm/serviceImpl.vm", serviceImplPath, className + "ServiceImpl.java");
        }
        if (controller) {
            String controllerPath = moduleProviderPath + srcMainJavaPath + moduleBasePackagePath + "/controller/";
            generator(engine, ctx, "vm/controller.vm", controllerPath, className + "Controller.java");
        }
    }

    private static void generator(VelocityEngine engine, VelocityContext ctx, String tmpName, String tarPackage,
        String modelName) {
        Template template = engine.getTemplate(tmpName);
        StringWriter sw = new StringWriter();
        template.merge(ctx, sw);
        try {
            Files.write(Paths.get(tarPackage, modelName), sw.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createPath(String first, String name) {
        try {
            Path path1 = Paths.get(first, name);
            if (Files.exists(path1)) {
                Files.delete(path1);
            }
            Path path = Paths.get(first);
            Files.createDirectories(path);
            Files.createFile(path1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
