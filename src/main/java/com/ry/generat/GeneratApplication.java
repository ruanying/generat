package com.ry.generat;

import com.ry.generat.util.GeneratorUtil;

public class GeneratApplication {

    public static void main(String[] args) {
        System.out.println("[****** 开始生成代码 *********]");
        GeneratorUtil.generatPackage(true, true, true, true, true, true);
        System.out.println("[****** 已完成生成代码 *******]");
    }

}
