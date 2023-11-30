# Application BANQUE-MANGER (Springboot)

### Requirement

* Gradle 8.2.1
* JDK 17
* [ Postgres DataBase 12.11 ](https://www.enterprisedb.com/postgresql-tutorial-resources-training?uuid=03b13357-9281-4985-baea-17b72a0febc3&campaignId=7012J000001F5IIQA0)
* add postgres to path(environment variable)
* NB (default password of postgres in our case is : root)

### DataBase Initialisation without docker:

* init dataBase user

```
   psql -U postgres -d postgres -a -f init_database.sql
```

* init data in dataBase

```
   psql -U postgres -d banque -a -f data.sql
```

* drop all database

```
  psql -U postgres -d postgres -a -f drop_database.sql
```
### DataBase Initialisation with docker:
```
  docker-compose up -d
```

### Swagger UI :

[ Swagger ](http://localhost:8083/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/)

## Curl request exemple :
* url : localhost 
* port :8083
* List of deviseCode ["EUR","USD","GBP","JPY"]

#### Deposit Operation:  
```
curl -X 'POST' \
  'http://localhost:8083/api/operation' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "accountReceiverId": 3,
  "amount": 10000,
  "type": "DEPOT",
  "deviseCode": "JPY"
}'

-------------------------------------------
curl -X 'POST' \
  'http://localhost:8083/api/operation' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "accountReceiverId": 3,
  "amount": 20,
  "type": "DEPOT",
  "deviseCode": "EUR"
}'
```
####  Transfer Operation:  
```
curl -X 'POST' \
  'http://localhost:8083/api/operation' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "accountReceiverId": 1,
  "accountSourceId": 3,
  "amount": 50.00,
  "type": "VIREMENT",
  "deviseCode": "EUR"
}'
```

####  withdrawal Operation:
```
curl -X 'POST' \
  'http://localhost:8083/api/operation' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "accountReceiverId": 1,
  "accountSourceId": 3,
  "amount": 50.00,
  "type": "RETRAIT",
  "deviseCode": "EUR"
}'
```