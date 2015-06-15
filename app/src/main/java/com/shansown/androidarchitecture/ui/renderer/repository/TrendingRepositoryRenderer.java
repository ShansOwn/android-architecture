package com.shansown.androidarchitecture.ui.renderer.repository;

import android.content.Context;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.pedrogomez.renderers.Renderer;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.ui.model.Repository;
import com.shansown.androidarchitecture.ui.model.User;
import com.shansown.androidarchitecture.ui.misc.CircleStrokeTransformation;
import com.shansown.androidarchitecture.ui.misc.Truss;
import com.shansown.androidarchitecture.ui.trending.TrendingViewModel;
import com.squareup.picasso.Picasso;
import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

@Singleton
public final class TrendingRepositoryRenderer extends Renderer<Repository> {

  @InjectView(R.id.trending_repository_avatar) ImageView avatarView;
  @InjectView(R.id.trending_repository_name) TextView nameView;
  @InjectView(R.id.trending_repository_description) TextView descriptionView;
  @InjectView(R.id.trending_repository_stars) TextView starsView;
  @InjectView(R.id.trending_repository_forks) TextView forksView;

  private final Picasso picasso;
  @Inject Lazy<TrendingViewModel> viewModelLazy;
  private final CircleStrokeTransformation avatarTransformation;
  private final int descriptionColor;

  @Inject
  public TrendingRepositoryRenderer(Context context, Picasso picasso) {
    this.picasso = picasso;
    avatarTransformation = new CircleStrokeTransformation(context,
        context.getResources().getColor(R.color.avatar_stroke), 1);
    descriptionColor = context.getResources().getColor(R.color.text_secondary);
    Timber.v("TrendingRepositoryRenderer created: " + this);
  }

  @Override protected void setUpView(View rootView) {
    ButterKnife.inject(this, rootView);
  }

  @Override protected void hookListeners(View view) {
    //Empty because we are using ButterKnife library
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.trending_view_repository, parent, false);
  }

  @Override public void render() {
    Repository repository = getContent();
    User owner = repository.getOwner();

    renderAvatar(owner.getAvatarUrl());
    renderName(repository.getName());
    renderStars(String.valueOf(repository.getStars()));
    renderForks(String.valueOf(repository.getForks()));
    renderDescription(owner.getLogin(), repository.getDescription());
  }

  @OnClick(R.id.trending_repository_item) void onItemClicked() {
    viewModelLazy.get().onRepositoryClicked(getContent());
  }

  private void renderAvatar(String avatarUrl) {
    picasso.load(avatarUrl)
        .placeholder(R.drawable.avatar)
        .fit()
        .transform(avatarTransformation)
        .into(avatarView);
  }

  private void renderName(String name) {
    nameView.setText(name);
  }

  private void renderStars(String stars) {
    starsView.setText(stars);
  }

  private void renderForks(String forks) {
    forksView.setText(forks);
  }

  private void renderDescription(String login, String repoDescription) {
    Truss description = new Truss();
    description.append(login);
    if (repoDescription != null) {
      description.pushSpan(new ForegroundColorSpan(descriptionColor));
      description.append(" — ");
      description.append(repoDescription);
      description.popSpan();
    }
    descriptionView.setText(description.build());
  }
}