REPOSITORY=/home/ec2-user/applications
APP_NAME=AuctionApp
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f $APP_NAME)
echo "$CURRENT_PID"

# 빌드된 Jar 파일 실행합니다.
# 이미 실행 중이라 해당 process를 kill하고 새 버전의 서버 프로세스를 실행합니다.
if [ -z $CURRENT_PID ]
then
  echo "> 실행중인 해당 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 어플리케이션 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
