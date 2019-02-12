package ru.otus.shw16;

/**
 * ДЗ-16: MessageServer
 * Cревер из ДЗ-15 разделить на три приложения:
 * • MessageServer
 * • Frontend
 * • DBServer
 * Запускать Frontend и DBServer из MessageServer.
 * Сделать MessageServer сокет-сервером, Frontend и DBServer клиентами.
 * Пересылать сообщения с Frontend на DBService через MessageServer.
 * Запустить приложение с двумя фронтендами (на разных портах)* и двумя датабазными серверами.
 *
 * * если у вас запуск веб приложения в контейнере, то MessageServer может копировать root.war в контейнеры при старте
 *
 * Пример запуска:
 * 1. MessageServer: mvn clean install exec:java -Dexec.mainClass="ru.otus.shw16.server.MessageSystemMain"
 * 2. DB: mvn exec:java -Dexec.mainClass="ru.otus.shw16.client.ClientMainDB" -Dexec.args="db-01 fe-01 0.0.0.0 5050"
 * 3. Front: mvn exec:java -Dexec.mainClass="ru.otus.shw16.client.ClientMainFrontEnd" -Dexec.args="fe-01 db-01 0.0.0.0 5050 8090"
 * 4. Front-2: mvn exec:java -Dexec.mainClass="ru.otus.shw16.client.ClientMainFrontEnd" -Dexec.args="fe-02 db-01 0.0.0.0 5050 8091"
 */

public class Main {

}
