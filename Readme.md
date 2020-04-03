# Reactive Cassandra Driver

### Purpose
While `com.datastax.oss:java-driver-mapper-processor` already gives you the flexibility of compile time query generation based on annotations and mapper for better (de)serialization,
this enables you to play with reactive constructs.
E.g. `Mono<T>`, `Flux<T>`...

###How to Install ?

#####Gradle
```groovy
repositories {
	mavenCentral()
	jcenter()
    maven { //Soon we will be on jCenter
        url  "https://dl.bintray.com/ppanda-beta/maven"
    }
}

dependencies {
	annotationProcessor 'ppanda.sharpie:interface-wrapper:0.0.1'
	annotationProcessor 'com.datastax.oss:java-driver-mapper-processor:4.5.1'

	implementation 'com.ppanda:cassandra-java-driver-reactive-mapper-reactor-core:0.0.1'

	testAnnotationProcessor 'ppanda.sharpie:interface-wrapper:0.0.1'
	testAnnotationProcessor 'com.datastax.oss:java-driver-mapper-processor:4.5.1'
}
```

###Usage

```java


@ReactiveDao
interface ProductDao {
	@Select
	Mono<Product> findById(UUID productId);
	
	@Select
	Flux<Product> findAll();
}

@ReactiveMapper
interface InventoryMapper {
	@DaoFactory
	ProductDao productDao(@DaoKeyspace CqlIdentifier keyspace);
}


//...

InventoryMapperUnderlying inventoryMapperUnderlying = new InventoryMapperUnderlyingBuilder(cqlSession).build();
InventoryMapper inventoryMapper = InventoryMapperFactory.wrapUnderlying(inventoryMapperUnderlying);
ProductDao productDao = inventoryMapper.productDao(CqlIdentifier.fromCql("keyspace1"));

```



