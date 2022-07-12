# spring-state-machine-demo

This template demonstrating the pre-authorization payment mechanism using Axon framework with event sourcing.
Spring boot application consuming events from kafka, payment aggregate changing the behavior w.r.t the command and  event and persisting state to the mongo db.

MongoDB is used as event store. Axon framework provides extension for Mongo db.

Payment aggregate manging the lifecycle of payment and has states below:
#### States of a state machine -

NEW - Initial State of Payment.

PRE_AUTH - Payment Pre-authorized.

PRE_AUTH_ERROR - Payment Pre-authorization declined.

AUTH - Payment authorization after pre-authorization done.

AUTH_ERROR - Payment authorization failed

#### State machine -

![](/home/knoldus/Downloads/payment.drawio.png)


### How to Run- 

#### Start kafka and MongoDb server locally:
```
docker-compose up -d
```

#### Start the spring-boot application -

Run the main class from intellij
  or
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=false"

```

#### send event to Kafka 

hotel check-in event(new payment and pre-authorization)
```
kafkacat -P -b localhost:9092 -t payment-authorize  src/test/resources/check-in.json
```
hotel check-out event(authorization and amount capture hold at check-in time)
```
kafkacat -P -b localhost:9092 -t payment-authorize  src/test/resources/check-out.json
```

### Results-
#### On hotel check-in event -

Check logs Pre authorization will be done if criteria met i.e, available amount is grater then hold amount.
 - state will change from NEW to PRE_auth, if pre-auth approved.
 - state will change from NEW to PRE_auth_ERROR, if pre-auth declined.

 ##### check the state persist in mongoDB -
   ```
  mongo admin -u root -p rootpassword
  
   ```
 ###### To see databases
 ```
 show dbs
 
admin          0.000GB
axonframework  0.000GB
config         0.000GB
local          0.000GB
payment        0.000GB
 ```
axonframework - db for event store
payment - db to store payment and state.

###### checkout to db's
```
show collections
axonframework

```
###### To see collections in db's
```
show collections
paymentEntity
```

###### To see documents in payment collections
```
db.paymentEntity.find()
{ "_id" : "113", "paymentState" : "PRE_AUTH", "amount" : "500", "_class" : "org.knoldus.axon.state.machine.query.PaymentEntity" }
```

###### To see documents in domainEvents collections
```
db.domainevents.find()
{ "_id" : ObjectId("62cd556760647a242065ef75"), "aggregateIdentifier" : "113", "type" : "PaymentAggregate", "sequenceNumber" : NumberLong(0), "serializedPayload" : "{\"id\":\"4b20db2c-5b55-4278-b3cf-959acf34c0b2\",\"charge\":{\"id\":\"113\",\"holdAmount\":365,\"availableAmount\":500,\"flow\":\"check-in\",\"date\":\"2022-05-5\",\"hotel\":\"The Lalit, New Delhi\",\"authorize\":false}}", "timestamp" : "2022-07-12T11:05:11.091Z", "payloadType" : "org.knoldus.axon.state.machine.event.PreAuthorize", "payloadRevision" : null, "serializedMetaData" : "{\"traceId\":\"f5269a43-16b2-4732-82d0-4bba90433b9a\",\"correlationId\":\"f5269a43-16b2-4732-82d0-4bba90433b9a\"}", "eventIdentifier" : "fbada547-c77f-4316-98bd-972dd1d24c7c" }
{ "_id" : ObjectId("62cd556760647a242065ef76"), "aggregateIdentifier" : "113", "type" : "PaymentAggregate", "sequenceNumber" : NumberLong(1), "serializedPayload" : "{\"id\":\"113\",\"charge\":{\"id\":\"113\",\"holdAmount\":365,\"availableAmount\":500,\"flow\":\"check-in\",\"date\":\"2022-05-5\",\"hotel\":\"The Lalit, New Delhi\",\"authorize\":false}}", "timestamp" : "2022-07-12T11:05:11.095Z", "payloadType" : "org.knoldus.axon.state.machine.event.PreAuthorizationApprove", "payloadRevision" : null, "serializedMetaData" : "{\"traceId\":\"f5269a43-16b2-4732-82d0-4bba90433b9a\",\"correlationId\":\"f5269a43-16b2-4732-82d0-4bba90433b9a\"}", "eventIdentifier" : "6107e199-589c-4843-b08c-7330474d4a56" }
```

#### On hotel check-out event -

Check logs authorization will be done if criteria met i.e, pre-authorization already done for that id as well authorization true in event.
- state will change from PRE_auth to AUTH, if auth approved.
- state will change from PRE_auth_ERROR to AUTH_ERROR, if auth declined.




