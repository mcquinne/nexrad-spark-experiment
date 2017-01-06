#!/usr/bin/env sh

TAG="nexrad-processor"
NETWORK="nexrad_submission"

pushd $(dirname $0)

../gradlew build
docker service rm ${TAG}
docker build -t ${TAG} .
docker service create --name ${TAG} --network ${NETWORK} \
  --constraint "node.role == manager" --restart-condition none ${TAG} \
  $*
#docker run --interactive --tty --rm --network ${NETWORK} ${TAG}

popd
