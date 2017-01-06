FROM cgswong/aws

COPY data/* /data/
COPY credentials /root/.aws/
COPY s3cfg /root/.s3cfg

RUN s3cmd mb s3://noaa-nexrad-level2
RUN s3cmd put /data/* s3://noaa-nexrad-level2

ENTRYPOINT ['s3cmd', 'la']
