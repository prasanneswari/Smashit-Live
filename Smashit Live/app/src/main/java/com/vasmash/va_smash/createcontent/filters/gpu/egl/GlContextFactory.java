package com.vasmash.va_smash.createcontent.filters.gpu.egl;



public class GlContextFactory extends DefaultContextFactory {

    private static final int EGL_CONTEXT_CLIENT_VERSION = 2;

    public GlContextFactory() {
        super(EGL_CONTEXT_CLIENT_VERSION);
    }

}
