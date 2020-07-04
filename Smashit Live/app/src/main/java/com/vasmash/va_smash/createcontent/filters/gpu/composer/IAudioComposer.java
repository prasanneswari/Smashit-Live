package com.vasmash.va_smash.createcontent.filters.gpu.composer;


interface IAudioComposer {

    void setup();

    boolean stepPipeline();

    long getWrittenPresentationTimeUs();

    boolean isFinished();

    void release();
}
