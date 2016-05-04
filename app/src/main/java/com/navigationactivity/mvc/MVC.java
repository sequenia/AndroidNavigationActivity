package com.navigationactivity.mvc;

/**
 * Содержит все операции взаимодействия для реализации MVC - Model-View-Controller.
 *
 * View - фрагмен, активити или любой другой экран.
 * Занимается только отображением данных и обработкой действий пользователя.
 * Источник данных и то, что делается по действиям пользователей, определяет Контроллер.
 *
 * Model - класс, который умеет скачивать, создавать, редактировать и удалять данные.
 * В нем содержится вся бизнес логика и общение с серверами/бд/SharedPreferences.
 * Контроллер достает отсюда данные и показывает во View.
 *
 * Controller - Передает события из View в Model и данные из Model во View.
 * Посредник между Моделью и Вью.
 * Умеет отображать данные во вью только в том случае, если Вью еще не уничтожена,
 * что критично для запросов.
 *
 * Created by chybakut2004 on 15.03.16.
 */
public interface MVC {

    /**
     * Главные операции, которые должна уметь делать View.
     * Все View должны уметь показывать разные сообщения.
     * Обязательно, чтобы все View реализовали его!
     */
    interface ViewOperations {

        /**
         * Показывает сообщение с указанным id (RequestCode)
         * @param text текст сообщения
         * @param requestCode RequestCode
         */
        void showMessage(String text, int requestCode);

        /**
         * Показывает сообщение с RequestCode по умолчанию
         * @param text текст сообщения
         */
        void showMessage(String text);

        /**
         * Закрывает View
         */
        void close();
    }

    /**
     * Действия, которые умеет делать любой контроллер:
     * Захватывать View, понимать что вью исчезло, безопасно выполнять дейсвия с View и т.д.
     * @param <V> Вью, которой управляет контроллер. Обязательно должна быть унаследована от ViewOperations,
     *           чтобы любой контроллер умел выполнять базовые данные над View.
     */
    interface ControllerOperations<V extends ViewOperations> {

        /**
         * Срабатывает, когда View доступна для отображения данных
         * @param view View
         * @param fromSaveInstanceState восстановлена ли View после пересоздания
         */
        void onTakeView(V view, boolean fromSaveInstanceState);

        /**
         * Срабатывает, когда view больше не доступна для отображения данных.
         * У фрагмента это onDetach,
         * У активити это onDestroy
         */
        void onLossView();

        /**
         * true, если View существует и можно производить действия над ней
         */
        boolean isViewAttached();

        /**
         * true, если View восстановлена после пересоздания
         */
        boolean isRestoring();
    }

    /**
     * Операции, которые может совершать модель.
     */
    interface ModelOperations {

        /**
         * Возвращает ссылку на контроллер
         */
        ControllerOperations getController();

        interface OnErrorListener {
            void onError(ModelError responseError);
        }
    }

    class ModelError {

        private String message;

        public ModelError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}