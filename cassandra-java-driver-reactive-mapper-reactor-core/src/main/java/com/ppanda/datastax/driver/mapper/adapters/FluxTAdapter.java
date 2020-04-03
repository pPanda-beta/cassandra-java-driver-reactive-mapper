package com.ppanda.datastax.driver.mapper.adapters;

import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import ppanda.sharpie.tools.interfacewrapper.converters.TypeConverter;
import reactor.core.publisher.Flux;

public class FluxTAdapter<T> implements TypeConverter<Flux<T>, MappedReactiveResultSet<T>> {
	
	@Override
	public Flux<T> convertFrom(MappedReactiveResultSet<T> original) {
		return Flux.from(original);
	}
}
