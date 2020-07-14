package com.vasmash.va_smash.createcontent.filters.gpu;


import com.vasmash.va_smash.createcontent.filters.gpu.egl.filter.GlFilter;

public interface FilterAdjuster {
    public void adjust(GlFilter filter, int percentage);
}
