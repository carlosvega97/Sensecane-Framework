package com.magc.sensecane.framework.model.database;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.model.BaseEntity;
import com.magc.sensecane.framework.model.database.annotation.Autogenerated;
import com.magc.sensecane.framework.model.database.annotation.Column;
import com.magc.sensecane.framework.model.database.annotation.PrimaryKey;
import com.magc.sensecane.framework.model.database.annotation.Table;
import com.magc.sensecane.framework.model.database.annotation.Unique;

//TODO: try to implement a generic builder if viable
public abstract class TableEntity<T> extends BaseEntity {
	
	protected final Table table;
	protected final List<Field> columns;
	protected final List<Field> autogenerated;
	protected final List<Field> unique;
	
	public TableEntity() {
		this.table = this.getTableAnnotation();
		this.columns = this.findFieldsWithAnnotation(Column.class);
		this.autogenerated = this.findFieldsWithAnnotation(Autogenerated.class);
		this.unique = this.findFieldsWithAnnotation(Unique.class);
	}
	
	public String getTablename() {
		return this.table.value();
	}

	public Column getPrimaryKeyColumn() {
		Column pk = null;
		try {
			pk = this.getFields().stream()
				.filter(f -> !getAnnotations(f, PrimaryKey.class).isEmpty())
				.map(f -> getAnnotations(f, Column.class))
				.map(c -> c.get(0))
				.findFirst().orElse(null);
		} catch (Exception e) {e.printStackTrace();}
		return pk;
	}
	
	public T getId() {
		T result = null;
		
		try {
			Field f = this.getPrimaryKeyField();
			boolean accesible = f.isAccessible();
			f.setAccessible(true);
			result = (T) f.get(this);
			f.setAccessible(accesible);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Field getPrimaryKeyField() {
		Field pk = null;
		try {
			pk = this.getFields().stream()
					.filter(f -> !getAnnotations(f, PrimaryKey.class).isEmpty())
					.findFirst().orElse(null);
		} catch (Exception e) {e.printStackTrace();}
		return pk;
	}
	
	public List<Field> getColumnFields() {
		return this.columns;
	}
	
	private Table getTableAnnotation() {
		return this.getClass().getDeclaredAnnotation(Table.class);
	}
	
	private List<Field> getFields() {
		return Arrays.asList(this.getClass().getDeclaredFields());
	}
	
	private final <A extends Annotation> List<A> getAnnotations(Field field, Class<A> annotation) {
		return Arrays.<A>asList(field.getDeclaredAnnotationsByType(annotation));
	}
	
	private final List<Field> findFieldsWithAnnotation(final Class<? extends Annotation> annotation){
		return this.getFields().stream()
			.filter(f -> getAnnotations(f, annotation).size() > 0)
			.collect(Collectors.toList());
	}
	
}
