NOW_TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

# Toggle port Number
if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "[$NOW_TIME] No WAS is connected to nginx" >> /home/ubuntu/hous/deploy_err.log
  exit 1
fi

echo "[$NOW_TIME] Start health check of WAS at 'http://127.0.0.1:${TARGET_PORT}' ..." >> /home/ubuntu/hous/deploy.log

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10
do
  echo "[$NOW_TIME] #${RETRY_COUNT} trying..." >> /home/ubuntu/hous/deploy.log
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:${TARGET_PORT}/health)

  if [ ${RESPONSE_CODE} -eq 200 ]; then
    echo "[$NOW_TIME] New WAS successfully running" >> /home/ubuntu/hous/deploy.log
    exit 0
  elif [ ${RETRY_COUNT} -eq 10 ]; then
    echo "[$NOW_TIME] Health check failed." >> /home/ubuntu/hous/deploy_err.log
    exit 1
  fi
  sleep 10
done
