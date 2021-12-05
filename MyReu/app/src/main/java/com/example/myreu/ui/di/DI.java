package com.example.myreu.ui.di;

import com.example.myreu.ui.service.DummyReuApiService;

public class DI {

    /**
     * Singleton to get instance of services
     */
    private static final DummyReuApiService service = new DummyReuApiService();

    /**
     * Get an instance on @{@link DummyReuApiService}
     */
    public static DummyReuApiService getListReuApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link DummyReuApiService}.
     */
    public static DummyReuApiService getNewInstanceApiService() {
        return new DummyReuApiService();
    }
}