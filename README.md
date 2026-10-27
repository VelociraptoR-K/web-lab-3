# Web Lab 3


### Стек:
- Java 21, Jakarta EE 11
- WildFly 41
- jOOQ
- PostgreSQL 18


---

## Запуск через Docker

Необходимы Docker и Docker Compose.

Все команды ниже выполняются из корня проекта.

1. Скопировать пример:

```bash
cp backend/.env.example backend/.env
```

В `backend/.env` указать пользователя, пароль и имя базы данных.

Сгенерировать JWT-секрет:

```bash
openssl rand -base64 32 | tr '+/' '-_' | tr -d '=\r\n'
```

Скопировать результат в `JWT_SECRET_BASE64URL` в `backend/.env`.

2. Файл ключа для проверки JWT

Создать папку:

```bash
mkdir -p backend/secrets
```

В ней создать файл `jwt-secret.jwk`:

```json
{
  "kty": "oct",
  "alg": "HS256",
  "use": "sig",
  "k": "СЮДА_ЗНАЧЕНИЕ_JWT_SECRET_BASE64URL_ИЗ_ENV"
}
```

В поле `k` вставить значение `JWT_SECRET_BASE64URL` из `backend/.env`.


3. Запуск

```bash
docker compose --env-file backend/.env up --build
```

---


Адреса:

| Сервис | Адрес |
|---|---|
| Фронтенд | http://localhost:2000 |
| API | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger.html |
| OpenAPI | http://localhost:8080/openapi |


---

## База данных

PostgreSQL работает в отдельном контейнере.
Данные сохраняются в именованном volume `postgres-data`.

При первой инициализации выполняется `backend/database/init.sql`.
При последующих запусках существующая БД сохраняется,
а скрипт повторно не выполняется.

### Пользователи и роли

Новые пользователи регистрируются с ролью USER.

| Роль | Доступ |
|---|---|
| USER | Список студентов и просмотр отдельного студента |
| ADMIN | Просмотр, создание, изменение и удаление студентов |


Чтобы назначить администратора нужно зарегистрировать пользователя,
затем выполнить в контейнерной БД:

```sql
UPDATE users
SET role = 'ADMIN'
WHERE username = 'username'
RETURNING id, username, role;
```

---

## Аутентификация

- Пароли хешируются с помощью PBKDF2.
- Access-токен JWT подписывается HS256 и действует 15 минут.
- Access передаётся в `Authorization: Bearer <token>`.
- Refresh-токен действует 7 дней, хранится в HttpOnly cookie.
- В БД сохраняется SHA-256-хеш refresh-токена.
- При обновлении refresh заменяется новым.
- При выходе refresh отзывается, cookie удаляется.
- Уже выданный access остаётся действительным до истечения его срока.


---

## Основные ручки

| Метод | Путь | Назначение |
|---|---|---|
| POST | /api/auth/register | Регистрация |
| POST | /api/auth/login | Вход |
| POST | /api/auth/refresh | Обновление токенов |
| POST | /api/auth/logout | Выход |
| GET | /api/auth/me | Текущий пользователь |
| GET | /api/requests | Список, фильтры, сортировка и пагинация |
| QUERY | /api/requests | Фильтры в JSON-теле |
| GET | /api/requests/{id} | Просмотр студента |
| POST | /api/requests | Создание студента |
| PATCH | /api/requests/{id} | Изменение студента |
| DELETE | /api/requests/{id} | Удаление студента |






