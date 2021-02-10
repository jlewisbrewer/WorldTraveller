![#WorldTraveller](https://fontmeme.com/permalink/210209/cf641c6251baa465c04a44f0f4dada67.png)

## Introduction

WorldTraveller is a complete web application that enables users to click on a map of the world and then display to them the percentage of the world
that they have visited. It features an in-memory database to preload country information on the backend which is queried by the frontend. User data is not 
maintained.

## Preview

Initial page:


[![Image of initial website](https://i.postimg.cc/7LbkVqJH/Screen-Shot-2021-02-09-at-10-11-50-PM.png)](https://postimg.cc/kVrzM3yz)

On mouseover, country changes color:

[![Image of country changing color](https://i.postimg.cc/3rFcdPWn/Screen-Shot-2021-02-09-at-10-17-18-PM.png)](https://postimg.cc/fkkKPqc9)

Selected countries will change what percentage of the world the user has visited:

[![Image of countries updating](https://i.postimg.cc/J4t5MgXy/Screen-Shot-2021-02-09-at-10-21-28-PM.png)](https://postimg.cc/4mk91WBZ)




## Development

To run in development mode, navigate to the root of the project and type `mvn spring-boot:run` into a terminal. Navigate to http://localhost:8080 in your browser to access the project.

## References

The Spring Boot tutorials were instrumental for establishing the structure of the project, especially the tutorial [Building Rest Services with Spring](https://spring.io/guides/tutorials/rest/).

## License
This project is available as open source under the terms of the [MIT license](https://opensource.org/licenses/MIT).
