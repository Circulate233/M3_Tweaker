package com.circulation.m3t.reload;

public interface M3TCrtReload {

    void reload();

    default void postReload() {}
}
