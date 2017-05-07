# street-address-parser
Simple street address parser (draft) with a REST API, for testing Oracle Application Container Cloud.
## Building and running
### Building
$ mvn package
```
```
This produces an executable JAR with all dependencies included, and an Oracle Cloud-specific ZIP file wrapping the JAR 
and metadata, for later deployment to Oracle Application Container Cloud.

### Run locally
Launch the class `org.example.streetaddressparser.Main` in your IDE, or execute with Maven's exec-plugin:
```
$ mvn exec:java
```
This should produce some logging on stdout, and the REST API can now be reached at:
http://localhost:8080/street-address-parser/application.wadl

## Running in Oracle Application Container Cloud

### Sign up for an account and prepare the Storage Cloud Service
At the time of writing, there is a free trial offer at: 
https://cloud.oracle.com/application-container-cloud#

*Note* Account registration and activation might take a couple of hours.
*Note*: Look carefully after information on how to activate the Storage Cloud Service, and set its replication policy. 
Getting this set up correctly is a crucial precondition for running Application Container Cloud nodes. Getting the 
replication policy to take effect may also take a few hours.

### Deployment with the web console
1. Choose `Dashboard`
2. Choose `Navigation Menu` (on the left side)
3. Choose `Application Container`
4. Choose `Create Application`
5. Choose type `Java SE`
6. Set the name of the app to `StreetAddressParser`or similar
7. Browse and select the ZIP file produced in the Maven build above, as the `Archive` 
   of the `Application Artifacts`.
8. Push `Create`   

Wait a few minutes. Progress can be checked by reloading the application listing page of the app container service.

### Deployment with the REST API
In the following commands, we use the following shell variables as placeholders for your Oracle Cloud account 
information (find the values of these in your account settings):
* USER: your Oracle Cloud account user
* PASSWORD: your Oracle Cloud account password
* ID_DOMAIN: your account's identity domain

#### Preauthenticate your REST client
```
$ curl -v -s -X GET -H "X-Storage-User: Storage-$ID_DOMAIN:$USER" -H "X-Storage-Pass: $PASSWORD" https://$ID_DOMAIN.storage.oraclecloud.com/auth/v1.0
...
...
X-Auth-Token: AUTH_tk85d733932d109ac36c61a38eabf5dfdd
X-Storage-Token: AUTH_tk85d733932d109ac36c61a38eabf5dfdd
X-Storage-Url: https://uscom-central-1.storage.oraclecloud.com/v1/Storage-a474133
...
```
Note the value of `X-Auth-Token` and put it in a new shell variable, $AUTH_TOKEN.

#### Upload the deployment unit to Oracle's Storage Cloud
This is necessary to pass references to files as arguments in the REST APIs to deploy and update an application.  

#### Create a New Storage Container
To separate apps from each other, you may create a new storage container for each app. Or, if you don't 
mind a bit of clutter in a single container, you can skip this step, and simply reuse an existing storage container, 
e.g. the default one, `_apaas`.

```
$ curl -v -X PUT -H "X-Auth-Token: $AUTH_TOKEN" https://uscom-central-1.storage.oraclecloud.com/v1/Storage-$ID_DOMAIN/StreetAddressParser

...
 HTTP/1.1 201 Created
...
```
#### Upload the deployment unit to the Storage Cloud
```
$ curl -v -X PUT -H "X-Auth-Token: $AUTH_TOKEN" https://uscom-central-1.storage.oraclecloud.com/v1/Storage-$ID_DOMAIN/StreetAddresParser/street-address-parser-1.0-SNAPSHOT.zip -T ~/target/street-address-parser-1.0-SNAPSHOT.zip
...
HTTP/1.1 201 Created
...
```

#### Deploy the app
```
$ curl -v -X POST -u "$USER:$PASSWORD" -H "X-ID-TENANT-NAME:$ID_DOMAIN" -H "Content-Type: multipart/form-data" -F "name=StreetAddressParser" -F "runtime=java" -F "subscription=Hourly" -F "archiveURL=StreetAddressParser/street-address-parser-1.0-SNAPSHOT.zip" https://apaas.us.oraclecloud.com/paas/service/apaas/api/v1.1/apps/$ID_DOMAIN
...
< HTTP/1.1 202 Accepted
...
* Connection #0 to host apaas.us.oraclecloud.com left intact
{"identityDomain":"a474133","appId":"00e904b2-3bbb-44c6-8540-a8ea35f6f334","name":"EmployeeWebApp","status":"NEW","createdBy":"svenjok@gmail.com","creationTime":"2017-05-02T06:56:45.983+0000","lastModifiedTime":"2017-05-02T06:56:45.962+0000","subscriptionType":"MONTHLY","isClustered":false,"requiresAntiAffinity":false,"instances":[],"lastestDeployment":{"deploymentId":"4cb1aaba-0f8b-4afb-93a4-7ce514083d2b","deploymentStatus":"READY","deploymentURL":"https://apaas.us.oraclecloud.com/paas/service/apaas/api/v1.1/apps/a474133/EmployeeWebApp/deployments/4cb1aaba-0f8b-4afb-93a4-7ce514083d2b"},"currentOngoingActivity":"Creating Application","appURL":"https://apaas.us.oraclecloud.com/paas/service/apaas/api/v1.1/apps/a474133/EmployeeWebApp","message":[],
...
```

#### To redeploy (and possibly change scaling settings with a JSON descriptor)
```
$ curl -v -X PUT -u "$USER:$PASSWORD" -H "X-ID-TENANT-NAME:$ID_DOMAIN" -H "Content-Type: multipart/form-data" -F "runtime=java" -F "subscription=Hourly" -F "archiveURL=StreetAddressParser/street-address-parser-1.0-SNAPSHOT.zip" -Fdeployment=@src/test/deployment-of-2-instances.json https://apaas.us.oraclecloud.com/paas/service/apaas/api/v1.1/apps/a474133/StreetAddressParser
```

### Testing the app
**Note**: Substitute your own identity domain the URL examples below.

Lookup the API's WADL at: 
https://streetaddressparser-a474133.apaas.us2.oraclecloud.com/street-address-parser/application.wadl

Run some GET queries with curl, e.g:
```
$ curl -X GET https://streetaddressparser-a474133.apaas.us2.oraclecloud.com/street-address-parser/street-addresses/Stortorvet%201
{"name":"Stortorvet","no":1}
```

Finally, have a look at the requests in the SoapUI project in source folder `src/test/soapui`. 












