package com.ry.generat.model;

public class Column {

    /**
     * 字段名
     */
    private String column;

    /**
     * 数据类型
     */
    private String jdbcType;

    /**
     * 实体属性名
     */
    private String property;

    /**
     * 首字母大写的实体属性名
     */
    private String upProperty;

    /**
     * 字符最大长度
     */
    private Integer characterMaximumLength;

    /**
     * 注释
     */
    private String comment;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(Integer characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpProperty() {
        return upProperty;
    }

    public void setUpProperty(String upProperty) {
        this.upProperty = upProperty;
    }
}
