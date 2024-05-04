Application prerequirements:
 - PostreSQL empty, but existing database
 - Env variables:  
   url: ${JDBC_DATABASE_URL}   
   username: ${JDBC_DATABASE_USERNAME}   
   password: ${JDBC_DATABASE_PASSWORD}


Liquibase initialization fills Directors with Alfred Hitchcock and Jakie Chan, Quentin Tarantino is for manual POST testing.     
No films filled, it is recommended to begin with POST /api/films/upload of Hitchcock.json