java-jboss-sample-app
=====================
This application was tested in Cloud Foundry environment on JBoss Application Server. 
Application requires connection parameters such as userName, password and url of database connection.
There are two ways how to set this parameters for application. First method is to set system environment variables (list of this parameters could be searched in jdbcconnection.properties file of dbWriter/src/main/resources/ directory). Second way is to create Oracle Cloud Foundry service and to bind it on application. This service would produce database connection parameters in JSON format and application is able to extract required data from this provided parameters.

