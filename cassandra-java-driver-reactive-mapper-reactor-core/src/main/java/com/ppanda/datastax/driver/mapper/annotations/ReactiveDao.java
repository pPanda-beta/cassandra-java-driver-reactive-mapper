package com.ppanda.datastax.driver.mapper.annotations;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.ppanda.datastax.driver.mapper.adapters.FluxTAdapter;
import com.ppanda.datastax.driver.mapper.adapters.MonoTAdapter;
import ppanda.sharpie.tools.interfacewrapper.annotations.AnnotationCaptor;
import ppanda.sharpie.tools.interfacewrapper.annotations.WrapperInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@ApplyDaoAnnotationOnUnderlying
@WrapperInterface(returnTypeConverters = {MonoTAdapter.class, FluxTAdapter.class})
public @interface ReactiveDao {

}


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@AnnotationCaptor
@interface ApplyDaoAnnotationOnUnderlying {
	Dao dao() default @Dao;
}
