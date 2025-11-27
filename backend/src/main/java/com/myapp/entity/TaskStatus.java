package com.myapp.entity;

public enum TaskStatus {
    TODO("К выполнению"),
    IN_PROGRESS("В работе"),
    REVIEW("На проверке"),
    DONE("Завершена"),
    BLOCKED("Заблокирована"),
    CANCELLED("Отменена");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}