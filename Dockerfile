# Используем официальный образ Maven в качестве базового образа для сборки
FROM maven:latest as stage1

# Устанавливаем рабочую директорию в /app
WORKDIR /app

# Копируем файл pom.xml в рабочую директорию
COPY pom.xml /app

# Разрешаем Maven разрешить зависимости
RUN mvn dependency:resolve

# Копируем все файлы из текущего контекста сборки в рабочую директорию
COPY . /app

# Очищаем предыдущие сборки Maven
RUN mvn clean

# Собираем приложение, при этом пропуская выполнение тестов
RUN mvn package -DskipTests

# Создаем финальный образ, используя OpenJDK
FROM openjdk:19 as final

# Копируем скомпилированный JAR-файл из предыдущего образа в текущую директорию
COPY --from=stage1 /app/target/*.jar app.jar

# Выставляем порт 8080 для внешних подключений
EXPOSE 8081

# Задаем команду для запуска приложения при запуске контейнера
CMD ["java", "-jar","-Dspring.profiles.active=prod", "app.jar"]
