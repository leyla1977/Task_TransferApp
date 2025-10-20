**Transfer Service - REST API для переводов между картами**
Task_TransferApp/
│ 
├── backend/        # Spring Boot приложение/
│ │   ├── src/main/java/ru.netology.springBootDemo
│           ├──configuration
│           ├──controller
│           ├──model
│           ├──service
│   ├── src/main/resource/application.properties - основные настройки
│
│   ├── src/test/java/ru.netology.springBootDemo
│ 
│           ├──controller    # Unit-тесты с Mockito
│           ├──integration   # Интеграционные тесты с Testcontainers      
│           ├──model         
│           ├──service├      # Unit-тесты с Mockito

│   ├── src/test/resource/application-test.properties - настройки для тестов
│   ├── target/
│   ├── Dockerfile
│   ├── transfer.log        # Лог файл
│   └── pom.xml
├── frontend/               # React приложение
├── docker-compose.yml
└── README.md

1. # Запуск backend и PostgreSQL (через Docker Compose)
в Git bash:
docker-compose up --build

2. **# Установка зависимостей и запуск**
в Command Prompt:
npm install
REACT_APP_API_URL=http://localhost:5500 npm start

3. **Доступ к приложению**
Backend API: http://localhost:5500
Frontend: http://localhost:3000
Логи операций: в файле transfer.log в директории C:\Work\Java\Task_TransferApp\backend\transfer.log

4. **Тестирование при помощи CURL**
   в Git bash:
   # Перевод денег
curl -X POST http://localhost:5500/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "cardFromNumber": "1234567812345678",
    "cardToNumber": "8765432187654321",
    "cardFromValidTill": "12/25",
    "cardFromCVV": "123",
    "amount": {
      "value": 1000,
      "currency": "RUB"
    }
  }'

# Подтверждение операции
curl -X POST http://localhost:5500/confirmOperation \
  -H "Content-Type: application/json" \
  -d '{
    "operationId": "123e4567-e89b-12d3-a456-426614174000",
    "code": "0000"
  }'
