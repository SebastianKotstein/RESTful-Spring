# RESTful-Spring
A framework that supports the creation of RESTful applications with Spring.
This repository contains several utilities that should simplify the implementation of RESTful applications with Spring as well as development guidelines aligned with the original constraints of REST and several REST API design rules and best practices proposed in literature.

## Features
- Classes and methods for producing and consuming resource representations following the principles of HATEOAS (Hypermedia as the Engine of Application State). The class [`Hypermedia`](https://github.com/SebastianKotstein/RESTful-Spring/blob/master/src/main/java/de/skotstein/lib/spring/restfulspring/model/entities/Hypermedia.java) serves as a base class for any kind of hypermedia representation and allows one to easily embed and extract hyperlinks. The serialized result is a "HAL (Hypertext Application Language)"-like JSON structure. 
- Methods for converting exceptions into uniform and HATEOAS-compliant error representations 
- Helper class for splitting a collection into multiple pages and embedding pagination-related hyperlinks into returned representations
- Several filtering methods for applying queries on collections
- A HAL-compatible REST client implementation

