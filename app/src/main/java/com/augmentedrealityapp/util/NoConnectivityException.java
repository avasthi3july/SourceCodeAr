package com.augmentedrealityapp.util;

import java.io.IOException;

/**
 * Created by prasharma on 1/16/2017.
 */

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }

}