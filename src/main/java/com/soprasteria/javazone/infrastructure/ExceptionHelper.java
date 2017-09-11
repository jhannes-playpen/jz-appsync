package com.soprasteria.javazone.infrastructure;

public class ExceptionHelper {

    public static RuntimeException soften(Exception e) {
        return helper(e);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Exception> T helper(Exception e) throws T {
        throw (T)e;
    }

}
