package com.ppanda.datastax.driver.mapper;


import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.*;
import com.ppanda.datastax.driver.mapper.annotations.ReactiveDao;
import com.ppanda.datastax.driver.mapper.annotations.ReactiveMapper;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.forName;

public class IntegrationTest {
	@Rule
	public CassandraCQLUnit cassandraCQLUnit =
			new CassandraCQLUnit(
					new ClassPathCQLDataSet("cql/multi-line-statements.cql", "keyspace1"));
	private EasyRandom easyRandom;
	private ProductDao productDao;
	
	@Before
	public void setUp() throws Exception {
		EasyRandomParameters parameters = new EasyRandomParameters()
												  .seed(123L)
												  .objectPoolSize(100)
												  .randomizationDepth(3)
												  .charset(forName("UTF-8"))
												  .stringLengthRange(5, 50)
												  .collectionSizeRange(1, 10)
												  .scanClasspathForConcreteTypes(true)
												  .overrideDefaultInitialization(false)
												  .ignoreRandomizationErrors(true);
		
		easyRandom = new EasyRandom(parameters);
		
		InventoryMapperUnderlying inventoryMapperUnderlying = new InventoryMapperUnderlyingBuilder(cassandraCQLUnit.getSession()).build();
		InventoryMapper inventoryMapper = InventoryMapperFactory.wrapUnderlying(inventoryMapperUnderlying);
		productDao = inventoryMapper.productDao(CqlIdentifier.fromCql("keyspace1"));
	}
	
	@After
	public void tearDown() throws Exception {
		Mono.from(productDao.deleteAll()).block();
	}
	
	@Test
	public void shouldSave() {
		Product expectedProduct = easyRandom.nextObject(Product.class);
		
		StepVerifier.create(productDao.saveReactive(expectedProduct))
				.verifyComplete();
	}
	
	@Test
	public void shouldFindById() {
		Product expectedProduct = easyRandom.nextObject(Product.class);
		
		Mono.from(productDao.saveReactive(expectedProduct))
				.block();
		
		Mono<Product> productMono = productDao.findById(expectedProduct.getId());
		
		StepVerifier.create(productMono)
				.expectNext(expectedProduct)
				.verifyComplete();
	}
	
	@Test
	public void shouldFindAll() {
		Set<Product> someProducts = easyRandom.objects(Product.class, 50).collect(Collectors.toSet());
		
		Flux.fromIterable(someProducts)
				.flatMap(productDao::saveReactive)
				.blockLast();
		
		Flux<Product> foundProducts = productDao.findAll().cache();
		
		StepVerifier.create(foundProducts)
				.expectNextCount(50)
				.recordWith(ArrayList::new)
				.expectRecordedMatches(someProducts::containsAll)
				.verifyComplete();
	}
}


@ReactiveMapper
interface InventoryMapper {
	@DaoFactory
	ProductDao productDao(@DaoKeyspace CqlIdentifier keyspace);
}

@ReactiveDao
interface ProductDao {
	@Select
	Mono<Product> findById(UUID productId);
	
	@Select
	Flux<Product> findAll();
	
	@Insert
	ReactiveResultSet saveReactive(Product product);
	
	@Query("TRUNCATE product")
	ReactiveResultSet deleteAll();
}

@Entity
class Product {
	
	@PartitionKey
	private UUID id;
	private String description;
	
	public Product() {
	}
	
	public Product(UUID id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Product product = (Product) o;
		
		if (id != null ? !id.equals(product.id) : product.id != null) return false;
		return description != null ? description.equals(product.description) : product.description == null;
	}
	
	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}
}


