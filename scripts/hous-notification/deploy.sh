NOW_TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

HOST_NAME=$(cat /etc/hostname)
TARGET_PORT=8083

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "[$NOW_TIME] Kill WAS running at ${TARGET_PORT}." >> /home/ubuntu/hous-notification/deploy.log
  sudo kill ${TARGET_PID}
fi

if [ ${HOST_NAME} == "hous-prod-server" ]; then
  nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.profiles.active=prod /home/ubuntu/hous-notification/*.jar >> /home/ubuntu/hous-notification/deploy.log 2>/home/ubuntu/hous-notification/deploy_err.log &
  echo "[$NOW_TIME] Now new WAS runs at ${TARGET_PORT}." >> /home/ubuntu/hous-notification/deploy.log
  exit 0
else
  nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.profiles.active=dev /home/ubuntu/hous-notification/*.jar >> /home/ubuntu/hous-notification/deploy.log 2>/home/ubuntu/hous-notification/deploy_err.log &
  echo "[$NOW_TIME] Now new WAS runs at ${TARGET_PORT}." >> /home/ubuntu/hous-notification/deploy.log
  exit 0
fi
