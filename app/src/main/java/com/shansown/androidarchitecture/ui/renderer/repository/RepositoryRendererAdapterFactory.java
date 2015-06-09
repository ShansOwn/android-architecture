package com.shansown.androidarchitecture.ui.renderer.repository;

import android.content.Context;
import android.view.LayoutInflater;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

@Singleton
public final class RepositoryRendererAdapterFactory {

  private final RepositoryRendererBuilder rendererBuilder;
  private final LayoutInflater layoutInflater;

  @Inject
  public RepositoryRendererAdapterFactory(Context context,
      RepositoryRendererBuilder rendererBuilder) {
    this.rendererBuilder = rendererBuilder;
    layoutInflater = LayoutInflater.from(context);
    Timber.v("RepositoryRendererAdapterFactory created: " + this);
  }

  public RVRendererAdapter<RepositoryData> getRendererAdapter(
      final RepositoryCollection repositoryCollection) {
    return new RVRendererAdapter<>(layoutInflater, rendererBuilder, repositoryCollection);
  }
}