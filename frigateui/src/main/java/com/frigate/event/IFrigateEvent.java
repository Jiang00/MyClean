package com.frigate.event;

/**
 * Created by ${} on 2018/1/26.
 */

public interface IFrigateEvent {
    void onFrigateEvent(String eventName, String... params);
}
