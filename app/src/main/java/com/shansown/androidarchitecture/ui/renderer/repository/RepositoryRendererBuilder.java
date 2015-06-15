package com.shansown.androidarchitecture.ui.renderer.repository;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;
import com.shansown.androidarchitecture.ui.model.Repository;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

@Singleton
public final class RepositoryRendererBuilder extends RendererBuilder<Repository> {

  @Inject
  public RepositoryRendererBuilder(TrendingRepositoryRenderer trendingRepositoryRenderer) {
    Collection<Renderer<Repository>> prototypes = new LinkedList<>();
    prototypes.add(trendingRepositoryRenderer);
    setPrototypes(prototypes);
    Timber.v("RepositoryRendererBuilder created: " + this);
  }

  @Override protected Class getPrototypeClass(Repository repository) {
    return TrendingRepositoryRenderer.class;
  }
}