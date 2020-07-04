package com.vasmash.va_smash.createcontent.filters.gpu.egl;


import com.daasuu.gpuv.egl.DefaultConfigChooser;

public class GlConfigChooser extends DefaultConfigChooser {

    private static final int EGL_CONTEXT_CLIENT_VERSION = 2;

    public GlConfigChooser(final boolean withDepthBuffer) {
        super(withDepthBuffer, EGL_CONTEXT_CLIENT_VERSION);
    }

}
