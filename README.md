Домашнее задание #1
Заготовка для социальной сети

Цель:
В результате выполнения ДЗ вы создадите базовый скелет социальной сети, который будет развиваться в дальнейших ДЗ.
В данном задании тренируются навыки:

* декомпозиции предметной области;
* построения элементарной архитектуры проекта

Описание/Пошаговая инструкция выполнения домашнего задания:

Требуется разработать создание и просмотр анкет в социальной сети.

**Функциональные требования:**

Простейшая авторизация пользователя.

Возможность создания пользователя, где указывается следующая информация:
* Имя
* Фамилия
* Возраст
* Пол
* Интересы
* Город
* Страницы с анкетой.

Нефункциональные требования:
* Любой язык программирования
* В качестве базы данных использовать MySQL (при остром желании PostgreSQL/MariaDB, но стоит иметь ввиду, что по этим базам может не быть возможности задать вопрос преподавателю)
* Не использовать ORM
* Программа должна представлять из себя монолитное приложение.

Не рекомендуется использовать следующие технологии:
* Репликация
* Шардинг
* Индексы
* Кэширование

Для удобства разработки и проверки задания можно воспользоваться этой спецификацией и реализовать в ней методы:
* /login
* /user/register
* /user/get/{id}

Фронт опционален.

Сделать инструкцию по локальному запуску приложения, приложить Postman-коллекцию.

ДЗ принимается в виде исходного кода на github и Postman-коллекции.


Критерии оценки: Оценка происходит по принципу зачет/незачет.

Требования:
* Есть возможность авторизации, регистрации, получение анкет по ID.
* Отсутствуют SQL-инъекции.
* Пароль хранится безопасно.

Рекомендуем сдать до: 20.11.2023