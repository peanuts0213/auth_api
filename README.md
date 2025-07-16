# Docker

## 개발용

```bash
docker build -f dockerfile.dev -t bict/auth-api-dev .
docker tag bict/auth-api-dev 192.168.0.18:5000/bict/auth-api-dev
docker push 192.168.0.18:5000/bict/auth-api-dev
docker run -d --gpus all -v %cd%:/app --name auth-api-dev bict/auth-api-dev tail -f /dev/null
```

## 프로덕션용

```bash
docker build -f dockerfile.prod -t bict/auth-api .
docker tag bict/auth-api 192.168.0.185:32000/bict/auth-api
docker push 192.168.0.185:32000/bict/auth-api
docker run --rm --gpus all bict/auth-api
```

# 환경변수

- SERVER_PORT
- CONTEXT_PATH
- DB_URL
- DB_USERNAME
- DB_PASSWORD
- KAFKA_BOOTSTRAP_SERVER
Additional commit for project setup and PR automation.
