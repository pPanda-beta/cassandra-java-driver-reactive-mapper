package com.ppanda.datastax.driver.mapper.annotations;

import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import ppanda.sharpie.tools.interfacewrapper.annotations.AnnotationCaptor;
import ppanda.sharpie.tools.interfacewrapper.annotations.WrapperInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@ApplyMapperAnnotationOnUnderlying
@WrapperInterface(unwrapReturnTypesAnnotatedWith = ReactiveDao.class)
public @interface ReactiveMapper {

}


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@AnnotationCaptor
@interface ApplyMapperAnnotationOnUnderlying {
	Mapper mapper() default @Mapper;
}

