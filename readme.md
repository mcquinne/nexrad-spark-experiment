Nexrad Spark Experiment
===

This is an experiment using Docker in swarm mode to create an Apache Spark
cluster that operates on the Nexrad radar data which has been uploaded to the
[noaa-nexrad-level2 S3 bucket](https://aws.amazon.com/noaa-big-data/nexrad/) on AWS.

### Requirements

At the moment this project as some external dependencies:

1. Docker CLI installed locally, pointing to a Docker engine that is:
    - Version 1.13+
    - In swarm mode
    - A swarm manager node
1. If you want to use the `localCluster.sh` script to provision machines, you need to
have docker-machine installed and configured to create engines at version 1.13+

### Disclaimer
 
This is basically a sandbox of mine. Play at your own risk.
 
### Usage

1. `localCluster.sh` to provision a local cluster of 4 virtualbox machines in swarm mode
1. `eval $(docker-machine env nexrad0)` to configure your shell to point to the master machine
1. `./gradlew build` to build the docker images on the master machine
1. `./gradlew deploy` to deploy the stack, including the processor image you just built
1. `docker logs -f $(docker ps -aql --filter name=nexrad_processor*)` to tail the logs of the 
processor container
1. `./gradlew remove` to remove the stack
1. Develop, rinse, repeat steps 3 through 6

### Local testing
 
My intent was to be able to spin up a local swarm that included a mock S3 service, but
getting it all wired up threw too many hurdles in the way, so at the moment it's 
hitting the real bucket, but only working on a small subset of the data unless you have
FULL_RESULTS=true set when you deploy the stack.

Maybe someday I'll get the fake-s3 image wired up correctly and use the mock-s3-init
to initialize it. We'll see.

### Credits

While I've moved away from it a bit in switching to Docker's new swarm mode,
[this blog post](https://medium.com/@daviws/a-swarm-of-sparks-8f5a4afc72cc#.2vekufc8q)
by Davi de Castro Reis was extremely helpful in getting me started.


