#!/bin/bash
NOW_TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

BUILD_JAR=$(ls /home/ubuntu/hous/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> [$NOW_TIME] build 파일명: $JAR_NAME" >> /home/ubuntu/hous/deploy.log

echo "> [$NOW_TIME] build 파일 복사" >> /home/ubuntu/hous/deploy.log
DEPLOY_PATH=/home/ubuntu/hous/
cp $BUILD_JAR $DEPLOY_PATH

echo "> [$NOW_TIME] 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/hous/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> [$NOW_TIME] 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/hous/deploy.log
else
  echo "> [$NOW_TIME] kill -15 $CURRENT_PID" >> /home/ubuntu/hous/deploy.log
  kill -15 $CURRENT_PID
  sleep 20
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME

echo "> [$NOW_TIME] DEPLOY_JAR 배포" >> /home/ubuntu/hous/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ubuntu/hous/deploy.log 2>/home/ubuntu/hous/deploy_err.log &
