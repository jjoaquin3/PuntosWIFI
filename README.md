# PuntosWIFI
[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Pipeline de análisis de datos utilizando los datos abiertos, correspondientes a las Puntos de acceso WiFi en la Ciudad de México para que pueda ser consultado mediante un API Rest.

## Indice

- [Introducción](#introduccion)
- [Ambiente](#ambiente)
- [Diseño](#disenio)
- [Desarrollo](#desarrollo)
    - [API](#api)
    - [Programación Funcional](#pf)
    - [GraphQL](#graphql)
    - [OpenAPi Spring Doc](#doc)
    - [Pruebas Unitarias](#pruebas)
    - [Docker](#docker)
- [Uso/Ejemplos](#uso-ejemplos)


## Introducción <a name="introduccion"></a>

El proyecto nace como un forma de demostrar la capacidades de elegir un stack tecnológico, la organización y bien implementar una solución para la obtención de información.

La información que alimenta la DB se encuentra en [Puntos de acceso WIFI Ciudad de México](https://datos.cdmx.gob.mx/dataset/puntos-de-acceso-wifi-en-la-ciudad-de-mexico).

- Se tiene un conjunto de datos los cuales se deben limpiar siendo esto la primera etapa, un ETL
- Un API con diversas funciones para consulta la información almacenada, con el uso de Spring Boot ✨✨✨
  - Consultas REST
  - Consultas REST con logica creadas a traves de Programación Funcional
  - Consultas GraphQL
  - Consultas GraphQL con logica creadas a traves de Programación Funcional
- La DB se empaqueta en un contenedor
- La API se empaqueta en un contenedor
- Prubas Unitarias en las capas de controladores y servicios
- Archivos dockercompose y dockerfile para el empaquetado de la API y DB


## Ambiente <a name="ambiente"></a>

| Recursos | Info |
| ------ | ------ |
| Eclipse Temurin JDK 17.0.6 | https://adoptium.net/es/temurin/releases/ |
| Maven Project | https://spring.io/guides/gs/maven/ |
| Spring Boot 2.7.8 | https://start.spring.io/ |
| Postgresql 15 | https://www.postgresql.org/about/news/postgresql-15-released-2526/ |
| Project lombok 1.18 | https://projectlombok.org/setup/maven |
| Graphql | https://docs.spring.io/spring-graphql/docs/current/reference/html/ |
| Openapi 1.6.14 | https://springdoc.org/ |
| Docker  20.10.22| https://www.docker.com/products/docker-desktop/ |
| Pentaho Data Integration 9.3.0.0-428 | https://sourceforge.net/projects/pentaho/ |
| GitHub | https://github.com/jjoaquin3/PuntosWIFI |

Estos serian los principales, las otras libreria como Mocketio, JUnit, entro otros, se pueden encontrar en pom.xml del API


## Diseño <a name="disenio"></a>

La solucion general:

![1  ALL](https://user-images.githubusercontent.com/12112344/220410100-45b4f114-d3b0-4859-9a60-ff7a405a42d9.png)

A nivel de API el diseño es:

![2  API](https://user-images.githubusercontent.com/12112344/220814619-8b092cb2-ba3c-494b-b9ca-9f3473eeb9fd.png)


## Desarrollo  <a name="desarrollo"></a>

### ETL <a name="etl"></a>

Limpiemos la información con Pentaho Data Integration

Nuestro JOB es sencillo, inicio, cargo la transformacion y si todo bien marcar como completo si no a error.

![4](https://user-images.githubusercontent.com/12112344/220413957-f604b8fc-3140-4620-8729-a9a97253ed7d.png)

Ahora con nuestra transformación

![5](https://user-images.githubusercontent.com/12112344/220414213-240a2316-d50d-4bd0-a6db-aa7baa59537a.png)

1. "CSV file input" Obtención de data raw, es nuestro archivo csv a cargar en nuestra DB
2. "Modified JavaScript value" limpiemos, hay strings, formatos de fecha, etc.
3. "Select values" formateamos al tipo de dato que nos sirve mas y esta mapeado en nuestra DB
4. "Table output" cargamos a la DB, ojo aca, edita tu conexion primero si no te lanzara error


### API <a name="api"></a>

No será muy largo, pero empecemos por la estructura:

![3  layers](https://user-images.githubusercontent.com/12112344/220411902-7b8d96cf-59a7-4ee3-bd4b-012f2bd46f64.png)

1. Controller: para hacer que nuestras funciones sean visibles al exterior
2. Service: la logica del negocio
3. Respository: el ente que nos permite manipular los modelos y accesar a la DB
4. Model: la representacion de las entidades en la DB, Modelo Punto y 2 archvios DTO para 1 Proyeccion JPA y el otro como auxiliar para calculo, no es necesario estos, pero estan para ejemplificar las diferentes formas de operar o realizar una solucion.


### Modelo 

Usemos Lombok para ahorrar codigo
Tambien anotaciones de Open API

``` java
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "PUNTO")
@Schema(description = "Modelo que representa la información de los puntos WIFI")
public class Punto
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(required = true, description = "Icremental, Llave Subrogada")
	private Long dbid;

```

### Repository

Hagamos uso de page and sorting para la paginacion, ojo aca que en la capa de servicio tambien esta como hacerlo medianto programacion funcional

``` java
@Repository
public interface IPuntoRepository extends PagingAndSortingRepository<Punto, Long>
{
	@Operation(summary = "Obtener todos los Puntos WIFI")
	@Parameter(description = "Pagina sobre la cual se vaciará la información creada en la capa de servicio", name = "pageable", required = true)
	Page<Punto> findAll(Pageable pageable);		

```


### Service

Sin ir mas lejos, hacemos uso de nuestro reposository, las funciones que terminan con "FP" son de programación funcional

 
``` java
@Service
public class PuntoService
{
	private final IPuntoRepository puntoRepository;

	public PuntoService(IPuntoRepository puntoRepository)
	{
		this.puntoRepository = puntoRepository;
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB")
	public List<Punto> findAll(Integer pageNo, Integer pageSize)
	{
		// 1. Crear paging para limitar por pagina y tamaño
		// 2. Retornar lista con ayuda de Query Methods

		Pageable paging = PageRequest.of(pageNo, pageSize);
		return this.puntoRepository.findAll(paging).toList();
	}
```


### Controller

Hay 3 archivos de controlador, los cuales

- PuntoController, es el controlador clasico REST, usamos PagingAndSortingRepository para el paginado
- PuntoControllerFP, es el controlador REST que hace uso de las funciones de PuntoService que fueron creadas con programación funcional
- PuntoControllerGQ, es el controlador para hacer uso de GraphQL

``` java
@RestController
@RequestMapping("/puntos")
@CrossOrigin(origins = "*")
@Tag(name = "Puntos WIFI", description = "Controlador para operaciones sobre puntos WIFI en Ciuda de Mexico")
public class PuntoController
{
	private final PuntoService puntoService;

	public PuntoController(PuntoService puntoService)
	{
		this.puntoService = puntoService;
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB")
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@GetMapping(produces = "application/json")
	public List<Punto> findAll(
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size
	)
	{
		return puntoService.findAll(page, size);
	}
```

### Programación Funcional <a name="pf"></a>

Nos ubicamos en nuestro achivo de service "PuntoService":

Con el uso de lambda y streams podemos acceder a las colecciones, luego a traves de parametros hacemos los filtrados:

Un retorno de toda la informacion limitada por pagina y tamaño:

``` java
@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB con programación funcional")
public List<Punto> findAllFP(Integer pageNo, Integer pageSize)
{
	// 1. Obtener la lista de todo la información
	// 2. Retornar lista con uso de streams para limitar por pagina y tamaño
  Iterable<Punto> lista = this.puntoRepository.findAll();
	return StreamSupport.stream(lista.spliterator(), false).skip(pageNo * pageSize).limit(pageSize).collect(Collectors.toList());
}
```

El dificil xD, tenemos datos como latitud y longitud en formato double, estos se deben comparar con la informacion guardada y devolver los Puntos mas cercanos ordenados de menos a mayor distancia:
  
``` java
@Operation(summary = "Obtiene lista paginada de puntos WIFI en orden de menor a mayor distancia con programacion funcional")
public List<Punto> findByLatitudAndLongitudFP(Double latitud, Double longitud, Integer pageNo, Integer pageSize)
{
	// 0. Obtener la lista de todo la información
	// 1. Función para calcular la distancia entre dos puntos
	// 2. Crear un Stream de las distancias calculadas
	// 3. Ordenar las distancias por orden ascendente
	// 4. Convertir las distancias ordenadas de vuelta a objetos Punto limitando por pagina y tamaño

	Iterable<Punto> puntos = this.puntoRepository.findAll();
	Function<Punto, Double> distancia = punto -> Math.sqrt(Math.pow(punto.getLatitud() - latitud, 2) + Math.pow(punto.getLongitud() - longitud, 2));
	Stream<PuntoDistanciaDTO> stream = StreamSupport.stream(puntos.spliterator(), false).map(punto -> new PuntoDistanciaDTO(punto, distancia.apply(punto)));
	stream = stream.sorted(Comparator.comparing(PuntoDistanciaDTO::getDistancia));
	return stream.map(PuntoDistanciaDTO::getPunto).skip(pageNo * pageSize).limit(pageSize).collect(Collectors.toList());
}
```


### GraphQL <a name="graphql"></a>

Las dependencias en el pom.xml

``` xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-graphql</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.graphql</groupId>
	<artifactId>spring-graphql-test</artifactId>
	<scope>test</scope>
</dependency>
```

Necesitamos crear un schema en nuestro directorio "~/resources/graphql" con extension gqls, yo creo este "Schema.gqls", he aqui tengo un error wajaja pero os  dejo que lo buscais

```
schema
{
	query:Query
}

type Query 
{
	puntosFindAll(page: Int = 0, size: Int = 10): [Punto]
	puntosFindById(id: String!): Punto
	puntosFindByColonia(colonia: String, page: Int = 0, size: Int = 10): [Punto]
	puntosFindByLatitudAndLongitud(latitud: Float, longitud: Float, page: Int = 0, size: Int = 10): [Punto]
   
	puntosFindAllFP(page: Int = 0, size: Int = 10): [Punto]
	puntosFindByIdFP(id: String!): Punto
	puntosFindByColoniaFP(colonia: String, page: Int = 0, size: Int = 10): [Punto]
	puntosFindByLatitudAndLongitudFP(latitud: Float, longitud: Float, page: Int = 0, size: Int = 10): [Punto]
}

type Punto
{
	dbid: ID!
	id: String	
	programa: String
	fecha_instalacion: String
	latitud: Float
	longitud: Float
	colonia: String
	alcaldia: String
	distancia: Float
}
```

Donde type Query, se establecen las funciones GraphQL, los parametros *(nombre: TIPO [= val])* y luego el retorno, en estos los que terminan con FP son los que hacen uso de las funciones credas con Programacion Funcional

Hacer el controlador para permitir consultar nuestros servicios

![Captura de pantalla 2023-02-21 113600](https://user-images.githubusercontent.com/12112344/220418903-9f181cbc-e736-47e2-aef9-576f9d50c132.png)

Donde vemos las anotaciones
- @Controller: para el endpoint
- @QueryMapping: aca el nombre de la funcion debe coincidir con el nombre en nuestro "Schema.gqls" en el apartado de "type query"
- @Argument: para indicar el argumento indicado en el apartado de "type query" en nuestro "Schema.gqls"


### OpenAPi Spring Doc <a name="doc"></a>

En nuestro pom.xml importamos

``` xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.14</version>
</dependency>
```

En nuestro clase RUN xD, creamos un Bean para la informacion inicial de nuestra documentacion
``` java
@Bean java
public OpenAPI springShopOpenAPI()
{
  return new OpenAPI().info(new Info().title("Puntos WIFI API")
      .contact(new Contact().email("jjcacao2@gmail.com").name("José"))
      .description("Pipeline de análisis de datos utilizando los datos abiertos de la Ciudad de México correspondientes a las Puntos de acceso WiFi en la Ciudad de México para que pueda ser consultado mediante un API Rest.")
      .version("v1.0.3").license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0")))
      .externalDocs(new ExternalDocumentation().description("Readme")
          .url("https://github.com/jjoaquin3/PuntosWIFI#readme"));
}
```

y nos da esto, si ingresamos a la ruta http://localhost:8080/swagger-ui/index.html

![8](https://user-images.githubusercontent.com/12112344/220420102-7f6c997d-046f-4fea-abc7-cbe1c8342c3c.png)

Para ello las anotaciones respectivas estan en https://springdoc.org/


### Pruebas Unitarias <a name="pruebas"></a>

Hacemos uso de JUnit y Mockito, esta ya estan por defecto como parte de "spring-boot-starter-test":

Probando nuestras funciones de la capa de servicio
``` java
class PuntoServiceTest
{
	@Mock
	private IPuntoRepository puntoRepository;

	@InjectMocks
	private PuntoService puntoService;

	private Punto punto;

	@BeforeEach
	public void setUp() throws Exception
	{
		punto = Punto.builder().alcaldia("ALC").colonia("COL").dbid((long) 1234).fecha_instalacion(null).id("EL ID")
				.latitud(1.001).longitud(2.002).programa("Varios").build();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void findAll()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		Page<Punto> pageLista = new PageImpl<Punto>(lista);
		Pageable pageRequest = PageRequest.of(1, 10);
		when(puntoRepository.findAll(pageRequest)).thenReturn(pageLista);

		// Test
		List<Punto> listaTest = puntoService.findAll(1, 10);
		assertEquals(listaTest.size(), pageLista.getNumberOfElements());
	}
```

Probando nuestras funciones de la capa de controlador para los realizados con programacion funcional en la capa de servicio

``` java
class PuntoControllerFPTest
{
	@Mock
	private PuntoService puntoService;

	@InjectMocks
	private PuntoControllerFP puntoController;

	private Punto punto;

	@BeforeEach
	public void setUp() throws Exception
	{
		punto = Punto.builder().alcaldia("ALC").colonia("COL").dbid((long) 1234).fecha_instalacion(null).id("EL ID")
				.latitud(1.001).longitud(2.002).programa("Varios").build();
		MockitoAnnotations.openMocks(this);
	}
  
  @Test
	public void FindByIDFP()
	{
		when(puntoService.findByIdFP("2L")).thenReturn(punto);

		// Test
		Punto resultTest = puntoController.findByIdFP("2L");
		assertThat(resultTest.getId().equals("EL ID"));
	}  
```

Corrieron UWU

![10](https://user-images.githubusercontent.com/12112344/220422691-009ca5fa-705e-496b-bbf1-df9f169d94e9.png)

### Docker <a name="docker"></a>

Creamos en la raiz de nuestro proyecto 2 archivos:

Dockerfile
``` sh
FROM eclipse-temurin:17.0.6_10-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} puntos-1.0.0.jar
ENTRYPOINT ["java","-jar","/puntos-1.0.0.jar"]
```
Creamos la imagen de nuestra API con la imagen eclipse-temurin:17.0.6_10-jdk-alpine para ejecutar nuestro jar puntos-1.0.0.jar


docker-compose.yml
``` sh
version: '2'

services:
  app:
    image: 'puntos:latest'
    build:
      context: .
    container_name: app
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/PUNTOSW
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=mysecretpassword
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_SHOW_SQL=false
    ports:
      - 8080:8080
          
  db:
    image: 'postgres'
    container_name: db
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=mysecretpassword
    ports:
      - 5432:5432
```
Enlazamos nuestra APP y la DB, ambos se levantaran en contenedores independientes, ojo aca las contraseñas, nombre de usuario y  claro los puertos

Nos ubicamos con una consola en la raiz de nuestro proyecto ejecutamos,

``` sh
 docker-compose up
```
Nos levantara nuestra APP

![9](https://user-images.githubusercontent.com/12112344/220421371-d012c4b6-0b1d-44d7-bc45-3cd1b72dfdf3.png)



## Uso/Ejemplos <a name="uso-ejemplos"></a>

1. Todos los Puntos paginado
> http://localhost:8080/puntos?page=0&size=20

![11](https://user-images.githubusercontent.com/12112344/220423309-8e4cecac-17d4-4098-8a28-e5d254dc7177.png)


2. Busqueda por ID con GraphQL

> http://localhost:8080/graphql
``` 
query 
{
    puntosFindByIdFP
    #puntosFindById
    (    
        id: "7"
    )
    {
        dbid
        id
        programa
        fecha_instalacion
        latitud
        longitud
        colonia
        alcaldia
        distancia
    }
}
```
![12](https://user-images.githubusercontent.com/12112344/220424343-1a6fe1d6-e073-4f40-98ee-b451e6979fb1.png)


3. Busqueda por Colonia con programacion funcional, paginado

> http://localhost:8080/puntosfp/colonias/MEXICO?page=0&size=2

![13](https://user-images.githubusercontent.com/12112344/220424550-53ec27e7-9729-4630-9ed4-5d222eaea6b6.png)


3. Busqueda por cercania [lat:long], servicio expuesto con GraphQL y logica realizada con programación funcional

> http://localhost:8080/graphql
``` 
query 
{
    puntosFindByLatitudAndLongitudFP
    #puntosFindByLatitudAndLongitud
    (    
        latitud: 19.4443523
        longitud: -99.1307745
        page: 0
        size: 5
    )
    {
        dbid
        id
        programa
        fecha_instalacion
        latitud
        longitud
        colonia
        alcaldia
        distancia
    }
}
```

![14](https://user-images.githubusercontent.com/12112344/220424927-33e2a64b-f681-4c0d-938c-b76a1191fcf6.png)



5. Documentacion REST

> http://localhost:8080/swagger-ui/index.html#/

![15](https://user-images.githubusercontent.com/12112344/220425680-acbc3d32-27ad-46d0-aebb-ba54a3997ac6.png)


6. Graph UI
> http://localhost:8080/graphiql

![16](https://user-images.githubusercontent.com/12112344/220425692-e981700a-ee0f-4afe-92db-d43631f6c3d9.png)


Fin c:

