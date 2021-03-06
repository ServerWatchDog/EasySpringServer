#!/bin/bash

function rand(){
    min=$1
    max=$(($2-$min+1))
    num=$(($RANDOM+1000000000)) #增加一个10位的数再求余
    echo $(($num%$max+$min))
}

BASE_URL=http://127.0.0.1:8080/api
#read -p '请输入登录密钥：' LOGIN_SESSION
LOGIN_SESSION=$1

export LOGIN=$(
curl -X 'POST' \
  'http://127.0.0.1:8080/api/client/connect' \
  -H 'accept: */*' \
  -H "Authorization: Bearer $LOGIN_SESSION" \
  -H 'Content-Type: application/json' \
  -d '{
  "arch": "x86_64",
  "system": "Windows 10",
  "cpuName": "Intel",
  "cpuCore": 4,
  "memory": 16384000000,
  "disk": 932000000000
}' )

echo $LOGIN
CONNECT_TOKEN=$(echo $LOGIN | jq -r '.data')

while [ true ]; do
  curl -X 'POST' \
    "$BASE_URL/client/push" \
    -H 'accept: */*' \
    -H "Authorization: Bearer $CONNECT_TOKEN" \
    -H 'Content-Type: application/json' \
    -d '{
  "cpuStage": '$(rand 10 70)',
  "usedMemory": '$(rand 163000000 16384000000)' ,
  "usedDisk": 93200000000,
  "usedNetwork": '$(rand 1000 600000)'
}'
echo '上报服务器状态完成！'
  sleep 10
done
