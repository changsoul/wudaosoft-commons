/*
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */
package com.wudaosoft.commons.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * JPA POJO字段映射插件
 * 
 * @author Changsoul.Wu
 */
public class JpaPojoPlugin extends PluginAdapter {

    private FullyQualifiedJavaType entityAt;
    private FullyQualifiedJavaType tableAt;
    private FullyQualifiedJavaType idAt;
    private FullyQualifiedJavaType columnAt;
    private FullyQualifiedJavaType gvAt;
    private FullyQualifiedJavaType gtAt;
    private FullyQualifiedJavaType ggId;
    

    public JpaPojoPlugin() {
        super();
        entityAt = new FullyQualifiedJavaType("javax.persistence.Entity");
        tableAt = new FullyQualifiedJavaType("javax.persistence.Table");
        idAt = new FullyQualifiedJavaType("javax.persistence.Id");
        columnAt = new FullyQualifiedJavaType("javax.persistence.Column");
        gvAt = new FullyQualifiedJavaType("javax.persistence.GeneratedValue");
        gtAt = new FullyQualifiedJavaType("javax.persistence.GenerationType");
        ggId = new FullyQualifiedJavaType("org.hibernate.annotations.GenericGenerator");
    }

    public boolean validate(List<String> warnings) {
        // this plugin is always valid
        return true;
    }

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		topLevelClass.addImportedType(entityAt);
		topLevelClass.addImportedType(tableAt);
		topLevelClass.addImportedType(columnAt);
		
		topLevelClass.addAnnotation("@Entity");
		topLevelClass.addAnnotation("@Table(name=\""+introspectedTable.getFullyQualifiedTableNameAtRuntime()+"\")");
		return true;
	}

	@Override
	public boolean modelGetterMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		
		if(introspectedTable.getPrimaryKeyColumns().size() == 1) {
			IntrospectedColumn pc = introspectedTable.getPrimaryKeyColumns().get(0);
			if(introspectedColumn.getJavaProperty().equalsIgnoreCase(pc.getJavaProperty())) {
				method.addAnnotation("@Id");
				topLevelClass.addImportedType(idAt);
				
				if(pc.isIdentity()) {
					method.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
					topLevelClass.addImportedType(gvAt);
					topLevelClass.addImportedType(gtAt);
				}
				
				if(pc.isStringColumn()) {
					method.addAnnotation("@GeneratedValue(generator = \"uuidGenerator\")");
					method.addAnnotation("@GenericGenerator(name = \"uuidGenerator\", strategy = \"uuid2\")");
					topLevelClass.addImportedType(gvAt);
					topLevelClass.addImportedType(ggId);
				}
			}
		}
		
		StringBuilder an = new StringBuilder("@Column(");
		an.append("name=\"").append(introspectedColumn.getActualColumnName()).append("\"");
		
		if(introspectedColumn.isStringColumn()) {
			int len = introspectedColumn.getLength();
			
			if(len <=0)
				len = 2048;
			
			an.append(", length=").append(len);
		}
		
		if(introspectedColumn.getScale() > 0) {
			
			an.append(", scale=").append(introspectedColumn.getScale());
		}
		
		if(!introspectedColumn.isNullable()) {
			
			an.append(", nullable=").append(introspectedColumn.isNullable());
		}
		
		an.append(")");
		method.addAnnotation(an.toString());
		
		return true;
	}
    
}
