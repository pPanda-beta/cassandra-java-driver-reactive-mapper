package com.ppanda.datastax.driver.mapper.adapters;

import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import ppanda.sharpie.tools.interfacewrapper.converters.TypeConverter;
import reactor.core.publisher.Mono;

public class MonoTAdapter<T> implements TypeConverter<Mono<T>, MappedReactiveResultSet<T>> {
	
	@Override
	public Mono<T> convertFrom(MappedReactiveResultSet<T> original) {
		return Mono.from(original);
	}
}
