# Отчет

Лабораторная работа №3. Использование
автоматических генераторов анализаторов
Bison и ANTLR

Вариант 7. Хороший язык

### Описание языка

- Все выражения по умолчанию заворачиваются в `main`.
- Все переменные имеют тип `int`.
- Есть чтение из стандартного ввода в виде `readInt()`
- Есть запись в стандартный вывод в виде `print a`
- Запись в переменные может осуществляться как `a = b`, так и как `(a, b) = (c, d)`
- `(a, b) = (b, a)` работает верно.
- Арифметика и логические выражения как в C.
- `if` как C, но без скобок.
- `for`, который вообще является `while`
- Функции через `fun <имя> | <параметры>`, `return`