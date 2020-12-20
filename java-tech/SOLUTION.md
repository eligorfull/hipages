Tech Challenge Solution 
=======================

# Project Structure

The solution involves two projects:

1. hipages - Client 
It is an angular application which displays the jobs and communicates 
with the server to obtain the data and update the information. This application has two main 
components jobs.component (list of jobs in new and accepted status which depends on the current tab selected) 
and job.component (which contains the details of each job, if the job is in 'accepted' status then some additional info would be displayed)

2. HiPagesApplication (java-tech) - Server
It is a Java - Springboot application which uses hibernate to retrieve the information from the DB.
This contains the main logic and manages the access to the DB to retrieve and update information.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)
* [Node.js] (https://nodejs.org/en/download/package-manager/)


### Run

1. Start database (The init.sql script will be inserted automatically into the DB):

    ```
    docker-compose up -d
    ```
   
2. Run the HiPagesApplication Springboot LunchApplication

3. The job endpoint is /job (e.g. http://localhost:8080/job) which has two rest endpoints.
    4.1. GET  -> /{jobStatus} to retrieve the jobs by a given status (e.g. http://localhost:8080/job/new)
    4.2. POST -> /change-status to modify the status of a job by a given job id (e.g. http://localhost:8080/job/change-status) 
                    expects a RequestBody json that converts to ChangeJobStatusDto changeJobStatusDto which contains the id of the job to be updated and the new status 

4. Tests
Test cases (unit tests) were included to validate the functionality at each layer of the application including
repository
service
controller