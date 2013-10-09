package org.trinity.foundation.api.render;


import com.google.common.util.concurrent.ListenableFuture;

public interface ViewBuilder {

    ListenableFuture<Void> build(ViewBuilderResult viewBuildResult);
}
