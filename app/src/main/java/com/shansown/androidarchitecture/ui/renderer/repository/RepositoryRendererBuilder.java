package com.shansown.androidarchitecture.ui.renderer.repository;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

@Singleton
public final class RepositoryRendererBuilder extends RendererBuilder<RepositoryData> {

  @Inject
  public RepositoryRendererBuilder(TrendingRepositoryRenderer trendingRepositoryRenderer) {
    Collection<Renderer<RepositoryData>> prototypes = new LinkedList<>();
    prototypes.add(trendingRepositoryRenderer);
    setPrototypes(prototypes);
    Timber.v("RepositoryRendererBuilder created: " + this);
  }

  @Override protected Class getPrototypeClass(RepositoryData repositoryData) {
    return TrendingRepositoryRenderer.class;
  }
}